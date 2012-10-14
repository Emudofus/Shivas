package org.shivas.data.converter.loaders;

import org.shivas.data.converter.MapData;
import org.shivas.data.entity.*;

import java.util.Collection;

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
    public Collection<Breed> loadBreeds() throws Exception {
        Collection<Breed> breeds = first.loadBreeds();
        breeds.addAll(second.loadBreeds());
        return breeds;
    }

    @Override
    public Collection<Experience> loadExperiences() throws Exception {
        Collection<Experience> experiences = first.loadExperiences();
        experiences.addAll(second.loadExperiences());
        return experiences;
    }

    @Override
    public Collection<MapData> loadMaps() throws Exception {
        Collection<MapData> maps = first.loadMaps();
        maps.addAll(second.loadMaps());
        return maps;
    }

    @Override
    public Collection<ItemSet> loadItemSets() throws Exception {
        Collection<ItemSet> itemSets = first.loadItemSets();
        itemSets.addAll(second.loadItemSets());
        return itemSets;
    }

    @Override
    public Collection<ItemTemplate> loadItems() throws Exception {
        Collection<ItemTemplate> itemTemplates = first.loadItems();
        itemTemplates.addAll(second.loadItems());
        return itemTemplates;
    }

    @Override
    public Collection<SpellTemplate> loadSpells() throws Exception {
        Collection<SpellTemplate> spellTemplates = first.loadSpells();
        spellTemplates.addAll(second.loadSpells());
        return spellTemplates;
    }

    @Override
    public Collection<Waypoint> loadWaypoints() throws Exception {
        Collection<Waypoint> waypoints = first.loadWaypoints();
        waypoints.addAll(second.loadWaypoints());
        return waypoints;
    }

    @Override
    public Collection<NpcTemplate> loadNpcTemplates() throws Exception {
        Collection<NpcTemplate> npcTemplates = first.loadNpcTemplates();
        npcTemplates.addAll(second.loadNpcTemplates());
        return npcTemplates;
    }
}
