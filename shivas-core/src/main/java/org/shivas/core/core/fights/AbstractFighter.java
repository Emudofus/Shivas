package org.shivas.core.core.fights;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.core.core.Location;
import org.shivas.core.core.maps.GameMap;
import org.shivas.protocol.client.enums.OrientationEnum;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public abstract class AbstractFighter implements Fighter {
    public static Comparator<Fighter> compareBy(CharacteristicType charac) {
        return Comparator.comparingInt(o -> o.getStats().get(charac).total());
    }

    protected Fight fight;
    protected FightTurn turn;

    protected FightTeam team;
    protected FightCell currentCell;
    protected OrientationEnum currentOrientation;

    @Override
    public Fight getFight() {
        return fight;
    }

    @Override
    public void setFight(Fight fight) {
        this.fight = fight;
    }

    @Override
    public FightTurn getTurn() {
        return turn;
    }

    @Override
    public void setTurn(FightTurn turn) {
        this.turn = turn;
    }

    @Override
    public FightTeam getTeam() {
        return team;
    }

    @Override
    public void setTeam(FightTeam team) {
        this.team = team;
    }

    @Override
    public boolean isLeader() {
        return team.getLeader() == this;
    }

    @Override
    public boolean canQuit() {
        return fight.canQuit(this);
    }

    @Override
    public FightCell getCurrentCell() {
        return currentCell;
    }

    @Override
    public void setCurrentCell(FightCell currentCell) {
        if (this.currentCell != null) {
            this.currentCell.setCurrentFighter(null);
        }
        this.currentCell = currentCell;
        currentCell.setCurrentFighter(this);
    }

    @Override
    public void takePlaceOf(Fighter other) {
        if (other == null) throw new IllegalArgumentException("other mustn't be null");

        FightCell oldCell = this.getCurrentCell(),
                  newCell = other.getCurrentCell();

        this.setCurrentCell(newCell);
        newCell.setCurrentFighter(this);

        other.setCurrentCell(oldCell);
        oldCell.setCurrentFighter(other);
    }

    @Override
    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    @Override
    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    @Override
    public boolean isAlive() {
        return turn == null ? getStats().life().current() > 0 : !turn.isLeft() && getStats().life().current() > 0;
    }

    @Override
    public int getPublicId() {
        return getId();
    }

    @Override
    public Location getLocation() {
        return new Location(
                fight.getMap(),
                currentCell.getId(),
                currentOrientation
        );
    }

    @Override
    public final void teleport(GameMap map, short cell) {
        throw new UnsupportedOperationException();
    }

}
