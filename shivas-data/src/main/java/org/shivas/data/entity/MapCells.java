package org.shivas.data.entity;

import com.google.common.collect.Iterators;
import org.shivas.protocol.client.enums.FightSideEnum;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 17/10/12
 * Time: 18:32
 */
public class MapCells implements CellProvider<GameCell> {
    private final MapTemplate map;

    private GameCell[] cells;
    private String[] encodedStartCells = new String[2];

    public MapCells(MapTemplate map) {
        this.map = map;
    }

    public MapTemplate getMap() {
        return map;
    }

    public int length() {
        return cells.length;
    }

    public GameCell[] get() {
        return cells;
    }

    public GameCell get(short id) {
        return cells.length <= id ? null : cells[id];
    }

    public GameCell getCell(short id) {
        return get(id);
    }

    public void set(GameCell[] cells) {
        this.cells = cells;
    }

    public String getEncodedStartCells(FightSideEnum side) {
        return side == FightSideEnum.BLUE ? encodedStartCells[0] : encodedStartCells[1];
    }

    public void setEncodedStartCells(FightSideEnum side, String encodedStartCells) {
        this.encodedStartCells[side == FightSideEnum.BLUE ? 0 : 1] = encodedStartCells;
    }

    public Stream<GameCell> stream() {
        return StreamSupport.stream(Spliterators.spliterator(cells, 0), false);
    }

    @Override
    public Iterator<GameCell> iterator() {
        return Iterators.forArray(cells);
    }
}
