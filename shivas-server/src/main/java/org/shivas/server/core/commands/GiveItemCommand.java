package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.core.commands.types.ItemTemplateType;
import org.shivas.server.core.commands.types.PlayerType;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

public class GiveItemCommand implements Command {
	
	private final RepositoryContainer repo;

	public GiveItemCommand(RepositoryContainer repo) {
		this.repo = repo;
	}

	@Override
	public String name() {
		return "give_item";
	}

	@Override
	public Conditions conditions() {
		Conditions conditions = new Conditions();
		
		conditions.add("target", new PlayerType(repo.players()), "The target to give item");
		conditions.add("item", ItemTemplateType.INSTANCE, "The item to give");
		conditions.add("quantity", Types.INTEGER, "The quantity of item to give", true);
		
		return conditions;
	}

	@Override
	public String help() {
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
		int quantity = params.get("quantity", Integer.class).intValue();
		if (quantity <= 0) quantity = 1;
		
		GameItem item = (GameItem) template.generate();
		item.setQuantity(quantity);
		
		target.getBag().persist(item);
		
		if (target.getClient() != null && target.getClient() != client) {
			target.getClient().tchat().info("\"%s\" gave you %d item (id=%d).", client.player().url(), quantity, template.getId());
		}

		client.write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
		
		log.info("You successfully gave %d item (id=%d).", quantity, template.getId());
	}

}
