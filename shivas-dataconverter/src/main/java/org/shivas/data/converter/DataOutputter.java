package org.shivas.data.converter;

import java.io.IOException;
import java.util.Collection;

public interface DataOutputter {

	void outputBreed(Structs.Breed breed, String fileName) throws IOException;
	void outputBreeds(Collection<Structs.Breed> breed, String directory) throws IOException;
	void outputExperiences(Collection<Structs.Experience> exps, String fileName) throws IOException;
	void outputMaps(Collection<Structs.GameMap> map, String fileName) throws IOException;
	void outputItemSets(Collection<Structs.ItemSet> itemsets, String fileName) throws IOException;
	void outputItems(Collection<Structs.ItemTemplate> items, String fileName) throws IOException;
	void outputSpells(Collection<Structs.SpellTemplate> spells, String fileName) throws IOException;
	void outputWaypoints(Collection<Structs.Waypoint> waypoints, String fileName) throws IOException;
    void outputNpcTemplates(Collection<Structs.NpcTemplate> npcTemplates, String fileName) throws IOException;
}
