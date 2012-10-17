package org.shivas.data.entity;

import com.google.common.collect.Iterators;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 17/10/12
 * Time: 18:32
 */
public class MapCells implements Iterable<GameCell> {
    private final MapTemplate map;

    private GameCell[] cells;
    private String[] encodedStartCells = new String[2];

    public MapCells(MapTemplate map) {
        this.map = map;
    }

    public MapTemplate getMap() {
        return map;
    }

    public GameCell[] get() {
        return cells;
    }

    public GameCell get(short id) {
        return cells.length <= id ? null : cells[id];
    }

    public void set(GameCell[] cells) {
        this.cells = cells;
    }

    public String getEncodedStartCells(int side) {
        return encodedStartCells.length < side ? null : encodedStartCells[side - 1];
    }

    public void setEncodedStartCells(int side, String encodedStartCells) {
        this.encodedStartCells[side - 1] = encodedStartCells;
    }

    @Override
    public Iterator<GameCell> iterator() {
        return Iterators.forArray(cells);
    }
}
