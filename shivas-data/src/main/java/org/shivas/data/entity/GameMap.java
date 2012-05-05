package org.shivas.data.entity;

import java.io.Serializable;
import java.util.List;

import org.shivas.common.maths.Point;

public class GameMap implements Serializable {

	private static final long serialVersionUID = 924299994836075322L;
	
	private int id;
	private Point position;
	private int width, height;
	private List<GameCell> cells;
	private String date;
	private String key;
	private boolean subscriber;
	private List<MapTrigger> trigger;
	
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
	public List<GameCell> getCells() {
		return cells;
	}
	public void setCells(List<GameCell> cells) {
		this.cells = cells;
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
	public List<MapTrigger> getTrigger() {
		return trigger;
	}
	public void setTrigger(List<MapTrigger> trigger) {
		this.trigger = trigger;
	}

}
