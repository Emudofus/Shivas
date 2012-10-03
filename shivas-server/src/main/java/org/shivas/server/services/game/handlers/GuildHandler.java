package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.GuildInvitationErrorEnum;
import org.shivas.protocol.client.enums.GuildMemberRightsEnum;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.protocol.client.formatters.GuildGameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.server.core.guilds.GuildCreationInteraction;
import org.shivas.server.core.guilds.GuildInvitation;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.database.models.Guild;
import org.shivas.server.database.models.GuildMember;
import org.shivas.server.database.models.Player;
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
            Guild guild = client.player().getGuild();

            client.write(GuildGameMessageFormatter.statsMessage(
                    guild.getName(),
                    guild.getEmblem(),
                    client.player().getGuildMember().getRights().toInt()
            ));

            guild.subscribe(client.eventListener());
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

        case 'J':
            switch (message.charAt(2)) {
            case 'E':
                parseDeclineInvitationMessage();
                break;

            case 'K':
                parseAcceptInvitationMessage();
                break;

            case 'R':
                parseInvitationMessage(message.substring(3));
                break;
            }
            break;

        case 'K':
            parseKickMessage(message.substring(2));
            break;

        case 'P':
            args = message.substring(2).split("\\|");
            parseEditMemberMessage(
                    Integer.parseInt(args[0]),
                    GuildRankEnum.valueOf(Integer.valueOf(args[1])),
                    Byte.parseByte(args[2]),
                    Integer.parseInt(args[3])
            );
            break;

        case 'V':
            parseClosePanelMessage();
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

    private void parseInvitationMessage(String name) throws CriticalException, InteractionException {
        assertTrue(client.player().hasGuild(), "you must have a guild");

        Player player = client.service().repositories().players().find(name);
        GameClient target = player != null ? player.getClient() : null;
        if (player == null) {
            client.write(GuildGameMessageFormatter.invitationErrorMessage(GuildInvitationErrorEnum.UNKNOWN));
        } else if (target == null || target.isBusy()) {
            client.write(GuildGameMessageFormatter.invitationErrorMessage(GuildInvitationErrorEnum.AWAY));
        } else if (player.hasGuild()) {
            client.write(GuildGameMessageFormatter.invitationErrorMessage(GuildInvitationErrorEnum.ALREADY_IN_GUILD));
        } else if (!client.player().getGuildMember().getRights().has(GuildMemberRightsEnum.INVITE)) {
            client.write(GuildGameMessageFormatter.invitationErrorMessage(GuildInvitationErrorEnum.NOT_ENOUGH_RIGHTS));
        } else if (client.player().getGuild().isFull()) {
            client.write(InfoGameMessageFormatter.fullGuildMessage(client.player().getGuild().getMaxPlaces()));
        } else {
            client.interactions().push(new GuildInvitation(client, target)).begin();
        }
    }

    private void parseDeclineInvitationMessage() throws InteractionException {
        client.interactions().remove(GuildInvitation.class).decline();
    }

    private void parseAcceptInvitationMessage() throws CriticalException, InteractionException {
        GuildInvitation invitation = client.interactions().remove(GuildInvitation.class);
        assertTrue(invitation.getTarget() == client, "you can not accept, you are the source");

        invitation.accept();
    }

    private void parseEditMemberMessage(int memberId, GuildRankEnum rank, byte rate, int rights) throws CriticalException {
        assertTrue(client.player().hasGuild(), "you must have a guild");

        Guild guild = client.player().getGuild();

        GuildMember member = client.player().getGuild().getMembers().get(memberId),
                    source = client.player().getGuildMember();
        assertTrue(member != null, "unknown member %d", memberId);

        if (member.getRank() == GuildRankEnum.LEADER && member != source) {
            return; // don't know if it may throws an exception or anything else
        }

        if (source.getRights().has(GuildMemberRightsEnum.RANK) && rank != member.getRank()) {
            assertFalse(rank == GuildRankEnum.LEADER && member != source, "only leader can set a new leader");

            member.setRank(rank);
            if (rank == GuildRankEnum.LEADER) {
                guild.setLeader(member.getPlayer());
                member.getRights().fill(true);

                source.setRank(GuildRankEnum.TESTING);
                source.getRights().fill(false);
            }
        }

        if (source.getRights().has(GuildMemberRightsEnum.RATES_XP_ALL) || (member == source && source.getRights().has(GuildMemberRightsEnum.RATES_XP))) {
            member.setExperienceRate(rate);
        }

        if (source.getRights().has(GuildMemberRightsEnum.RIGHTS)) {
            member.getRights().fromInt(rights);
        }

        client.write(GuildGameMessageFormatter.membersListMessage(guild.getMembers().toBaseGuildMemberType()));

        if (member.getPlayer().getClient() != null) {
            member.getPlayer().getClient().write(GuildGameMessageFormatter.statsMessage(
                    guild.getName(),
                    guild.getEmblem(),
                    member.getRights().toInt()
            ));
        }
    }

    private void parseKickMessage(String name) throws CriticalException {
        assertTrue(client.player().hasGuild(), "you must have a guild");

        Guild guild = client.player().getGuild();
        GuildMember member = guild.getMembers().get(name), source = client.player().getGuildMember();

        assertTrue(member != null, "unknown member %s", name);
        assertTrue(member.getRank() != GuildRankEnum.LEADER, "you can not kick the leader");
        assertTrue(source.getRights().has(GuildMemberRightsEnum.BAN) || member == source, "you have not enough rights");

        guild.getMembers().remove(member, client.player());

        client.write(GuildGameMessageFormatter.kickLocalSuccessMessage(source.getPlayer().getName(), member.getPlayer().getName()));

        if (member != source) {
            client.write(GuildGameMessageFormatter.membersListMessage(guild.getMembers().toBaseGuildMemberType()));
        }
    }

    private void parseClosePanelMessage() throws InteractionException {
        client.interactions().remove(GuildCreationInteraction.class).end();
    }
}
