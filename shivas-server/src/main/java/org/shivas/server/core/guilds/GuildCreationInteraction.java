package org.shivas.server.core.guilds;

import org.shivas.protocol.client.formatters.GuildGameMessageFormatter;
import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.database.models.Guild;
import org.shivas.server.database.repositories.GuildRepository;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/09/12
 * Time: 23:46
 */
public class GuildCreationInteraction extends AbstractInteraction {
    private final GameClient client;

    public GuildCreationInteraction(GameClient client) {
        this.client = client;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.GUILD_CREATION;
    }

    @Override
    protected void internalEnd() throws InteractionException {
        cancel();
    }

    @Override
    public void begin() throws InteractionException {
        client.write(GuildGameMessageFormatter.startCreationMessage());
    }

    @Override
    public void cancel() throws InteractionException {
        client.write(GuildGameMessageFormatter.quitCreationMessage());
    }

    public boolean create(String name, GuildEmblem emblem) throws InteractionException {
        GuildRepository guilds = client.service().repositories().guilds();

        if (client.player().hasGuild()) {
            client.write(GuildGameMessageFormatter.alreadyHaveGuildMessage());
        } else if (guilds.exists(name)) {
            client.write(GuildGameMessageFormatter.nameExistsMessage());
        } else {
            Guild guild = guilds.createDefault(name, client.player(), emblem);

            client.write(GuildGameMessageFormatter.statsMessage(
                    guild.getName(),
                    guild.getEmblem(),
                    client.player().getGuildMember().getRights().toInt()
            ));
            client.write(GuildGameMessageFormatter.creationSuccessMessage());

            guild.subscribe(client.eventListener());

            return true;
        }

        return false;
    }
}
