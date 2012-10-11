package org.shivas.data.converter;

import org.shivas.data.entity.*;

import java.io.IOException;
import java.util.Collection;

public interface DataOutputter {

	void outputBreed(Breed breed, String fileName) throws IOException;
	void outputBreeds(Collection<Breed> breed, String directory) throws IOException;
	void outputExperiences(Collection<Experience> exps, String fileName) throws IOException;
	void outputMaps(Collection<MapData> map, String fileName) throws IOException;
	void outputItemSets(Collection<ItemSet> itemsets, String fileName) throws IOException;
	void outputItems(Collection<ItemTemplate> items, String fileName) throws IOException;
	void outputSpells(Collection<SpellTemplate> spells, String fileName) throws IOException;
	void outputWaypoints(Collection<Waypoint> waypoints, String fileName) throws IOException;
    void outputNpcTemplates(Collection<NpcTemplate> npcTemplates, String fileName) throws IOException;
}
