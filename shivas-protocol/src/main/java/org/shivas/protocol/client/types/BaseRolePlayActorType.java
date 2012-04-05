package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 17/02/12
 * Time: 16:15
 */
public abstract class BaseRolePlayActorType {
    private long id;

    private short currentCellId;
    private OrientationEnum currentOrientation;

    protected BaseRolePlayActorType() {
    }

    protected BaseRolePlayActorType(long id, short currentCellId, OrientationEnum currentOrientation) {
        this.id = id;
        this.currentCellId = currentCellId;
        this.currentOrientation = currentOrientation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getCurrentCellId() {
        return currentCellId;
    }

    public void setCurrentCellId(short currentCellId) {
        this.currentCellId = currentCellId;
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }
}
