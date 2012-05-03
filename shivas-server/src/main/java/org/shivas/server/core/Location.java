package org.shivas.server.core;

import java.io.Serializable;

import org.shivas.data.entity.GameMap;

public class Location implements Serializable {

	private static final long serialVersionUID = -4975380217739829252L;
	
	private GameMap map;
	private short cell;
	
	public Location(GameMap map, short cell) {
		this.map = map;
		this.cell = cell;
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

}
