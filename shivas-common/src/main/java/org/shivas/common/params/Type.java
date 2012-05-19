package org.shivas.common.params;

public interface Type {
	
	Class<?> getJavaClass();
	Object getDefaultValue();
	
	Object parse(String string) throws ParsingException;
	
}
