package org.shivas.protocol.client.types;

public class BaseWaypointType {
	private int mapId;
	private long cost;
	
	public BaseWaypointType() {
	}

	public BaseWaypointType(int mapId, long cost) {
		this.mapId = mapId;
		this.cost = cost;
	}

	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the cost
	 */
	public long getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(long cost) {
		this.cost = cost;
	}
}
