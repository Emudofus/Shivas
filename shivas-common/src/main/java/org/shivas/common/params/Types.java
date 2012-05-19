package org.shivas.common.params;

import org.shivas.common.params.types.BooleanType;
import org.shivas.common.params.types.IntegerType;
import org.shivas.common.params.types.LongType;
import org.shivas.common.params.types.StringType;

public final class Types {
	private Types() {}
	
	public static final Type INTEGER = new IntegerType();
	public static final Type LONG = new LongType();
	public static final Type STRING = new StringType();
	public static final Type BOOLEAN = new BooleanType();
}
