package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.server.core.guilds.GuildCreationInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 16:35
 */
public class GuildHandler extends AbstractBaseHandler<GameClient> {
    public GuildHandler(GameClient client) {
        super(client);
    }

    @Override
    public void init() throws Exception { }

    @Override
    public void onClosed() { }

    @Override
    public void handle(String message) throws Exception {
        String[] args;
        switch (message.charAt(1)) {
        case 'C':
            args = message.substring(2).split("\\|");
            parseCreateMessage(
                    args[4],
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3])
            );
            break;
        }
    }

    private void parseCreateMessage(String name, int backgroundId, int backgroundColor, int foregroundId, int foregroundColor) throws InteractionException {
        GuildEmblem emblem = new GuildEmblem(backgroundId, backgroundColor, foregroundId, foregroundColor);

        if (client.interactions().current(GuildCreationInteraction.class).create(name, emblem)) {
            client.interactions().remove(GuildCreationInteraction.class).end();
        }
    }
}
