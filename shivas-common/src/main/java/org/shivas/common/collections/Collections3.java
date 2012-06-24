package org.shivas.common.collections;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public final class Collections3 {
	private Collections3(){}
	
	public static <T, U> Collection<U> transform(Collection<T> input, Function<T, U> transformer) {
		List<U> output = Lists.newArrayListWithCapacity(input.size());
		for (T in : input) {
			U out = transformer.apply(in);
			output.add(out);
		}
		return output;
	}
	
	public static <T> Collection<T> fromString(String string, String separator, Function<String, T> transformer) {
		String[] input = string.split(separator);
		List<T> output = Lists.newArrayListWithCapacity(input.length);
		for (String in : input) {
			T out = transformer.apply(in);
			output.add(out);
		}
		return output;
	}
	
	public static <T> String toString(Collection<T> input, String separator, Function<T, String> transformer) {
		StringBuilder output = new StringBuilder();
		boolean first = true;
		for (T in : input) {
			if (first) first = false;
			else output.append(separator);
			
			String out = transformer.apply(in);
			output.append(out);
		}
		return output.toString();
	}
}
