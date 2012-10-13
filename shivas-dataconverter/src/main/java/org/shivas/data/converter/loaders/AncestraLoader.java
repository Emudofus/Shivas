package org.shivas.data.converter.loaders;

import com.google.common.collect.Lists;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.shivas.data.converter.JDBCLoader;
import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

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
        for (ResultSet rset : select("experiences").execute()) {
            Experience experience = new Experience();

            experience.setPrevious(last);
            if (last != null) last.setNext(experience);

            last = experience;
        }

        return experiences;
    }

    @Override
    public Collection<MapData> loadMaps() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
