package org.shivas.common.collections;

import java.util.Map;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class Maps2 {
	private Maps2() {}
	
	public static <K, V> Map<K, V> toMap(Iterable<V> values, Function<V, K> transformer) {
		Map<K, V> map = Maps.newHashMap();
		for (V value : values) {
			K key = transformer.apply(value);
			map.put(key, value);
		}
		return map;
	}
	
	public static <K, V> V randomValue(Map<K, V> map) {
		Random rand = new Random(System.nanoTime());
		
		Object[] keys = map.keySet().toArray();
		Object key = keys[rand.nextInt(keys.length)];
		
		return map.get(key);
	}
}
