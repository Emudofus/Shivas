package org.shivas.data.converter;

import org.shivas.data.entity.*;

import java.util.Collection;

public interface DataOutputter {

	void outputBreeds(Collection<Breed> breed) throws Exception;
	void outputExperiences(Collection<Experience> exps) throws Exception;
	void outputMaps(Collection<MapData> map) throws Exception;
	void outputItemSets(Collection<ItemSet> itemsets) throws Exception;
	void outputItems(Collection<ItemTemplate> items) throws Exception;
	void outputSpells(Collection<SpellTemplate> spells) throws Exception;
	void outputWaypoints(Collection<Waypoint> waypoints) throws Exception;
    void outputNpcTemplates(Collection<NpcTemplate> npcTemplates) throws Exception;
}
