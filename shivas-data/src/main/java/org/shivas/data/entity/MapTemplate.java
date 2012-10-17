package org.shivas.data.entity;

import org.shivas.common.maths.Point;

import java.io.Serializable;
import java.util.Map;

public class MapTemplate implements Serializable {

	private static final long serialVersionUID = 924299994836075322L;
	
	private int id;
	private Point position;
	private int width, height;
	private MapCells cells = new MapCells(this);
	private String date;
	private String key;
	private boolean subscriber;
	private Map<Short, MapTrigger> trigger;
	private Waypoint waypoint;
    private boolean canFight;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public MapCells getCells() {
		return cells;
	}
    public GameCell getCell(short cellId) {
        return cells.get(cellId);
    }
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isSubscriber() {
		return subscriber;
	}
	public void setSubscriber(boolean subscriber) {
		this.subscriber = subscriber;
	}
	public Map<Short, MapTrigger> getTrigger() {
		return trigger;
	}
	public void setTrigger(Map<Short, MapTrigger> trigger) {
		this.trigger = trigger;
	}
	public Waypoint getWaypoint() {
		return waypoint;
	}
	public void setWaypoint(Waypoint waypoint) {
		this.waypoint = waypoint;
	}
    public boolean canFight() {
        return canFight;
    }
    public void setCanFight(boolean canFight) {
        this.canFight = canFight;
    }
}
