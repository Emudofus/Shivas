package org.shivas.data.entity;

import java.io.Serializable;

public class MapTrigger implements Serializable {

	private static final long serialVersionUID = -3959363931599403565L;

	private int id;
	private GameMap map;
	private short cell;
	private GameMap nextMap;
	private short nextCell;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public GameMap getMap() {
		return map;
	}
	public void setMap(GameMap map) {
		this.map = map;
	}
	public short getCell() {
		return cell;
	}
	public void setCell(short cell) {
		this.cell = cell;
	}
	public GameMap getNextMap() {
		return nextMap;
	}
	public void setNextMap(GameMap nextMap) {
		this.nextMap = nextMap;
	}
	public short getNextCell() {
		return nextCell;
	}
	public void setNextCell(short nextCell) {
		this.nextCell = nextCell;
	}
}
