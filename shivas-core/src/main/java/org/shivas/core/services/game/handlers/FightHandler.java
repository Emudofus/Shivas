package org.shivas.core.services.game.handlers;

import org.shivas.core.core.castables.Fists;
import org.shivas.core.core.castables.Weapon;
import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventListener;
import org.shivas.core.core.fights.*;
import org.shivas.core.core.fights.events.*;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.paths.Path;
import org.shivas.core.core.paths.PathNotFoundException;
import org.shivas.core.core.paths.Pathfinder;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.Spell;
import org.shivas.core.services.CriticalException;
import org.shivas.core.services.game.GameClient;
import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.protocol.client.enums.EndActionTypeEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.formatters.FightGameMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 22:31
 */
public class FightHandler extends RolePlayHandler implements EventListener {
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
        fight.getEvent().subscribe(client.eventListener());

        super.configure();
    }

    @Override
    public void onClosed() {
        fight.getEvent().unsubscribe(this);
    }

    @Override
    public void handle(String message) throws Exception {
        String[] args;
        switch (message.charAt(0)) {
        case 'B':
            switch (message.charAt(1)) {
            case 'M':
                parseSendMessage(message.substring(2));
                break;

            default:
                super.handle(message);
                break;
            }
            break;

        case 'G':
            switch (message.charAt(1)) {
            case 'A':
                parseGameActionMessage(ActionTypeEnum.valueOf(Integer.parseInt(message.substring(2, 5))), message.substring(5));
                break;

            case 'p':
                parseChangePlaceMessage(Short.parseShort(message.substring(2)));
                break;

            case 'Q':
                if (message.length() > 2) {
                    parseKickMessage(Integer.parseInt(message.substring(2)));
                } else {
                    parseQuitMessage();
                }
                break;

            case 'R':
                parseSetReadyMessage();
                break;

            case 't':
                parseEndTurnMessage();
                break;
            }
            break;

        default:
            super.handle(message);
        }
    }

    private void parseSendMessage(String message) throws Exception {
        String[] args = message.split("\\|", 2);
        if (args[0].equals("*")) {
            fight.speak(fighter, args[1]);
        } else {
            super.handle(message);
        }
    }

    private void parseGameActionMessage(ActionTypeEnum actionType, String data) throws Exception {
        String[] args;
        switch (actionType) {
        case MOVEMENT:
            parseMovementMessage(Path.parsePath(data));
            break;

        case CAST_SPELL:
            args = data.split(";");
            parseCastSpellMessage(Short.parseShort(args[0]), Short.parseShort(args[1]));
            break;

        case MELEE_ATTACK:
            parseMeleeAttackMessage(Short.parseShort(data));
            break;

        default:
            log.warn("unknown action {} (data={})", actionType, data);
            break;
        }
    }

    private void parseMovementMessage(Path path) throws CriticalException, PathNotFoundException, InteractionException {
        assertTrue(fighter.getTurn().isCurrent(), "it is not your turn");
        assertTrue(path.size() > 0, "invalid path");

        Pathfinder finder = new FightPathfinder(fighter, path.last().cell());
        fight.move(fighter, finder.find());
    }

    private void parseMeleeAttackMessage(short targetCell) throws CriticalException, InteractionException {
        assertTrue(fighter.getTurn().isCurrent(), "it is not your turn");

        GameItem item = client.player().getBag().get(ItemPositionEnum.Weapon);
        fight.cast(fighter, item == null ? Fists.INSTANCE : (Weapon) item, targetCell);
    }

    private void parseCastSpellMessage(short spellId, short cellId) throws CriticalException, InteractionException {
        assertTrue(fighter.getTurn().isCurrent(), "it is not your turn");

        Spell spell = client.player().getSpells().get(spellId);
        assertTrue(spell != null, "unknown spell %d", spellId);

        fight.cast(fighter, spell, cellId);
    }

    private void parseChangePlaceMessage(short cellId) throws InteractionException {
        fight.changePlace(fighter, cellId);
    }

    private void parseQuitMessage() throws InteractionException {
        fight.quit(fighter);
    }

    private void parseKickMessage(int fighterId) throws CriticalException, InteractionException {
        Fighter target = fight.find(fighterId);
        assertFalse(target == null, "unknown fighter %d", fighterId);

        if (fighter != target) {
            assertTrue(target.getTeam() == fighter.getTeam(), "you must be in the same team");
            assertTrue(fighter.isLeader(), "you must be the leader");
        }

        fight.quit(target);
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

        case FIGHTER_ACTION:
            listenFighterAction((FighterActionEvent) event);
            break;

        case FIGHTER_MOVEMENT_END:
            listenFightEndMovement((FighterMovementEndEvent) event);
            break;

        case FIGHTER_CAST_END:
            listenFighterCastEnd((FighterCastEndEvent) event);
            break;

        case FIGHTER_QUIT:
            listenFighterQuit((FighterQuitEvent) event);
            break;
        }
    }

    private void listenFightInitialization() {
        client.write(FightGameMessageFormatter.newFightMessage(
                fight.getState(),
                fighter.canQuit(),
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

        case FINISHED:
            listenFightFinished(event);
            break;
        }
    }

    private void listenFightBeginning() {
        client.write(FightGameMessageFormatter.fightersPlacementMessage(fight.toBaseFighterType()));
        client.write(FightGameMessageFormatter.fightStartMessage());
        client.write(FightGameMessageFormatter.turnListMessage(fight.getTurns().toInt()));
        client.write(FightGameMessageFormatter.fighterInformationsMessage(fight.toBaseFighterType()));
    }

    private void listenFightFinished(StateUpdateEvent event) {
        fight.getEvent().unsubscribe(this);
        client.player().setFighter(null);

        if (event instanceof FightEndEvent) {
            FightEndEvent endEvent = (FightEndEvent) event;

            client.write(FightGameMessageFormatter.fightEndMessage(
                    fight.getDuration().getMillis(),
                    fight.getFightType() == FightTypeEnum.AGRESSION,
                    endEvent.getWinners().getLeader().toBaseEndFighterType(),
                    endEvent.getWinners().toBaseEndFighterType(),
                    endEvent.getLosers().toBaseEndFighterType()
            ));
        } else {
            client.write(FightGameMessageFormatter.fightEndMessage(
                    fight.getDuration().getMillis(),
                    fight.getFightType() == FightTypeEnum.AGRESSION,
                    fight.getChallengers().getLeader().toBaseEndFighterType(),
                    fight.getChallengers().toBaseEndFighterType(),
                    fight.getDefenders().toBaseEndFighterType()
            ));
        }

        try {
            client.newHandler(new RolePlayHandler(client));
        } catch (Exception e) {
            log.error("can't set the roleplay handler", e);
        }
    }

    private void listenFightQuit() {
        fight.getEvent().unsubscribe(this);
        client.player().setFighter(null);

        client.write(FightGameMessageFormatter.fighterLeftMessage());

        try {
            client.newHandler(new RolePlayHandler(client));
        } catch (Exception e) {
            log.error("can't set the roleplay handler", e);
        }
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
        client.write(FightGameMessageFormatter.fighterInformationsMessage(fight.toBaseFighterType()));
        client.write(FightGameMessageFormatter.turnEndMessage(turn.getFighter().getId()));
    }

    private void listenFighterAction(FighterActionEvent event) {
        switch (event.getActionType()) {
        case MOVEMENT:
            listenFighterMovement((FighterMovementEvent) event);
            break;

        case CAST_SPELL:
        case MELEE_ATTACK:
            listenFighterCast((FighterCastEvent) event);
            break;

        case LIFE_CHANGEMENT:
            listenFighterLifeUpdate((FighterLifeUpdateEvent) event);
            break;

        case CELL_CHANGEMENT:
            if (event instanceof FighterTeleportEvent) {
                listenFighterTeleport((FighterTeleportEvent) event);
            } else if (event instanceof FighterTransposeEvent) {
                listenFighterTranspose((FighterTransposeEvent) event);
            }
            break;

        case CELL_SLIDE:
            listenFighterSlide((FighterSlideEvent) event);
            break;
        }
    }

    private void listenFighterCast(FighterCastEvent event) {
        client.write(FightGameMessageFormatter.startActionMessage(event.getFighter().getId()));

        if (event.isFailure()) {
            client.write(FightGameMessageFormatter.actionMessage(ActionTypeEnum.SPELL_FAILURE, event.getFighter().getId()));
        } else {
            if (event.getCastable() instanceof Weapon || event.getCastable() == Fists.INSTANCE) {
                client.write(FightGameMessageFormatter.actionMessage(
                        ActionTypeEnum.MELEE_ATTACK,
                        event.getFighter().getId(),
                        event.getFighter().getId(),
                        event.getTarget().getId()
                ));

                if (event.isCritical()) {
                    client.write(FightGameMessageFormatter.actionMessage(ActionTypeEnum.SPELL_CRITICAL, event.getFighter().getId()));
                }
            } else {
                Spell spell = (Spell) event.getCastable();

                client.write(FightGameMessageFormatter.castSpellActionMessage(
                        event.getFighter().getId(),
                        spell.getTemplate().getId(),
                        spell.getTemplate().getSprite(),
                        spell.getTemplate().getSpriteInfos(),
                        spell.getLevel().getId(),
                        event.getTarget().getId()
                ));

                if (event.isCritical()) {
                    client.write(FightGameMessageFormatter.actionMessage(
                            ActionTypeEnum.SPELL_CRITICAL,
                            event.getFighter().getId(),
                            spell.getTemplate().getId()
                    ));
                }
            }
        }
    }

    private void listenFighterLifeUpdate(FighterLifeUpdateEvent event) {
        client.write(FightGameMessageFormatter.actionMessage(
                ActionTypeEnum.LIFE_CHANGEMENT,
                event.getFighter().getId(),
                event.getTarget().getId(),
                event.getDelta()
        ));
    }

    private void listenFighterTeleport(FighterTeleportEvent event) {
        client.write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_CHANGEMENT,
                event.getFighter().getId(),
                event.getFighter().getId(),
                event.getTarget().getId()
        ));
    }

    private void listenFighterTranspose(FighterTransposeEvent event) {
        client.write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_CHANGEMENT,
                event.getFighter().getId(),
                event.getFighter().getId(),
                event.getFighter().getCurrentCell().getId()
        ));

        client.write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_CHANGEMENT,
                event.getFighter().getId(),
                event.getTarget().getId(),
                event.getTarget().getCurrentCell().getId()
        ));
    }

    private void listenFighterSlide(FighterSlideEvent event) {
        client.write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_SLIDE,
                event.getFighter().getId(),
                event.getTarget().getId(),
                event.getNewCell().getId()
        ));
    }

    private void listenFighterMovement(FighterMovementEvent event) {
        client.write(FightGameMessageFormatter.fighterMovementMessage(
                event.getFighter().getId(),
                event.getPath().toString()
        ));
    }

    private void listenFightEndMovement(FighterMovementEndEvent event) {
        client.write(FightGameMessageFormatter.actionMessage(
                ActionTypeEnum.MP_CHANGEMENT,
                event.getFighter().getId(),
                event.getFighter().getId(),
                event.getUsedMovementPoints() * -1
        ));

        if (event.getFighter() instanceof PlayerFighter) {
            client.write(FightGameMessageFormatter.endFightActionMessage(EndActionTypeEnum.MOVEMENT, event.getFighter().getId()));
        }
    }

    private void listenFighterCastEnd(FighterCastEndEvent event) {
        client.write(FightGameMessageFormatter.actionMessage(
                ActionTypeEnum.AP_CHANGEMENT,
                event.getFighter().getId(),
                event.getFighter().getId(),
                -event.getCastable().getCost()
        ));

        client.write(FightGameMessageFormatter.endFightActionMessage(EndActionTypeEnum.SPELL, event.getFighter().getId()));
    }

    private void listenFighterQuit(FighterQuitEvent event) {
        if (event.getFighter() == fighter) {
            listenFightQuit();
        } else {
            client.write(FightGameMessageFormatter.fighterQuitMessage(event.getFighter().getId()));
        }
    }
}
