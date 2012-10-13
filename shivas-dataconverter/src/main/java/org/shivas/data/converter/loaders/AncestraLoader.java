package org.shivas.data.converter.loaders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.util.query.Op;
import org.atomium.util.query.Order;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.shivas.common.maths.Point;
import org.shivas.data.converter.App;
import org.shivas.data.converter.JDBCLoader;
import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

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

    @Override
    public Collection<ItemSet> loadItemSets() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<ItemTemplate> loadItems() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<SpellTemplate> loadSpells() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
