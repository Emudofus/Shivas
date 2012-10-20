package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.protocol.client.formatters.FightGameMessageFormatter;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.FightTurn;
import org.shivas.server.core.fights.PlayerFighter;
import org.shivas.server.core.fights.events.FightEvent;
import org.shivas.server.core.fights.events.FightTurnEvent;
import org.shivas.server.core.fights.events.FighterEvent;
import org.shivas.server.core.fights.events.StateUpdateEvent;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 22:31
 */
public class FightHandler extends AbstractBaseHandler<GameClient> implements EventListener {
    private static final Logger log = LoggerFactory.getLogger(FightHandler.class);

    protected Fight fight;
    protected PlayerFighter fighter;

    public FightHandler(GameClient client) {
        super(client);
    }

    @Override
    public void init() throws Exception {
        if (!client.player().isFighting()) throw new Exception("player is not fighting");

        fight = client.player().getFight();
        fighter = client.player().getFighter();

        fight.getEvent().subscribe(this);
    }

    @Override
    public void onClosed() {
        fight.getEvent().unsubscribe(this);
    }

    @Override
    public void handle(String message) throws Exception {
        String[] args;
        switch (message.charAt(0)) {
        case 'G':
            switch (message.charAt(1)) {
            case 'A':
                parseGameActionMessage(ActionTypeEnum.valueOf(Integer.parseInt(message.substring(2, 5))), message.substring(5));
                break;

            case 'p':
                parseChangePlaceMessage(Short.parseShort(message.substring(2)));
                break;

            case 'R':
                parseSetReadyMessage();
                break;

            case 't':
                parseEndTurnMessage();
                break;
            }
            break;
        }
    }

    private void parseGameActionMessage(ActionTypeEnum actionType, String data) {
        switch (actionType) {
        case MOVEMENT: // TODO
            break;

        case CAST_SPELL: // TODO
            break;

        case MELEE_ATACK: // TODO
            break;

        default:
            log.warn("unknown action {} (data={})", actionType, data);
            break;
        }
    }

    private void parseChangePlaceMessage(short cellId) throws InteractionException {
        fight.changePlace(fighter, cellId);
    }

    private void parseSetReadyMessage() throws InteractionException {
        fighter.setReady();
    }

    private void parseEndTurnMessage() throws FightException, CriticalException {
        assertTrue(fighter.getTurn().isCurrent(), "it is not your turn");
        fighter.getTurn().end();
    }

    @Override
    public void listen(Event event) {
        switch (event.type()) {
        case FIGHT:
            listenFight((FightEvent) event);
            break;
        }
    }

    private void listenFight(FightEvent event) {
        switch (event.getFightEventType()) {
        case INITIALIZATION:
            listenFightInitialization();
            break;

        case FIGHTER_PLACEMENT:
            listenFighterPlacement((FighterEvent) event);
            break;

        case FIGHTER_READY:
            listenFighterReady((FighterEvent) event);
            break;

        case STATE_UPDATE:
            listenStateUpdate((StateUpdateEvent) event);
            break;

        case TURN:
            listenTurn((FightTurnEvent) event);
            break;
        }
    }

    private void listenFightInitialization() {
        client.write(FightGameMessageFormatter.newFightMessage(
                fight.getState(),
                fight.canCancel(),
                fight.getFightType() == FightTypeEnum.DUEL,
                false, // TODO fight spectators
                fight.getRemainingPreparation(),
                fight.getFightType()
        ));

        client.write(FightGameMessageFormatter.startCellsMessage(
                fight.getChallengers().getEncodedStartCells(),
                fight.getDefenders().getEncodedStartCells(),
                fighter.getTeam().getType()
        ));

        client.write(FightGameMessageFormatter.showFightersMessage(fight.getChallengers().toBaseFighterType()));
        client.write(FightGameMessageFormatter.showFightersMessage(fight.getDefenders().toBaseFighterType()));
    }

    private void listenFighterPlacement(FighterEvent event) {
        client.write(FightGameMessageFormatter.fighterPlacementMessage(
                event.getFighter().getId(),
                event.getFighter().getCurrentCell().getId(),
                event.getFighter().getCurrentOrientation()
        ));
    }

    private void listenFighterReady(FighterEvent event) {
        client.write(FightGameMessageFormatter.fighterReadyMessage(
                event.getFighter().getId(),
                event.getFighter().isReady()
        ));
    }

    private void listenStateUpdate(StateUpdateEvent event) {
        switch (event.getNewState()) {
        case ACTIVE:
            listenFightBeginning();
            break;
        }
    }

    private void listenFightBeginning() {
        client.write(FightGameMessageFormatter.fightersPlacementMessage(fight.toBaseFighterType()));
        client.write(FightGameMessageFormatter.fightStartMessage());
        client.write(FightGameMessageFormatter.turnListMessage(fight.getTurns().toInt()));
        client.write(FightGameMessageFormatter.fighterInformationsMessage(fight.toBaseFighterType()));
    }

    private void listenTurn(FightTurnEvent event) {
        switch (event.getFightTurnEventType()) {
        case START:
            listenStartTurn(event.getTurn());
            break;

        case STOP:
            listenStopTurn(event.getTurn());
            break;
        }
    }

    private void listenStartTurn(FightTurn turn) {
        client.write(FightGameMessageFormatter.turnStartMessage(turn.getFighter().getId(), turn.getRemaining().getMillis()));
    }

    private void listenStopTurn(FightTurn turn) {
        client.write(FightGameMessageFormatter.turnEndMessage(turn.getFighter().getId()));
    }
}
