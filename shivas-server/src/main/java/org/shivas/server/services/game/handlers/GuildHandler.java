package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.formatters.GuildGameMessageFormatter;
import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.server.core.guilds.GuildCreationInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.database.models.Guild;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
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
    public void init() throws Exception {
        if (client.player().hasGuild()) {
            client.player().getGuild().subscribe(client.eventListener());
        }
    }

    @Override
    public void onClosed() {
        if (client.player().hasGuild()) {
            client.player().getGuild().unsubscribe(client.eventListener());
        }
    }

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

        case 'I':
            switch (message.charAt(2)) {
            case 'G':
                parseGetInfosMessage();
                break;

            case 'M':
                parseGetMembersListMessage();
                break;
            }
            break;
        }
    }

    private void parseCreateMessage(String name, int backgroundId, int backgroundColor, int foregroundId, int foregroundColor) throws InteractionException, CriticalException {
        assertFalse(client.player().hasGuild(), "you must not have a guild");

        GuildEmblem emblem = new GuildEmblem(backgroundId, backgroundColor, foregroundId, foregroundColor);

        if (client.interactions().current(GuildCreationInteraction.class).create(name, emblem)) {
            client.interactions().remove(GuildCreationInteraction.class).end();
        }
    }

    private void parseGetInfosMessage() throws CriticalException {
        assertTrue(client.player().hasGuild(), "you must have a guild");

        Guild guild = client.player().getGuild();

        client.write(GuildGameMessageFormatter.informationsGeneralMessage(
                guild.isValid(),
                guild.getExperience().level(),
                guild.getExperience().current(),
                guild.getExperience().min(),
                guild.getExperience().max()
        ));
    }

    private void parseGetMembersListMessage() throws CriticalException {
        assertTrue(client.player().hasGuild(), "you must have a guild");

        Guild guild = client.player().getGuild();

        client.write(GuildGameMessageFormatter.membersListMessage(guild.getMembers().toBaseGuildMemberType()));
    }
}
