package org.shivas.data.converter.loaders;

import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.util.Collection;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Experience> loadExperiences() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
