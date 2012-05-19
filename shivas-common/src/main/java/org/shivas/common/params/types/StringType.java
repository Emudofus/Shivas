package org.shivas.common.params.types;

import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;

public class StringType implements Type {

	public static final String DEFAULT_VALUE = "";

	@Override
	public Class<?> getJavaClass() {
		return String.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(String string) throws ParsingException {
		String[] args = string.split("\"");
		if (args.length < 2) {
			throw new ParsingException("a String must be surrounded by quote");
		}
		return args[1];
	}

}
