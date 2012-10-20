package org.shivas.server.core.fights;

import com.google.common.collect.Maps;
import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightStateEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.protocol.client.types.BaseFighterType;
import org.shivas.server.config.Config;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.ThreadedEventDispatcher;
import org.shivas.server.core.fights.events.FightEventType;
import org.shivas.server.core.fights.events.FightInitializationEvent;
import org.shivas.server.core.fights.events.FighterEvent;
import org.shivas.server.core.fights.events.StateUpdateEvent;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.utils.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.shivas.common.collections.CollectionQuery.from;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:28
 */
public abstract class Fight extends AbstractInteraction {

    private static final Logger log = LoggerFactory.getLogger(Fight.class);

    protected final Config config;
    protected final ExecutorService worker = Executors.newSingleThreadExecutor();
    protected final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    protected final EventDispatcher event = new ThreadedEventDispatcher(worker);
    protected final Map<FightTeamEnum, FightTeam> teams = Maps.newIdentityHashMap();
    protected final GameMap map;
    protected final FightCell[] cells;

    protected FightStateEnum state;
    protected FightTurnList turns;

    protected Fight(Config config, GameMap map) {
        this.config = config;
        this.map = map;
        this.state = FightStateEnum.INIT;
        this.cells = generateCells();
    }

    public Config getConfig() {
        return config;
    }

    public ExecutorService getWorker() {
        return worker;
    }

    public ScheduledExecutorService getTimer() {
        return timer;
    }

    public EventDispatcher getEvent() {
        return event;
    }

    public FightTeam getTeam(FightTeamEnum fightTeamEnum) {
        return teams.get(fightTeamEnum);
    }

    public FightTeam getChallengers() {
        return getTeam(FightTeamEnum.CHALLENGERS);
    }

    public FightTeam getDefenders() {
        return getTeam(FightTeamEnum.DEFENDERS);
    }

    public FightTurnList getTurns() {
        return turns;
    }

    public GameMap getMap() {
        return map;
    }

    public FightStateEnum getState() {
        return state;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.FIGHT;
    }

    public abstract FightTypeEnum getFightType();
    public abstract int getRemainingPreparation();
    public abstract boolean canCancel();

    protected void beforeInit() throws InteractionException {}
    public void init() throws InteractionException {
        if (state != FightStateEnum.INIT) throw new FightException("you can't init a fight already initialized");

        beforeInit();

        state = FightStateEnum.PLACE;

        event.publish(new FightInitializationEvent(this));

        afterInit();
    }
    protected void afterInit() throws InteractionException {}

    protected void beforeBegin() throws InteractionException {}
    @Override
    public void begin() throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can't begin a fight already begun");

        beforeBegin();

        turns = new FightTurnList(this);
        event.publish(new StateUpdateEvent(this, FightStateEnum.ACTIVE));

        turns.getCurrent().begin();
        state = FightStateEnum.ACTIVE;

        afterBegin();
    }
    protected void afterBegin() throws InteractionException {}

    protected void beforeCancel() throws InteractionException {}
    @Override
    public void cancel() throws InteractionException {
        if (state != FightStateEnum.PLACE && state != FightStateEnum.ACTIVE) throw new FightException("you can't cancel this fight");

        beforeCancel();

        state = FightStateEnum.FINISHED;

        afterCancel();
    }
    protected void afterCancel() throws InteractionException {}

    protected void beforeEnd() throws InteractionException {}
    @Override
    protected void internalEnd() throws InteractionException {
        if (state != FightStateEnum.PLACE && state != FightStateEnum.ACTIVE) throw new FightException("you can't cancel this fight");

        beforeEnd();

        state = FightStateEnum.FINISHED;

        afterEnd();
    }
    protected void afterEnd() throws InteractionException {}

    public void notifyReady(Fighter fighter) throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can only set ready when the fight's state allows it");

        event.publish(new FighterEvent(FightEventType.FIGHTER_READY, fighter));

        if (getChallengers().areReady() && getDefenders().areReady()) {
            begin();
        }
    }

    public void changePlace(Fighter fighter, short targetCellId) throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can only change place when the fight's state allows it");

        if (fighter.isReady()) {
            return;
        }

        FightCell cell = cells[targetCellId];

        if (!cell.isAvailable()) {
            return;
        }
        if (cell.getStartCellTeam() != fighter.getTeam().getType()) {
            throw new FightException("you can't take place on other side's cell");
        }

        fighter.setCurrentCell(cell);
        event.publish(new FighterEvent(FightEventType.FIGHTER_PLACEMENT, fighter));
    }

    public void cast(Fighter caster, Castable castable, FightCell targetCell) throws InteractionException {
        if (state != FightStateEnum.ACTIVE) throw new FightException("you can't cast now");
    }

    public void move(Fighter fighter, FightCell targetCell) throws InteractionException {
        if (state != FightStateEnum.ACTIVE) throw new FightException("you can't move now");
    }

    public FightSideEnum toSide(FightTeamEnum team) {
        return team == FightTeamEnum.CHALLENGERS ? FightSideEnum.RED :
               team == FightTeamEnum.DEFENDERS   ? FightSideEnum.BLUE : null;
    }

    public FightTeamEnum toTeam(FightSideEnum side) {
        return side == FightSideEnum.RED  ? FightTeamEnum.CHALLENGERS :
               side == FightSideEnum.BLUE ? FightTeamEnum.DEFENDERS : null;
    }

    public FightCell firstAvailableStartCell(FightTeamEnum team) {
        for (FightCell cell : cells) {
            if (cell.getStartCellTeam() == team) {
                return cell;
            }
        }
        throw new RuntimeException("there is not anymore available places");
    }

    protected FightCell[] generateCells() {
        FightCell[] cells = new FightCell[map.getCells().length()];

        for (GameCell cell : map.getCells()) {
            cells[cell.getId()] = new FightCell(cell, toTeam(cell.getStartFightSide()));
        }

        return cells;
    }

    public void exceptionThrowed(Throwable throwable) {
        log.error("unhandled exception {} : {}", throwable.getClass(), throwable.toString());
    }

    public Collection<BaseFighterType> toBaseFighterType() {
        return from(getChallengers())
              .with(getDefenders())
              .transform(Converters.FIGHTER_TO_BASEFIGHTERTYPE)
              .lazyCollection();
    }
}
