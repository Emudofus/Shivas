package org.shivas.data.entity;

import java.io.Serializable;

public class MapTrigger implements Serializable {

	private static final long serialVersionUID = -3959363931599403565L;

	private MapTemplate map;
	private short cell;
	private MapTemplate nextMap;
	private short nextCell;

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
	public MapTemplate getNextMap() {
		return nextMap;
	}
	public void setNextMap(MapTemplate nextMap) {
		this.nextMap = nextMap;
	}
	public short getNextCell() {
		return nextCell;
	}
	public void setNextCell(short nextCell) {
		this.nextCell = nextCell;
	}
}
