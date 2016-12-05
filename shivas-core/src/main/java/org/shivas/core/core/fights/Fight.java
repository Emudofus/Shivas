package org.shivas.core.core.fights;

import org.joda.time.Duration;
import org.shivas.core.config.Config;
import org.shivas.core.core.castables.Castable;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.fights.frames.Frame;
import org.shivas.core.core.interactions.Interaction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.paths.Path;
import org.shivas.data.entity.CellProvider;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightStateEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.protocol.client.types.BaseFighterType;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public interface Fight extends Interaction, CellProvider<FightCell> {
    int getId();

    Config getConfig();

    ExecutorService getWorker();

    Random getRandom();

    EventDispatcher getEvent();

    FightTeam getTeam(FightTeamEnum fightTeamEnum);

    FightTeam getChallengers();

    FightTeam getDefenders();

    FightTurnList getTurns();

    GameMap getMap();

    Collection<FightCell> getCells();

    FightCell getCell(short cellId);

    FightStateEnum getState();

    Frame getCurrentFrame();

    void setCurrentFrame(Frame currentFrame) throws FightException;

    void eraseCurrentFrame();

    void schedule(Duration duration, Runnable action);

    void purgeScheduledTasks();

    Duration getDuration();

    @Override
    InteractionType getInteractionType();

    FightTypeEnum getFightType();

    int getRemainingPreparation();

    boolean canQuit(Fighter fighter);

    void init() throws InteractionException;

    @Override
    void begin() throws InteractionException;

    @Override
    void cancel() throws InteractionException;

    void notifyReady(Fighter fighter) throws InteractionException;

    void changePlace(Fighter fighter, short targetCellId) throws InteractionException;

    void cast(Fighter caster, Castable castable, short targetCell) throws InteractionException;

    void move(Fighter fighter, Path path) throws InteractionException;

    void quit(Fighter fighter) throws InteractionException;

    void speak(Fighter fighter, String message) throws InteractionException;

    FightSideEnum toSide(FightTeamEnum team);

    FightTeamEnum toTeam(FightSideEnum side);

    FightCell firstAvailableStartCell(FightTeamEnum team);

    Fighter find(int fighterId);

    void exceptionThrowed(Throwable throwable);

    Collection<BaseFighterType> toBaseFighterType();
}
