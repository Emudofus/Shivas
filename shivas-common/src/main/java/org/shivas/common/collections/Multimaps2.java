package org.shivas.common.collections;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class Multimaps2 {
	private Multimaps2() {}
	
	public static <K, V> Multimap<K, V> group(Collection<? extends V> collection, Function<V, K> function) {
		Multimap<K, V> multimap = ArrayListMultimap.create();
		
		for (V value : collection) {
			K key = function.apply(value);
			multimap.put(key, value);
		}
		
		return multimap;
	}
}
