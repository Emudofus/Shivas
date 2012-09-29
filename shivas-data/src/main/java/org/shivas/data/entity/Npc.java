package org.shivas.data.entity;

import org.shivas.protocol.client.enums.OrientationEnum;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/09/12
 * Time: 18:25
 */
public class Npc implements Serializable {
    private static final long serialVersionUID = -3403427613062336090L;

    private int id;
    private NpcTemplate template;
    private MapTemplate map;
    private short cell;
    private OrientationEnum orientation;
    private Action action;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NpcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NpcTemplate template) {
        this.template = template;
    }

    public MapTemplate getMap() {
        return map;
    }

    public void setMap(MapTemplate map) {
        this.map = map;
    }

    public short getCell() {
        return cell;
    }

    public void setCell(short cell) {
        this.cell = cell;
    }

    public OrientationEnum getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationEnum orientation) {
        this.orientation = orientation;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
