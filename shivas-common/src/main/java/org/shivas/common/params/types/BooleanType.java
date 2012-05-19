package org.shivas.common.params.types;

import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;

public class BooleanType implements Type {

	public static final Boolean DEFAULT_VALUE = false;

	@Override
	public Class<?> getJavaClass() {
		return Boolean.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(String string) throws ParsingException {
		if (string.equalsIgnoreCase("TRUE") || string.equalsIgnoreCase("1")) {
			return true;
		} else if (string.equalsIgnoreCase("FALSE") || string.equalsIgnoreCase("0")) {
			return false;
		} else {
			throw new ParsingException("can't convert " + string + " to Boolean");
		}
	}

}
