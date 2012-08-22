package org.shivas.common.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

public class CollectionQuery<T> {
	
	public static <T> CollectionQuery<T> from(Iterable<T> from) {
		return new CollectionQuery<T>(from);
	}
	
	private Iterable<T> from;

	protected CollectionQuery(Iterable<T> from) {
		this.from = from;
	}
	
	public CollectionQuery<T> filter(Predicate<T> predicate) {
		return from(Iterables.filter(from, predicate));
	}

	public <E> CollectionQuery<E> transform(Function<T, E> function) {
		return from(Iterables.transform(from, function));
	}
	
	public CollectionQuery<T> skip(int index) {
		return from(Iterables.skip(from, index));
	}
	
	public CollectionQuery<T> limit(int nb) {
		return from(Iterables.limit(from, nb));
	}
	
	public CollectionQuery<T> with(Iterable<T> values) {
		return from(Iterables.concat(from, values));
	}
	
	public CollectionQuery<T> with(T... values) {
		final Iterator<T> it = Iterators.forArray(values);
		return from(new FluentIterable<T>() {
			public Iterator<T> iterator() {
				return Iterators.concat(from.iterator(), it);
			}
		});
	}
	
	public CollectionQuery<T> orderBy(Comparator<T> comparator) {
		return from(Ordering.from(comparator).sortedCopy(from));
	}
	
	public T first() {
		Iterator<T> it = from.iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	public T single() {
		Iterator<T> it = from.iterator();
		if (!it.hasNext()) return null;
		
		T single = it.next();
		if (it.hasNext()) return null;
		
		return single;
	}
	
	public int count() {
		return Iterables.size(from);
	}
	
	public int count(Predicate<T> predicate) {
		return filter(predicate).count();
	}
	
	public boolean all(Predicate<T> predicate) {
		return Iterables.all(from, predicate);
	}
	
	public boolean any(Predicate<T> predicate) {
		return Iterables.any(from, predicate);
	}
	
	public Iterable<T> end() {
		return from;
	}
	
	public List<T> computeList() {
		List<T> result = new ArrayList<T>();
		for (T obj : from) {
			result.add(obj);
		}
		return result;
	}
	
	public Set<T> computeSet() {
		Set<T> result = new HashSet<T>();
		for (T obj : from) {
			result.add(obj);
		}
		return result;
	}
	
	public <K> Map<K, T> computeMap(Function<T, K> function) {
		return Maps.uniqueIndex(from, function);
	}

}
