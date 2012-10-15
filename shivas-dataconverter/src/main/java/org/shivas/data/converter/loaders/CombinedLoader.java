package org.shivas.data.converter.loaders;

import com.google.common.collect.Maps;
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

    public static <V> void merge(V source, V target) {
        // TODO merge entity
    }

    protected <K, V> Map<K, V> mergeAll(Map<K, V> target, Map<K, V> source) {
        Map<K, V> result = Maps.newHashMap();

        for (Map.Entry<K, V> entry : source.entrySet()) {
            V first = entry.getValue(),
              second = target.get(entry.getKey());

            if (second == null) {
                target.put(entry.getKey(), first);
            } else {
                merge(first, second);
            }
        }

        return result;
    }

    @Override
    public Map<Integer, Breed> loadBreeds() throws Exception {
        return mergeAll(first.loadBreeds(), second.loadBreeds());
    }

    @Override
    public Map<Short, Experience> loadExperiences() throws Exception {
        return mergeAll(first.loadExperiences(), second.loadExperiences());
    }

    @Override
    public Map<Integer, MapData> loadMaps() throws Exception {
        return mergeAll(first.loadMaps(), second.loadMaps());
    }

    @Override
    public Map<Short, ItemSet> loadItemSets() throws Exception {
        return mergeAll(first.loadItemSets(), second.loadItemSets());
    }

    @Override
    public Map<Short, ItemTemplate> loadItems() throws Exception {
        return mergeAll(first.loadItems(), second.loadItems());
    }

    @Override
    public Map<Short, SpellTemplate> loadSpells() throws Exception {
        return mergeAll(first.loadSpells(), second.loadSpells());
    }

    @Override
    public Map<Integer, Waypoint> loadWaypoints() throws Exception {
        return mergeAll(first.loadWaypoints(), second.loadWaypoints());
    }

    @Override
    public Map<Integer, NpcTemplate> loadNpcTemplates() throws Exception {
        return mergeAll(first.loadNpcTemplates(), second.loadNpcTemplates());
    }
}
