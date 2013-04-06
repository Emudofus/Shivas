package org.shivas.common.collections;

import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * TODO
 * only values added with {@link org.shivas.common.collections.SortedList#add(Object)} are sorted
 *
 * @author Blackrush
 *         Mambo
 */
public class SortedList<T> extends ArrayList<T> {
    public static <T> SortedList<T> create(Comparator<T> comparator) {
        return new SortedList<T>(comparator);
    }

    public static <T extends Comparable<T>> SortedList<T> create() {
        return create(Ordering.<T>natural());
    }

    private final Comparator<T> comparator;

    SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean add(T t) {
        int left = 0, right = size() - 1, center;

        while (left <= right) {
            center = (left + right) / 2;
            T obj = get(center);

            int diff = comparator.compare(t, obj);
            if (diff > 0) {
                left = center + 1;
            } else if (diff < 0) {
                right = center - 1;
            } else {
                left = center;
                break;
            }
        }

        add(left, t);
        return true;
    }
}
