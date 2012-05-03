package org.shivas.server.core;

import java.io.Serializable;

import org.shivas.data.entity.GameMap;
import org.shivas.protocol.client.enums.OrientationEnum;

public class Location implements Serializable {

	private static final long serialVersionUID = -4975380217739829252L;
	
	private GameMap map;
	private short cell;
	private OrientationEnum orientation;

	public Location(GameMap map, short cell, OrientationEnum orientation) {
		this.map = map;
		this.cell = cell;
		this.orientation = orientation;
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

	public OrientationEnum getOrientation() {
		return orientation;
	}

	public void setOrientation(OrientationEnum orientation) {
		this.orientation = orientation;
	}

}
