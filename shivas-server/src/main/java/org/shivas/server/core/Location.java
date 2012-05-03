package org.shivas.server.core;

import java.io.Serializable;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.core.maps.GMap;

public class Location implements Serializable {

	private static final long serialVersionUID = -4975380217739829252L;
	
	private GMap map;
	private short cell;
	private OrientationEnum orientation;

	public Location(GMap map, short cell, OrientationEnum orientation) {
		this.map = map;
		this.cell = cell;
		this.orientation = orientation;
	}

	public GMap getMap() {
		return map;
	}

	public void setMap(GMap map) {
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
