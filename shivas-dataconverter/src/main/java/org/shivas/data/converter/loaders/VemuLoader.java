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
import org.shivas.protocol.client.enums.*;

import java.sql.ResultSet;
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
    public Map<Integer, Breed> loadBreeds() throws Exception {
        throw new Exception("Vemu's db doesn't support this feature");
    }

    @Override
    public Map<Short, Experience> loadExperiences() throws Exception {
        Map<Short, Experience> experiences = Maps.newHashMapWithExpectedSize(200);

        for (ResultSet rset : select("exp_data").orderBy("Level", Order.ASC).execute()) {
            Experience exp = new Experience();
            exp.setLevel(rset.getShort("Level"));
            exp.setPlayer(rset.getLong("Character"));
            exp.setJob(rset.getInt("Job"));
            exp.setMount(rset.getInt("Mount"));
            exp.setAlignment(rset.getShort("Pvp"));
            exp.setGuild(rset.getLong("Guild"));

            experiences.put(exp.getLevel(), exp);
        }

        return experiences;
    }

    private Map<Integer, MapData> maps;

    @Override
    public Map<Integer, MapData> loadMaps() throws Exception {
        if (maps != null) {
            return maps;
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
        return maps;
    }

    @Override
    public Map<Short, ItemSet> loadItemSets() throws Exception {
        if (items == null) {
            loadItems();
        }

        Map<Short, ItemSet> itemSets = Maps.newHashMap();

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

            itemSets.put(itemSet.getId(), itemSet);
        }

        return itemSets;
    }

    private Map<Short, ItemTemplate> items;

    @Override
    public Map<Short, ItemTemplate> loadItems() throws Exception {
        if (items != null) {
            return items;
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
        return items;
    }

    private List<SpellEffect> loadEffects(String string) {
        List<SpellEffect> effects = Lists.newArrayList();
        for (String str : string.split("\\|")) {
            if (str.equalsIgnoreCase("-1") || str.isEmpty()) continue;

            String[] args = str.split(";");
            if (args.length <= 1) continue;

            SpellEffect effect = new SpellEffect();
            effect.setType(SpellEffectsEnum.valueOf(Integer.parseInt(args[0])));
            effect.setFirst(Short.parseShort(args[1]));
            effect.setSecond(Short.parseShort(args[2]));
            effect.setThird(Short.parseShort(args[3]));
            if (args.length > 4) effect.setTurns(Short.parseShort(args[4]));
            if (args.length > 5) effect.setChance(Short.parseShort(args[5]));
            if (args.length > 6) effect.setDice(Dofus1Dice.parseDice(args[6]));
            if (args.length > 7) effect.setTarget(args[7]);

            effects.add(effect);
        }
        return effects;
    }

    @Override
    public Map<Short, SpellTemplate> loadSpells() throws Exception {
        Map<Short, SpellTemplate> spells = Maps.newHashMapWithExpectedSize(1882);

        for (ResultSet rset : select("spells_data").execute()) {
            SpellTemplate spell = new SpellTemplate();

            spell.setId(rset.getShort("id"));
            spell.setSprite(rset.getShort("sprite"));
            spell.setSpriteInfos(rset.getString("spriteInfos"));

            SpellLevel[] levels = new SpellLevel[6];
            for (byte i = 1; i <= 6; ++i) {
                String str = rset.getString("lvl" + i);
                if (str.equalsIgnoreCase("-1") || str.isEmpty()) continue;
                String[] args = str.split(",");

                SpellLevel level = new SpellLevel();
                level.setId(i);
                level.setCostAP(args[2].isEmpty() ? 6 : Byte.parseByte(args[2].trim()));
                level.setMinRange(Byte.parseByte(args[3].trim()));
                level.setMaxRange(Byte.parseByte(args[4].trim()));
                level.setCriticalRate(Short.parseShort(args[5].trim()));
                level.setFailureRate(Short.parseShort(args[6].trim()));
                level.setInline(args[7].trim().equalsIgnoreCase("true"));
                level.setLos(args[8].trim().equalsIgnoreCase("true"));
                level.setEmptyCell(args[9].trim().equalsIgnoreCase("true"));
                level.setAdjustableRange(args[10].trim().equalsIgnoreCase("true"));
                level.setMaxPerTurn(Byte.parseByte(args[12].trim()));
                level.setMaxPerPlayer(Byte.parseByte(args[13].trim()));
                level.setTurns(Byte.parseByte(args[14].trim()));
                level.setRangeType(args[15].trim());
                level.setEndsTurnOnFailure(args[19].trim().equalsIgnoreCase("true"));

                level.setEffects(loadEffects(args[0]));
                level.setCriticalEffects(loadEffects(args[1]));

                levels[i - 1] = level;
            }
            spell.setLevels(levels);

            spells.put(spell.getId(), spell);
        }

        return spells;
    }

    @Override
    public Map<Integer, Waypoint> loadWaypoints() throws Exception {
        if (maps == null) {
            loadMaps();
        }

        Map<Integer, Waypoint> waypoints = Maps.newHashMap();

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

            waypoints.put(waypoint.getId(), waypoint);
        }

        return waypoints;
    }

    @Override
    public Map<Integer, NpcTemplate> loadNpcTemplates() throws Exception {
        if (items == null) {
            loadItems();
        }

        Map<Integer, NpcTemplate> npcs = Maps.newHashMap();

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

            npcs.put(npc.getId(), npc);
        }

        return npcs;
    }
}
