package org.shivas.data.converter.loaders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.util.query.Op;
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

import static java.util.Arrays.asList;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:24
 */
public class AncestraLoader extends JDBCLoader {
    public AncestraLoader(String hostname, String user, String passwd, String database) {
        super(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", hostname, database, user, passwd), "com.mysql.jdbc.Driver", new MySqlQueryBuilderFactory());
    }

    @Override
    public Map<Integer, Breed> loadBreeds() throws Exception {
        throw new Exception("Ancestra's db doesn't support this feature");
    }

    @Override
    public Map<Short, Experience> loadExperiences() throws Exception {
        Map<Short, Experience> experiences = Maps.newHashMapWithExpectedSize(200);

        Experience last = null;
        for (ResultSet rset : select("experience").orderBy("lvl", Order.ASC).execute()) {
            Experience experience = new Experience();

            experience.setLevel(rset.getShort("lvl"));
            experience.setPlayer(rset.getLong("perso"));
            experience.setGuild(experience.getPlayer() * 10);
            experience.setJob(rset.getInt("metier"));
            experience.setMount(rset.getInt("dinde"));
            experience.setAlignment(rset.getShort("pvp"));

            experience.setPrevious(last);
            if (last != null) last.setNext(experience);

            last = experience;

            experiences.put(experience.getLevel(), experience);
        }

        return experiences;
    }

    private Map<Integer, MapData> maps;

    @Override
    public Map<Integer, MapData> loadMaps() throws Exception {
        if (maps != null) {
            return maps;
        }

        Map<Integer, MapData> maps = Maps.newHashMapWithExpectedSize(6000);

        for (ResultSet rset : select("maps").execute()) {
            if (rset.getString("mapData").isEmpty()) {
                App.outln("map N°%d invalide", rset.getInt("id"));
            }

            MapData map = new MapData();
            map.setId(rset.getInt("id"));
            map.setDate(rset.getString("date"));
            map.setWidth(rset.getInt("width"));
            map.setHeight(rset.getInt("heigth"));
            map.setKey(rset.getString("key"));
            map.setData(rset.getString("mapData"));

            String places = rset.getString("places");
            if (!places.isEmpty()) {
                List<String> startCells = Lists.newArrayList();
                for (String p : places.split("\\|")) {
                    if (p.isEmpty()) continue;
                    startCells.add(p);
                }

                if (startCells.size() == 2) {
                    map.setStartCells(startCells);
                    map.setCanFight(true);
                }
            }

            String[] positions = rset.getString("mappos").split(",");
            map.setPosition(new Point(
                    Integer.parseInt(positions[0]),
                    Integer.parseInt(positions[1])
            ));

            map.setTrigger(Maps.<Short, MapTrigger>newHashMap());

            maps.put(map.getId(), map);
        }

        for (ResultSet rset : select("scripted_cells").where("EventID", Op.EQ, 1).orderBy("MapID", Order.ASC).execute()) {
            MapData map = maps.get(rset.getInt("MapID"));
            if (map == null) {
                App.outln("un trigger fait référence à une map inconnue (N°%d)", rset.getInt("MapID"));
                continue;
            }

            MapTrigger trigger = new MapTrigger();
            trigger.setMap(map);
            trigger.setCell(rset.getShort("CellID"));

            String[] targetArgs = rset.getString("ActionsArgs").split(",");
            if (targetArgs.length != 2) {
                App.outln("trigger malformé (map %d cell %d)", trigger.getMap().getId(), trigger.getCell());
                continue;
            }
            MapData targetMap = maps.get(Integer.parseInt(targetArgs[0]));
            if (targetMap == null) {
                App.outln("un trigger fait référence à une map inconnue (N°%d)", targetArgs[0]);
                continue;
            }
            trigger.setNextMap(targetMap);
            trigger.setNextCell(Short.parseShort(targetArgs[1]));

            map.getTrigger().put(trigger.getCell(), trigger);
        }

        this.maps = maps;
        return maps;
    }

    private Map<Short, ItemSet> itemSets;

    @Override
    public Map<Short, ItemSet> loadItemSets() throws Exception {
        if (itemSets != null) {
            return itemSets;
        }

        Map<Short, ItemSet> itemSets = Maps.newHashMap();

        for (ResultSet rset : select("itemsets").execute()) {
            ItemSet itemSet = new ItemSet();

            itemSet.setId(rset.getShort("ID"));
            itemSet.setItems(Lists.<ItemTemplate>newArrayList());
            itemSet.setEffects(HashMultimap.<Integer, ConstantItemEffect>create());

            int level = 2;
            for (String bonus : rset.getString("bonus").split(";")) {
                if (bonus.isEmpty()) continue;
                for (String effect : bonus.split(",")) {
                    String[] args = effect.split(":");

                    itemSet.getEffects().put(level, new ConstantItemEffect(
                            ItemEffectEnum.valueOf(Integer.parseInt(args[0].trim())),
                            Short.parseShort(args[1].trim().replace("%", ""))
                    ));
                }
                ++level;
            }

            itemSets.put(itemSet.getId(), itemSet);
        }

        this.itemSets = itemSets;
        return itemSets;
    }

    private Map<Short, ItemTemplate> items;

    @Override
    public Map<Short, ItemTemplate> loadItems() throws Exception {
        if (items != null) {
            return items;
        }
        if (itemSets == null) {
            loadItemSets();
        }

        Map<Short, ItemTemplate> items = Maps.newHashMap();

        for (ResultSet rset : select("item_template").execute()) {
            ItemTypeEnum type = ItemTypeEnum.valueOf(rset.getInt("type"));

            ItemTemplate itemTemplate;
            if (type.isWeapon()) {
                String[] infos = rset.getString("armesInfos").split(";");
                if (infos.length < 7) continue;

                WeaponTemplate weaponTemplate = new WeaponTemplate(null);
                weaponTemplate.setCost(Short.parseShort(infos[0]));
                weaponTemplate.setMinRange(Short.parseShort(infos[1]));
                weaponTemplate.setMaxRange(Short.parseShort(infos[2]));
                weaponTemplate.setCriticalRate(Short.parseShort(infos[3]));
                weaponTemplate.setFailureRate(Short.parseShort(infos[4]));
                weaponTemplate.setCriticalBonus(Short.parseShort(infos[5]));
                weaponTemplate.setTwoHands(infos[6].equals("1"));
                weaponTemplate.setEthereal(false);

                itemTemplate = weaponTemplate;
            } else if (type.isUsable()) {
                itemTemplate = new UsableItemTemplate(null); // TODO usable items
            } else {
                itemTemplate = new ItemTemplate(null);
            }

            itemTemplate.setId(rset.getShort("id"));
            itemTemplate.setType(type);
            itemTemplate.setLevel(rset.getShort("level"));
            itemTemplate.setWeight(rset.getShort("pod"));
            itemTemplate.setItemSet(itemSets.get(rset.getShort("panoplie")));
            itemTemplate.setPrice((short) rset.getInt("prix"));
            itemTemplate.setConditions(rset.getString("condition"));
            itemTemplate.setForgemageable(true);

            List<ItemEffectTemplate> effects = Lists.newArrayList();
            for (String effectString : rset.getString("statsTemplate").split(",")) {
                if (effectString.isEmpty()) continue;
                String[] args = effectString.split("#");

                ItemEffectTemplate effect = new ItemEffectTemplate(null);
                effect.setEffect(ItemEffectEnum.valueOf(Integer.parseInt(args[0], 16)));
                try {
                    effect.setBonus(Dofus1Dice.parseDice(args[4]));
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
            effect.setType(SpellEffectTypeEnum.valueOf(Integer.parseInt(args[0])));
            effect.setFirst(Short.parseShort(args[1]));
            effect.setSecond(Short.parseShort(args[2]));
            effect.setThird(Short.parseShort(args[3]));
            if (args.length > 4) effect.setTurns(Short.parseShort(args[4]));
            if (args.length > 5) effect.setChance(Short.parseShort(args[5]));
            if (args.length > 6) effect.setDice(Dofus1Dice.parseDice(args[6]));
            if (args.length > 7 && !args[7].isEmpty()) effect.setTargetRaw(Integer.parseInt(args[7]));

            effects.add(effect);
        }
        return effects;
    }

    private void loadZonesRaw(List<SpellEffect> effects, List<SpellEffect> criticalEffects, String zones) {
        int i = 0;
        for (List<SpellEffect> current : asList(effects, criticalEffects)) {
            for (SpellEffect effect : current) {
                String zone = zones.substring(i, i + 2);
                effect.setZoneRaw(zone);

                i += 2;
                if (i >= zones.length()) i = 0;
            }
        }
    }

    @Override
    public Map<Short, SpellTemplate> loadSpells() throws Exception {
        Map<Short, SpellTemplate> spells = Maps.newHashMapWithExpectedSize(1882);

        for (ResultSet rset : select("sorts").execute()) {
            SpellTemplate spellTemplate = new SpellTemplate();

            spellTemplate.setId(rset.getShort("id"));
            spellTemplate.setSprite(rset.getShort("sprite"));
            spellTemplate.setSpriteInfos(rset.getString("spriteInfos"));

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
                level.setEndsTurnOnFailure(args[19].trim().equalsIgnoreCase("true"));

                List<SpellEffect> effects = loadEffects(args[0]), criticalEffects = loadEffects(args[1]);
                loadZonesRaw(effects, criticalEffects, args[15].trim());
                level.setEffects(effects);
                level.setCriticalEffects(criticalEffects);

                levels[i - 1] = level;
            }
            spellTemplate.setLevels(levels);

            spells.put(spellTemplate.getId(), spellTemplate);
        }

        return spells;
    }

    @Override
    public Map<Integer, Waypoint> loadWaypoints() throws Exception {
        Map<Integer, Waypoint> waypoints = Maps.newHashMap();

        int id = 0;
        for (ResultSet rset : select("zaaps").execute()) {
            Waypoint waypoint = new Waypoint();
            waypoint.setId(++id);
            waypoint.setMap(maps.get(rset.getInt("mapID")));
            waypoint.setCell(rset.getShort("cellID"));

            waypoints.put(waypoint.getId(), waypoint);
        }

        return waypoints;
    }

    @Override
    public Map<Integer, NpcTemplate> loadNpcTemplates() throws Exception {
        if (items == null) {
            loadItems();
        }

        Map<Integer, NpcTemplate> npcTemplates = Maps.newHashMap();

        for (ResultSet rset : select("npc_template").execute()) {
            NpcTemplate npcTemplate = new NpcTemplate();

            npcTemplate.setId(rset.getInt("id"));
            npcTemplate.setSkin(rset.getShort("gfxID"));
            npcTemplate.setSize(rset.getShort("scaleX"));
            npcTemplate.setGender(Gender.valueOf(rset.getInt("sex")));
            npcTemplate.setColor1(rset.getInt("color1"));
            npcTemplate.setColor2(rset.getInt("color2"));
            npcTemplate.setColor3(rset.getInt("color3"));
            npcTemplate.setExtraClip(rset.getInt("extraClip"));
            npcTemplate.setCustomArtwork(rset.getInt("customArtWork"));

            ItemTemplate[] accessories = new ItemTemplate[5];
            int i = 0;
            for (String accessoryString : rset.getString("accessories").split(",")) {
                if (accessoryString.isEmpty()) continue;

                ItemTemplate accessory = items.get(Short.parseShort(accessoryString, 16));
                accessories[i++] = accessory;
            }
            npcTemplate.setAccessories(accessories);

            if (!rset.getString("ventes").isEmpty()) {
                npcTemplate.setType(NpcTypeEnum.BUY_SELL);
            } else {
                npcTemplate.setType(NpcTypeEnum.SPEAK);
            }

            npcTemplates.put(npcTemplate.getId(), npcTemplate);
        }

        return npcTemplates;
    }
}
