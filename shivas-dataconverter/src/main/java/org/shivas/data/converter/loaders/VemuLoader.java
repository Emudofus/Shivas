package org.shivas.data.converter.loaders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.util.query.Order;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.shivas.common.maths.Point;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.data.converter.App;
import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;
import org.shivas.protocol.client.enums.NpcTypeEnum;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 14/10/12
 * Time: 17:21
 */
public class VemuLoader extends JDBCLoader {
    public VemuLoader(String hostname, String user, String password, String database) {
        super(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", hostname, database, user, password), "com.mysql.jdbc.Driver", new MySqlQueryBuilderFactory());
    }

    @Override
    public Collection<Breed> loadBreeds() throws Exception {
        throw new Exception("Vemu's db doesn't support this feature");
    }

    @Override
    public Collection<Experience> loadExperiences() throws Exception {
        List<Experience> experiences = Lists.newArrayList();

        for (ResultSet rset : select("exp_data").orderBy("Level", Order.ASC).execute()) {
            Experience exp = new Experience();
            exp.setLevel(rset.getShort("Level"));
            exp.setPlayer(rset.getLong("Character"));
            exp.setJob(rset.getInt("Job"));
            exp.setMount(rset.getInt("Mount"));
            exp.setAlignment(rset.getShort("Pvp"));
            exp.setGuild(rset.getLong("Guild"));

            experiences.add(exp);
        }

        return experiences;
    }

    private Map<Integer, MapData> maps;

    @Override
    public Collection<MapData> loadMaps() throws Exception {
        if (maps != null) {
            return Lists.newArrayList(maps.values());
        }

        Map<Integer, MapData> maps = Maps.newHashMap();

        for (ResultSet rset : select("maps_data").execute()) {
            MapData map = new MapData();
            map.setId(rset.getInt("ID"));
            map.setWidth(rset.getInt("Width"));
            map.setHeight(rset.getInt("Height"));
            map.setData(rset.getString("MapData"));
            map.setKey(rset.getString("DecryptKey"));
            map.setDate(rset.getString("CreateTime"));

            String[] position = rset.getString("pos").split(",");
            map.setPosition(new Point(
                    Integer.parseInt(position[0].trim()),
                    Integer.parseInt(position[1].trim())
            ));

            map.setSubscriber(rset.getBoolean("NeedRegister"));

            map.setTrigger(Maps.<Short, MapTrigger>newHashMap());

            maps.put(map.getId(), map);
        }

        for (ResultSet rset : select("maps_triggers").execute()) {
            MapData map = maps.get(rset.getInt("MapID"));
            if (map == null) {
                App.outln("un trigger fait référence à une map inconnue (N°%d)", rset.getInt("MapID"));
                continue;
            }

            MapTrigger trigger = new MapTrigger();
            trigger.setMap(map);
            trigger.setCell(rset.getShort("CellID"));

            String[] targetInfos = rset.getString("Action").split("/")[1].split(";");
            if (targetInfos.length != 2) {
                App.outln("trigger malformé (map %d cell %d)", map.getId(), trigger.getCell());
                continue;
            }
            MapData target = maps.get(Integer.parseInt(targetInfos[0]));
            if (target == null) {
                App.outln("un trigger fait référence à une map inconnue (N°%s)", targetInfos[0]);
                continue;
            }

            trigger.setNextMap(target);
            trigger.setNextCell(Short.parseShort(targetInfos[1]));

            map.getTrigger().put(trigger.getCell(), trigger);
        }

        this.maps = maps;
        return Lists.newArrayList(maps.values());
    }

    @Override
    public Collection<ItemSet> loadItemSets() throws Exception {
        if (items == null) {
            loadItems();
        }

        List<ItemSet> itemSets = Lists.newArrayList();

        for (ResultSet rset : select("items_pano").execute()) {
            ItemSet itemSet = new ItemSet();

            itemSet.setId(rset.getShort("id"));
            itemSet.setItems(Lists.<ItemTemplate>newArrayList());
            itemSet.setEffects(HashMultimap.<Integer, ConstantItemEffect>create());

            for (String itemString : rset.getString("items").split(",")) {
                ItemTemplate item = items.get(Short.parseShort(itemString));
                if (item == null) {
                    App.outln("la panoplie N°%d fait référence à un objet inexistant (id=%s)", itemSet.getId(), itemString);
                    continue;
                }
                item.setItemSet(itemSet);
                itemSet.getItems().add(item);
            }

            for (int level = 2; level <= 8; ++level) {
                String effects = rset.getString("effects" + level);
                if (effects.isEmpty()) continue;

                for (String effect : effects.split(";")) {
                    String[] args = effect.split(",");

                    itemSet.getEffects().put(level, new ConstantItemEffect(
                            ItemEffectEnum.valueOf(Integer.parseInt(args[0].trim())),
                            Short.parseShort(args[1].trim())
                    ));
                }
            }

            itemSets.add(itemSet);
        }

        return itemSets;
    }

    private Map<Short, ItemTemplate> items;

    @Override
    public Collection<ItemTemplate> loadItems() throws Exception {
        if (items != null) {
            return Lists.newArrayList(items.values());
        }

        Map<Short, ItemTemplate> items = Maps.newHashMap();

        for (ResultSet rset : select("items_data").execute()) {
            ItemTemplate itemTemplate;

            ItemTypeEnum type = ItemTypeEnum.valueOf(rset.getInt("Type"));
            if (type.isWeapon()) {
                WeaponTemplate weaponTemplate = new WeaponTemplate(null);
                weaponTemplate.setTwoHands(rset.getBoolean("TwoHands"));
                weaponTemplate.setEthereal(rset.getBoolean("IsEthereal"));

                itemTemplate = weaponTemplate;
            } else if (type.isUsable()) {
                itemTemplate = new UsableItemTemplate(null); // TODO usable items
            } else {
                itemTemplate = new ItemTemplate(null);
            }

            itemTemplate.setId(rset.getShort("ID"));
            itemTemplate.setType(type);
            itemTemplate.setLevel(rset.getShort("Level"));
            itemTemplate.setWeight(rset.getShort("Weight"));
            itemTemplate.setForgemageable(rset.getBoolean("Forgemageable"));
            itemTemplate.setPrice((short) rset.getInt("Price"));
            itemTemplate.setConditions(rset.getString("Conditions"));

            List<ItemEffectTemplate> effects = Lists.newArrayList();
            for (String effectString : rset.getString("Stats").split(",")) {
                if (effectString.isEmpty()) continue;
                String[] args = effectString.split("#");

                ItemEffectTemplate effect = new ItemEffectTemplate();
                effect.setEffect(ItemEffectEnum.valueOf(Integer.parseInt(args[0], 16)));
                try {
                    effect.setBonus(Dofus1Dice.parseDice(args[4].trim()));
                } catch (IndexOutOfBoundsException ex) {
                    effect.setBonus(new Dofus1Dice());
                }

                effects.add(effect);
            }
            itemTemplate.setEffects(effects);

            items.put(itemTemplate.getId(), itemTemplate);
        }

        this.items = items;
        return Lists.newArrayList(items.values());
    }

    @Override
    public Collection<SpellTemplate> loadSpells() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Waypoint> loadWaypoints() throws Exception {
        if (maps == null) {
            loadMaps();
        }

        List<Waypoint> waypoints = Lists.newArrayList();

        int id = 0;
        for (ResultSet rset : select("zaaps").execute()) {
            Waypoint waypoint = new Waypoint();
            waypoint.setId(++id);

            MapData map = maps.get(rset.getInt("mapid"));
            if (map == null) {
                App.outln("un zaap fait référence à une map inconnue (N°%d)", rset.getInt("mapid"));
                continue;
            }

            waypoint.setMap(map);
            waypoint.setCell(rset.getShort("cellid"));

            waypoints.add(waypoint);
        }

        return waypoints;
    }

    @Override
    public Collection<NpcTemplate> loadNpcTemplates() throws Exception {
        if (items == null) {
            loadItems();
        }

        List<NpcTemplate> npcs = Lists.newArrayList();

        for (ResultSet rset : select("npcs_templates").execute()) {
            NpcTemplate npc = new NpcTemplate();

            npc.setId(rset.getInt("ID"));
            npc.setSkin(rset.getShort("Gfx"));
            npc.setSize(rset.getShort("Size"));
            npc.setGender(Gender.valueOf(rset.getInt("Sex")));
            npc.setColor1(rset.getInt("Color1"));
            npc.setColor2(rset.getInt("Color2"));
            npc.setColor3(rset.getInt("Color3"));
            npc.setCustomArtwork(rset.getInt("ArtWork"));
            npc.setExtraClip(rset.getInt("Bonus"));
            if (!rset.getString("SellingList").isEmpty()) {
                npc.setType(NpcTypeEnum.BUY_SELL);
            } else {
                npc.setType(NpcTypeEnum.SPEAK);
            }

            ItemTemplate[] accessories = new ItemTemplate[5];
            int i = 0;
            for (String accessoryString : rset.getString("Items").split(",")) {
                if (accessoryString.isEmpty()) continue;

                ItemTemplate accessory = items.get(Short.parseShort(accessoryString.trim(), 16));
                accessories[i++] = accessory;
            }
            npc.setAccessories(accessories);

            npcs.add(npc);
        }

        return npcs;
    }
}
