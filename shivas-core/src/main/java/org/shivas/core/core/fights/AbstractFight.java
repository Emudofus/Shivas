package org.shivas.core.core.fights;

import com.google.common.collect.Maps;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.shivas.common.threads.Timer;
import org.shivas.core.config.Config;
import org.shivas.core.core.castables.Castable;
import org.shivas.core.core.channels.ChannelEvent;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.ThreadedEventDispatcher;
import org.shivas.core.core.fights.events.*;
import org.shivas.core.core.fights.frames.FighterCastFrame;
import org.shivas.core.core.fights.frames.FighterMovementFrame;
import org.shivas.core.core.fights.frames.Frame;
import org.shivas.core.core.interactions.AbstractInteraction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.paths.Path;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightStateEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.types.BaseFighterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.shivas.common.collections.CollectionQuery.from;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:28
 */
public abstract class AbstractFight extends AbstractInteraction implements Fight {

    private static final Logger log = LoggerFactory.getLogger(AbstractFight.class);

    protected final int id;
    protected final Config config;
    protected final Timer<Fight> timer;
    protected final ExecutorService worker;
    protected final Random random = new Random(System.nanoTime());
    protected final EventDispatcher event;
    protected final Map<FightTeamEnum, FightTeam> teams = Maps.newIdentityHashMap();
    protected final GameMap map;
    protected final List<FightCell> cells;

    protected FightStateEnum state;
    protected FightTurnList turns;
    protected Frame currentFrame;
    protected Instant beginning, end;

    protected AbstractFight(int id, Config config, Timer<Fight> timer, ExecutorService worker, GameMap map, Fighter challenger, Fighter defender) {
        this.id = id;
        this.config = config;
        this.timer = timer;
        this.worker = worker;
        this.event = new ThreadedEventDispatcher(worker);
        this.map = map;
        this.state = FightStateEnum.INIT;
        this.cells = generateCells();

        this.map.add(this);

        this.teams.put(FightTeamEnum.CHALLENGERS, new FightTeam(FightTeamEnum.CHALLENGERS, toSide(FightTeamEnum.CHALLENGERS), this, challenger));
        this.teams.put(FightTeamEnum.DEFENDERS, new FightTeam(FightTeamEnum.DEFENDERS, toSide(FightTeamEnum.DEFENDERS), this, defender));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public ExecutorService getWorker() {
        return worker;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public EventDispatcher getEvent() {
        return event;
    }

    @Override
    public FightTeam getTeam(FightTeamEnum fightTeamEnum) {
        return teams.get(fightTeamEnum);
    }

    @Override
    public FightTeam getChallengers() {
        return getTeam(FightTeamEnum.CHALLENGERS);
    }

    @Override
    public FightTeam getDefenders() {
        return getTeam(FightTeamEnum.DEFENDERS);
    }

    @Override
    public FightTurnList getTurns() {
        return turns;
    }

    @Override
    public GameMap getMap() {
        return map;
    }

    @Override
    public Collection<FightCell> getCells() {
        return cells;
    }

    @Override
    public FightCell getCell(short cellId) {
        return cells.size() <= cellId ? null : cells.get(cellId);
    }

    @Override
    public FightStateEnum getState() {
        return state;
    }

    @Override
    public Frame getCurrentFrame() {
        return currentFrame;
    }

    /**
     * sets the current frame and starts it
     * @param currentFrame frame
     * @throws FightException
     */
    @Override
    public void setCurrentFrame(Frame currentFrame) throws FightException {
        if (state != FightStateEnum.ACTIVE) throw new FightException("this is allowed when the fight is active");

        if (this.currentFrame != null) {
            this.currentFrame.setNext(currentFrame);
        } else {
            this.currentFrame = currentFrame;
            this.currentFrame.begin();
        }
    }

    @Override
    public void eraseCurrentFrame() {
        this.currentFrame = null;
    }

    @Override
    public void schedule(Duration duration, Runnable action) {
        timer.schedule(this, duration, action);
    }

    @Override
    public void purgeScheduledTasks() {
        timer.purge(this);
    }

    /**
     * @return null duration if fight has not started yet
     */
    @Override
    public Duration getDuration() {
        return beginning != null ?
                new Duration(beginning, end) : // does not matter if end is null, it will be converted to Instant.now()
                null;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.FIGHT;
    }

    protected void onStopped() {
        map.remove(this);
    }

    protected void beforeInit() throws InteractionException {}
    @Override
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

        beginning = Instant.now();

        beforeBegin();

        turns = new FightTurnList(this);
        event.publish(new StateUpdateEvent(this, FightStateEnum.ACTIVE));
        timer.start();

        turns.beginNextTurn();
        state = FightStateEnum.ACTIVE;

        afterBegin();
    }
    protected void afterBegin() throws InteractionException {}

    protected void beforeCancel() throws InteractionException {}
    @Override
    public void cancel() throws InteractionException {
        if (state != FightStateEnum.PLACE && state != FightStateEnum.ACTIVE) throw new FightException("you can't cancel this fight");

        beforeCancel();

        event.publish(new StateUpdateEvent(this, FightStateEnum.FINISHED));
        state = FightStateEnum.FINISHED;
        timer.stop();

        afterCancel();
        onStopped();
    }
    protected void afterCancel() throws InteractionException {}

    protected void beforeEnd() throws InteractionException {}
    @Override
    protected void internalEnd() throws InteractionException {
        if (state != FightStateEnum.PLACE && state != FightStateEnum.ACTIVE) throw new FightException("you can't cancel this fight");

        end = Instant.now();

        beforeEnd();

        FightTeam winners, losers;
        if (getChallengers().areAlive()) {
            winners = getChallengers();
            losers = getDefenders();
        } else {
            winners = getDefenders();
            losers = getChallengers();
        }

        event.publish(new FightEndEvent(this, winners, losers));
        state = FightStateEnum.FINISHED;
        timer.stop();

        afterEnd();
        onStopped();
    }
    protected void afterEnd() throws InteractionException {}

    @Override
    public void notifyReady(Fighter fighter) throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can only set ready when the fight's state allows it");

        event.publish(new FighterEvent(FightEventType.FIGHTER_READY, fighter));

        if (getChallengers().areReady() && getDefenders().areReady()) {
            begin();
        }
    }

    @Override
    public void changePlace(Fighter fighter, short targetCellId) throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can only change place when the fight's state allows it");

        if (fighter.isReady()) {
            return;
        }

        FightCell cell = getCell(targetCellId);

        if (!cell.isAvailable()) {
            return;
        }
        if (cell.getStartCellTeam() != fighter.getTeam().getType()) {
            throw new FightException("you can't take place on other side's cell");
        }

        fighter.setCurrentCell(cell);
        event.publish(new FighterEvent(FightEventType.FIGHTER_PLACEMENT, fighter));
    }

    @Override
    public void cast(Fighter caster, Castable castable, short targetCell) throws InteractionException {
        setCurrentFrame(new FighterCastFrame(caster, castable, getCell(targetCell)));
    }

    @Override
    public void move(Fighter fighter, Path path) throws InteractionException {
        setCurrentFrame(new FighterMovementFrame(fighter, path));
    }

    @Override
    public void quit(Fighter fighter) throws InteractionException {
        if (fighter == null) throw new IllegalArgumentException("fighter must not be null");
        if (!canQuit(fighter)) throw new FightException("you can not quit this fight");

        if (fighter.isLeader()) {
            cancel();
        } else {
            FightTeam team = fighter.getTeam();
            if (team.remove(fighter)) {
                event.publish(new FighterQuitEvent(fighter));
            } else {
                throw new FightException(String.format("fighter %d can not be removed from his team", fighter.getId()));
            }
        }
    }

    @Override
    public void speak(Fighter fighter, String message) throws InteractionException {
        event.publish(new ChannelEvent(ChannelEnum.General, fighter, message));
    }

    @Override
    public FightSideEnum toSide(FightTeamEnum team) {
        return team == FightTeamEnum.CHALLENGERS ? FightSideEnum.RED :
               team == FightTeamEnum.DEFENDERS   ? FightSideEnum.BLUE : null;
    }

    @Override
    public FightTeamEnum toTeam(FightSideEnum side) {
        return side == FightSideEnum.RED  ? FightTeamEnum.CHALLENGERS :
               side == FightSideEnum.BLUE ? FightTeamEnum.DEFENDERS : null;
    }

    @Override
    public FightCell firstAvailableStartCell(FightTeamEnum team) {
        for (FightCell cell : cells) {
            if (cell.getStartCellTeam() == team) {
                return cell;
            }
        }
        throw new RuntimeException("there is not anymore available places");
    }

    @Override
    public Fighter find(int fighterId) {
        Fighter fighter = getChallengers().get(fighterId);
        if (fighter == null) {
            fighter = getDefenders().get(fighterId);
        }
        return fighter;
    }

    protected List<FightCell> generateCells() {
        return map.getCells().stream()
                  .map(x -> FightCell.copyFrom(x, toTeam(x.getStartFightSide())))
                  .collect(Collectors.toList());
    }

    @Override
    public void exceptionThrowed(Throwable throwable) {
        log.error("unhandled exception {} : {}", throwable.getClass(), throwable.toString());
    }

    @Override
    public Collection<BaseFighterType> toBaseFighterType() {
        return from(getChallengers())
              .with(getDefenders())
              .transform(Fighter::toBaseFighterType)
              .lazyCollection();
    }

    public Iterator<FightCell> iterator() {
        return cells.iterator();
    }
}
