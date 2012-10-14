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
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;
import org.shivas.protocol.client.enums.SpellEffectsEnum;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public Collection<Breed> loadBreeds() throws Exception {
        throw new Exception("Ancestra's db doesn't support this feature");
    }

    @Override
    public Collection<Experience> loadExperiences() throws Exception {
        List<Experience> experiences = Lists.newArrayListWithExpectedSize(200);

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

            experiences.add(experience);
        }

        return experiences;
    }

    @Override
    public Collection<MapData> loadMaps() throws Exception {
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

        return Lists.newArrayList(maps.values());
    }

    private Map<Short, ItemSet> itemSets;

    @Override
    public Collection<ItemSet> loadItemSets() throws Exception {
        if (this.itemSets != null) {
            return this.itemSets.values();
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
        return itemSets.values();
    }

    @Override
    public Collection<ItemTemplate> loadItems() throws Exception {
        if (itemSets == null) {
            loadItemSets();
        }

        List<ItemTemplate> itemTemplates = Lists.newArrayList();

        for (ResultSet rset : select("item_template").execute()) {
            ItemTypeEnum type = ItemTypeEnum.valueOf(rset.getInt("type"));

            ItemTemplate itemTemplate;
            if (type.isWeapon()) {
                String[] infos = rset.getString("armesInfos").split(";");
                if (infos.length < 7) continue;

                WeaponTemplate weaponTemplate = new WeaponTemplate(null);
                weaponTemplate.setEthereal(false);
                weaponTemplate.setTwoHands(infos[6].equals("1"));

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

                ItemEffectTemplate effect = new ItemEffectTemplate();
                effect.setEffect(ItemEffectEnum.valueOf(Integer.parseInt(args[0], 16)));
                try {
                    effect.setBonus(Dofus1Dice.parseDice(args[4]));
                } catch (IndexOutOfBoundsException ex) {
                    effect.setBonus(new Dofus1Dice());
                }

                effects.add(effect);
            }
            itemTemplate.setEffects(effects);

            itemTemplates.add(itemTemplate);
        }

        return itemTemplates;
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
    public Collection<SpellTemplate> loadSpells() throws Exception {
        List<SpellTemplate> spells = Lists.newArrayList();

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
                level.setRangeType(args[15].trim());
                level.setEndsTurnOnFailure(args[19].trim().equalsIgnoreCase("true"));

                level.setEffects(loadEffects(args[0]));
                level.setCriticalEffects(loadEffects(args[1]));

                levels[i - 1] = level;
            }
            spellTemplate.setLevels(levels);

            spells.add(spellTemplate);
        }

        return spells;
    }

    @Override
    public Collection<Waypoint> loadWaypoints() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<NpcTemplate> loadNpcTemplates() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
