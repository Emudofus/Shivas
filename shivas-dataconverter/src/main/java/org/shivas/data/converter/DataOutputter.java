package org.shivas.data.converter;

import java.io.IOException;
import java.util.Collection;

public interface DataOutputter {

	void outputBreed(Structs.Breed breed, String fileName) throws IOException;
	void outputExperiences(Collection<Structs.Experience> exps, String fileName) throws IOException;
	void outputMaps(Collection<Structs.GameMap> map, String fileName) throws IOException;
	
}