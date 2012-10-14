package org.shivas.data.converter.loaders;

import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 13:59
 */
public interface DataLoader {

    Collection<Breed> loadBreeds() throws Exception;
    Collection<Experience> loadExperiences() throws Exception;
    Collection<MapData> loadMaps() throws Exception;
    Collection<ItemSet> loadItemSets() throws Exception;
    Collection<ItemTemplate> loadItems() throws Exception;
    Collection<SpellTemplate> loadSpells() throws Exception;
    Collection<Waypoint> loadWaypoints() throws Exception;
    Collection<NpcTemplate> loadNpcTemplates() throws Exception;

}
