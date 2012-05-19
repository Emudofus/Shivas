package org.shivas.common.params.types;

import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;

public class LongType implements Type {

	public static final Long DEFAULT_VALUE = 0L;

	@Override
	public Class<?> getJavaClass() {
		return Long.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(String string) throws ParsingException {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			throw new ParsingException(e);
		}
	}

}
