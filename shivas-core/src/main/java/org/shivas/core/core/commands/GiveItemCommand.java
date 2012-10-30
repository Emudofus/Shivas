package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.core.core.commands.types.ItemTemplateType;
import org.shivas.core.core.commands.types.PlayerType;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

public class GiveItemCommand implements Command {

    @Inject
	private RepositoryContainer repo;

	@Override
	public String getName() {
		return "give_item";
	}

	@Override
	public Conditions getConditions() {
		return new Conditions() {{
            add("target", new PlayerType(repo.players()), "The target to give item");
            add("item", ItemTemplateType.INSTANCE, "The item to give");
            add("quantity", Types.INTEGER, "The quantity of item to give", true);
        }};
	}

	@Override
	public String getHelp() {
		return "Give an item";
	}

	@Override
	public boolean canUse(GameClient client) {
		return client.account().hasRights();
	}

	@Override
	public void use(GameClient client, DofusLogger log, Parameters params) {
		Player target = params.get("target", Player.class);
		ItemTemplate template = params.get("item", ItemTemplate.class);
		int quantity = params.get("quantity", Integer.class);
		if (quantity <= 0) quantity = 1;
		
		GameItem item = (GameItem) template.generate();
		item.setQuantity(quantity);
		
		target.getBag().persist(item);
		
		if (target.getClient() != null) {
            target.getClient().write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));

            if (target.getClient() != client) {
                target.getClient().tchat().info("\"%s\" gave you %d item (id=%d).", client.player().url(), quantity, template.getId());
            }
		}
		
		log.info("You successfully gave %d item (id=%d).", quantity, template.getId());
	}

}
