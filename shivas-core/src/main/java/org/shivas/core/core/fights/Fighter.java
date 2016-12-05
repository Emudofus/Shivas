package org.shivas.core.core.fights;

import org.shivas.common.statistics.Statistics;
import org.shivas.core.core.GameActor;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.types.BaseEndFighterType;
import org.shivas.protocol.client.types.BaseFighterType;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public interface Fighter extends GameActor {
    Integer getId();

    boolean isReady();

    Statistics getStats();

    short getLevel();

    Fight getFight();

    void setFight(Fight fight);

    FightTurn getTurn();

    void setTurn(FightTurn turn);

    FightTeam getTeam();

    void setTeam(FightTeam team);

    boolean isLeader();

    boolean canQuit();

    FightCell getCurrentCell();

    void setCurrentCell(FightCell currentCell);

    void takePlaceOf(Fighter other);

    OrientationEnum getCurrentOrientation();

    void setCurrentOrientation(OrientationEnum currentOrientation);

    boolean isAlive();

    BaseFighterType toBaseFighterType();

    BaseEndFighterType toBaseEndFighterType();
}
