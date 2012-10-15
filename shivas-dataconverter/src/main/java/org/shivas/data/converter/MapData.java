package org.shivas.data.converter;

import org.shivas.data.entity.MapTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 11/10/12
 * Time: 08:43
 */
public class MapData extends MapTemplate {
    private String data;
    private List<List<Short>> startCells;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<List<Short>> getStartCells() {
        return startCells;
    }

    public void setStartCells(List<List<Short>> startCells) {
        this.startCells = startCells;
    }
}
