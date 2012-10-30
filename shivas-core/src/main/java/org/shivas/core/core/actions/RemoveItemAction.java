package org.shivas.core.core.actions;

import org.shivas.data.Container;
import org.shivas.data.entity.Action;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.game.GameClient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/10/12
 * Time: 10:49
 */
public class RemoveItemAction implements Action {
    public static final String NAME = "REMOVE_ITEM";

    public static RemoveItemAction make(Map<String, String> params, Container ctner) {
        int templateId = Integer.parseInt(params.get("item"));
        int quantity = params.containsKey("quantity") ? Integer.parseInt(params.get("quantity")) : -1;

        return new RemoveItemAction(
                ctner.get(ItemTemplate.class).byId(templateId),
                quantity
        );
    }

    private final ItemTemplate template;
    private final int quantity;

    public RemoveItemAction(ItemTemplate template, int quantity) {
        this.template = template;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean able(Object target) {
        return target instanceof Player ||
               target instanceof GameClient;
    }

    @Override
    public void apply(Object target) {
        Player player;
        if (target instanceof Player) {
            player = (Player) target;
        } else if (target instanceof GameClient) {
            player = ((GameClient) target).player();
        } else return;

        GameItem item = player.getBag().get(template);
        if (item == null) return;

        if (quantity > 0) {
            item.minusQuantity(quantity);
        } else {
            item.setQuantity(0);
            player.getBag().delete(item);
        }

        if (player.getClient() != null) {
            if (item.getQuantity() > 0) {
                player.getClient().write(ItemGameMessageFormatter.quantityMessage(item.getId(), item.getQuantity()));
            } else {
                player.getClient().write(ItemGameMessageFormatter.deleteMessage(item.getId()));
            }
        }
    }
}
