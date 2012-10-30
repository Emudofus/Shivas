package org.shivas.core.core.fights;

import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.core.interactions.Invitation;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.services.game.GameClient;
import org.shivas.core.services.game.handlers.FightHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/10/12
 * Time: 12:02
 */
public class FightInvitation extends Invitation {
    private final FightFactory fightFactory;

    public FightInvitation(GameClient source, GameClient target, FightFactory fightFactory) {
        super(source, target);
        this.fightFactory = fightFactory;
    }

    protected void unsubscribeMap(GameClient client) {
        GameMap map = client.player().getLocation().getMap();

        map.event().unsubscribe(client.eventListener());
        map.leave(client.player());
    }

    protected void startFight() throws InteractionException {
        GameMap map = source.player().getLocation().getMap();
        Fight fight = fightFactory.build(FightTypeEnum.DUEL, map, source.player(), target.player());

        try {
            source.newHandler(new FightHandler(source));
            target.newHandler(new FightHandler(target));
        } catch (Exception e) {
            throw new InteractionException(e);
        }

        fight.init();
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
