package org.shivas.data.entity;

public class Waypoint {
	private int id;
	private MapTemplate map;
	private short cell;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
