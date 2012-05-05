package org.shivas.data.entity;

import java.io.Serializable;

public class GameCell implements Serializable {
	
    public static enum MovementType {
        Unwalkable,
        Door,
        Trigger,
        Walkable,
        Paddock,
        Road;

        public static MovementType valueOf(int ordinal){
        	for (MovementType value : values()) {
        		if (value.ordinal() == ordinal) return value;
        	}
        	return null;
        }
    }

	private static final long serialVersionUID = -1319951599312238520L;

    private short id;
    private boolean lineOfSight;
    private MovementType movementType;
    private int groundLevel;
    private int groundSlope;
    
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public boolean isLineOfSight() {
		return lineOfSight;
	}
	public void setLineOfSight(boolean lineOfSight) {
		this.lineOfSight = lineOfSight;
	}
	public MovementType getMovementType() {
		return movementType;
	}
	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}
	public int getGroundLevel() {
		return groundLevel;
	}
	public void setGroundLevel(int groundLevel) {
		this.groundLevel = groundLevel;
	}
	public int getGroundSlope() {
		return groundSlope;
	}
	public void setGroundSlope(int groundSlope) {
		this.groundSlope = groundSlope;
	}

}
