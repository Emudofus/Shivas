package org.shivas.data.converter.loaders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.util.query.Order;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.shivas.common.maths.Point;
import org.shivas.data.converter.App;
import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

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
