package org.shivas.data.converter.loaders;

import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 15:04
 */
public class CombinedLoader implements DataLoader {
    private final DataLoader first, second;

    public CombinedLoader(DataLoader first, DataLoader second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Map<Integer, Breed> loadBreeds() throws Exception {
        Map<Integer, Breed> breeds = first.loadBreeds();
        breeds.putAll(second.loadBreeds());
        return breeds;
    }

    @Override
    public Map<Short, Experience> loadExperiences() throws Exception {
        Map<Short, Experience> experiences = first.loadExperiences();
        experiences.putAll(second.loadExperiences());
        return experiences;
    }

    @Override
    public Map<Integer, MapData> loadMaps() throws Exception {
        Map<Integer, MapData> maps = first.loadMaps();
        maps.putAll(second.loadMaps());
        return maps;
    }

    @Override
    public Map<Short, ItemSet> loadItemSets() throws Exception {
        Map<Short, ItemSet> itemSets = first.loadItemSets();
        itemSets.putAll(second.loadItemSets());
        return itemSets;
    }

    @Override
    public Map<Short, ItemTemplate> loadItems() throws Exception {
        Map<Short, ItemTemplate> itemTemplates = first.loadItems();
        itemTemplates.putAll(second.loadItems());
        return itemTemplates;
    }

    @Override
    public Map<Short, SpellTemplate> loadSpells() throws Exception {
        Map<Short, SpellTemplate> spellTemplates = first.loadSpells();
        spellTemplates.putAll(second.loadSpells());
        return spellTemplates;
    }

    @Override
    public Map<Integer, Waypoint> loadWaypoints() throws Exception {
        Map<Integer, Waypoint> waypoints = first.loadWaypoints();
        waypoints.putAll(second.loadWaypoints());
        return waypoints;
    }

    @Override
    public Map<Integer, NpcTemplate> loadNpcTemplates() throws Exception {
        Map<Integer, NpcTemplate> npcTemplates = first.loadNpcTemplates();
        npcTemplates.putAll(second.loadNpcTemplates());
        return npcTemplates;
    }
}
