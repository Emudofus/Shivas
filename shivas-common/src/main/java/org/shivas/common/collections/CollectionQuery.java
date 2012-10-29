package org.shivas.common.collections;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

import javax.annotation.Nullable;
import java.util.*;

public class CollectionQuery<T> implements Iterable<T> {
	
	public static <T> CollectionQuery<T> from(Iterable<T> from) {
		return new CollectionQuery<T>(from);
	}

    public static <T> CollectionQuery<T> from(T... obj) {
        return new CollectionQuery<T>(Lists.newArrayList(obj));
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
		return from(new Iterable<T>() {
			public Iterator<T> iterator() {
				return Iterators.concat(from.iterator(), it);
			}
		});
	}
	
	public CollectionQuery<T> orderBy(Comparator<T> comparator) {
		return from(Ordering.from(comparator).sortedCopy(from));
	}

    public CollectionQuery<T> each(Function<T, Void> function) {
        for (T obj : from) {
            function.apply(obj);
        }

        return this;
    }

    public <E> CollectionQuery<E> ofType(final Class<E> clazz) {
        return filter(new Predicate<T>() {
            public boolean apply(@Nullable T input) {
                return input != null && clazz.isInstance(input);
            }
        }).transform(new Function<T, E>() {
            public E apply(@Nullable T input) {
                return clazz.cast(input);
            }
        });
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

    public Collection<T> lazyCollection() {
        return new AbstractCollection<T>() {
            public Iterator<T> iterator() {
                return from.iterator();
            }

            public int size() {
                return count();
            }
        };
    }

    public <C extends Collection<T>> C addTo(C collection) {
        if (collection == null) return null;

        for (T obj : from) {
            collection.add(obj);
        }
        return collection;
    }
	
	public List<T> computeList() {
        return addTo(new ArrayList<T>());
	}
	
	public Set<T> computeSet() {
        return addTo(new HashSet<T>());
	}
	
	public <K> Map<K, T> computeMap(Function<T, K> function) {
		return Maps2.toMap(from, function);
	}

    public T[] computeArray(Class<T> clazz) {
        return Iterables.toArray(from, clazz);
    }

    @Override
    public Iterator<T> iterator() {
        return from.iterator();
    }
}
