package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.PlayerFighter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 22:31
 */
public class FightHandler extends AbstractBaseHandler<GameClient> {
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
    }

    @Override
    public void onClosed() { }

    @Override
    public void handle(String message) throws Exception {
        String[] args;
        switch (message.charAt(0)) {
        case 'G':
            switch (message.charAt(1)) {
            case 'A':
                parseGameActionMessage(ActionTypeEnum.valueOf(Integer.parseInt(message.substring(2, 5))), message.substring(5));
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
}
