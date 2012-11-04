package org.shivas.data.entity;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 14:47
 */
public interface CellProvider<C extends GameCell> extends Iterable<C> {
    C getCell(short cellId);
}
