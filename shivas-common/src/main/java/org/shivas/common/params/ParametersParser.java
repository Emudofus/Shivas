package org.shivas.common.params;

public interface ParametersParser {

	Parameters parse(String string, Conditions conditions) throws ParsingException;
	
}
