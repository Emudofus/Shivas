package org.shivas.common.params.types;

import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;

public class IntegerType implements Type {

	public static final Integer DEFAULT_VALUE = 0;

	@Override
	public Class<?> getJavaClass() {
		return Integer.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(String string) throws ParsingException {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new ParsingException(e);
		}
	}

}
