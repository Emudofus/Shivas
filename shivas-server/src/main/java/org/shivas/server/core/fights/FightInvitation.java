package org.shivas.server.core.fights;

import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.interactions.Invitation;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/10/12
 * Time: 12:02
 */
public class FightInvitation extends Invitation {
    public FightInvitation(GameClient source, GameClient target) {
        super(source, target);
    }

    protected void unsubscribeMap(GameClient client) {
        GameMap map = client.player().getLocation().getMap();

        map.event().unsubscribe(client.eventListener());
        map.leave(client.player());
    }

    protected void startFight() throws InteractionException {
        new DuelFight(
                source.service().config(),
                source.player().getLocation().getMap(),
                source.player(),
                target.player()
        ).init();
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.FIGHT_INVITATION;
    }

    @Override
    public void begin() throws InteractionException {
        writeToAll(GameMessageFormatter.challengeRequestMessage(
                source.player().getId(),
                target.player().getId()
        ));
    }

    @Override
    public void accept() throws InteractionException {
        writeToAll(GameMessageFormatter.challengeAcceptedMessage(
                source.player().getId(),
                target.player().getId()
        ));

        unsubscribeMap(source);
        unsubscribeMap(target);
        startFight();
    }

    @Override
    public void decline() throws InteractionException {
        writeToAll(GameMessageFormatter.challengeDeclinedMessage(
                source.player().getId(),
                target.player().getId()
        ));
    }
}
