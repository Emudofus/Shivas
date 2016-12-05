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
    private static final long serialVersionUID = -834203863899206216L;

    private String data;
    private List<String> startCells;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getStartCells() {
        return startCells;
    }

    public void setStartCells(List<String> startCells) {
        this.startCells = startCells;
    }
}
