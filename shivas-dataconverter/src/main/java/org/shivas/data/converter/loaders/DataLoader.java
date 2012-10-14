package org.shivas.data.converter.loaders;

import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 13:59
 */
public interface DataLoader {

    Map<Integer, Breed> loadBreeds() throws Exception;
    Map<Short, Experience> loadExperiences() throws Exception;
    Map<Integer, MapData> loadMaps() throws Exception;
    Map<Short, ItemSet> loadItemSets() throws Exception;
    Map<Short, ItemTemplate> loadItems() throws Exception;
    Map<Short, SpellTemplate> loadSpells() throws Exception;
    Map<Integer, Waypoint> loadWaypoints() throws Exception;
    Map<Integer, NpcTemplate> loadNpcTemplates() throws Exception;

}
