package org.shivas.core.core.channels;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventListener;
import org.shivas.core.database.models.Guild;
import org.shivas.core.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 13:16
 */
public class GuildChannel implements Channel {
    @Override
    public void subscribe(EventListener listener) { }

    @Override
    public void unsubscribe(EventListener listener) { }

    @Override
    public void send(GameActor author, String message) {
        if (!(author instanceof Player)) return;

        Guild guild = ((Player) author).getGuild();
        if (guild != null) {
            guild.publish(new ChannelEvent(ChannelEnum.Guild, author, message));
        }
    }
}
