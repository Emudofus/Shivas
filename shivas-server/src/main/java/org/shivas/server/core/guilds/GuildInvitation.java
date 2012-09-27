package org.shivas.server.core.guilds;

import org.shivas.protocol.client.formatters.GuildGameMessageFormatter;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.interactions.Invitation;
import org.shivas.server.database.models.Guild;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 15:10
 */
public class GuildInvitation extends Invitation {
    public GuildInvitation(GameClient source, GameClient target) {
        super(source, target);
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.GUILD_INVITATION;
    }

    @Override
    public void begin() throws InteractionException {
        source.write(GuildGameMessageFormatter.invitationLocalMessage(target.player().getName()));

        target.write(GuildGameMessageFormatter.invitationRemoteMessage(
                source.player().getId(),
                source.player().getName(),
                source.player().getGuild().getName()
        ));
    }

    @Override
    public void accept() throws InteractionException {
        Guild guild = source.player().getGuild();

        guild.getMembers().add(target.player(), source.player());

        source.write(GuildGameMessageFormatter.invitationRemoteSuccessMessage(target.player().getName()));

        target.write(GuildGameMessageFormatter.statsMessage(
                guild.getName(),
                guild.getEmblem(),
                target.player().getGuildMember().getRights().toInt()
        ));
        target.write(GuildGameMessageFormatter.invitationLocalSuccessMessage());

        guild.subscribe(target.eventListener());
    }

    @Override
    public void decline() throws InteractionException {
        source.write(GuildGameMessageFormatter.invitationFailureMessage());
        target.write(GuildGameMessageFormatter.invitationFailureMessage());
    }
}
