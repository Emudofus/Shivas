package org.shivas.common.collections;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/10/12
 * Time: 18:50
 */
public class SortedArrayList<E extends Comparable<E>> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        int left = 0, right = size() - 1, center;

        while (left <= right) {
            center = (left + right) / 2;
            E obj = get(center);

            int diff = e.compareTo(obj);
            if (diff > 0) {
                left = center + 1;
            } else if (diff < 0) {
                right = center - 1;
            } else {
                left = center;
                break;
            }
        }

        add(left, e);
        return true;
    }
}
