package org.shivas.server.core.actions;

import org.shivas.data.entity.Action;
import org.shivas.server.core.guilds.GuildCreationInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/09/12
 * Time: 23:40
 */
public class CreateGuildAction implements Action {
    public static final int TYPE = 4;

    public static CreateGuildAction make(Map<String, String> params) {
        return new CreateGuildAction();
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public boolean able(Object target) {
        return (target instanceof GameClient) ||
               (target instanceof Player && ((Player) target).getClient() != null);
    }

    @Override
    public void apply(Object target) {
        GameClient client = null;
        if (target instanceof GameClient) {
            client = (GameClient) target;
        } else if (target instanceof Player) {
            client = ((Player) target).getClient();
        }
        if (client == null) return;

        try {
            client.interactions().push(new GuildCreationInteraction(client)).begin();
        } catch (InteractionException e) {
            e.printStackTrace();
        }
    }
}
