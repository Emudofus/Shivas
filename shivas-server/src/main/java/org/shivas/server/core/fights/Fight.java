package org.shivas.server.core.fights;

import org.shivas.protocol.client.enums.FightStateEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.ThreadedEventDispatcher;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.maps.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:28
 */
public abstract class Fight extends AbstractInteraction {

    private static final Logger log = LoggerFactory.getLogger(Fight.class);

    protected final ExecutorService worker = Executors.newSingleThreadExecutor();
    protected final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    protected final EventDispatcher event = new ThreadedEventDispatcher(worker);
    protected final FightTurnList turns;
    protected final GameMap map;

    protected FightStateEnum state;

    protected Fight(Config config, GameMap map) {
        this.turns = new FightTurnList(this, config.turnDuration(getFightType()));
        this.map = map;
    }

    public ExecutorService getWorker() {
        return worker;
    }

    public ScheduledExecutorService getTimer() {
        return timer;
    }

    public FightStateEnum getState() {
        return state;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.FIGHT;
    }

    public abstract FightTypeEnum getFightType();

    protected void beforeInit() throws InteractionException {}
    public void init() throws InteractionException {
        if (state != FightStateEnum.INIT) throw new FightException("you can't init a fight already initialized");

        beforeInit();

        state = FightStateEnum.PLACE;

        afterInit();
    }
    protected void afterInit() throws InteractionException {}

    protected void beforeBegin() throws InteractionException {}
    @Override
    public void begin() throws InteractionException {
        if (state != FightStateEnum.PLACE) throw new FightException("you can't begin a fight already begun");

        beforeBegin();

        turns.begin();
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

    public void cast(Fighter caster, Castable castable, FightCell targetCell) throws InteractionException {
        if (state != FightStateEnum.ACTIVE) throw new FightException("you can't cast now");
    }

    public void move(Fighter fighter, FightCell targetCell) throws InteractionException {
        if (state != FightStateEnum.ACTIVE) throw new FightException("you can't move now");
    }

    public void exceptionThrowed(Throwable throwable) {
        log.error("unhandled exception {} : {}", throwable.getClass(), throwable.toString());
    }
}
