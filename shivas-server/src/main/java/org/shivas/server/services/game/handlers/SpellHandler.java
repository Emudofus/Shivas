package org.shivas.server.services.game.handlers;

import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.protocol.client.formatters.SpellGameMessageFormatter;
import org.shivas.server.database.models.Spell;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;

public class SpellHandler extends AbstractBaseHandler<GameClient> {

	public SpellHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		String[] args;
		switch (message.charAt(1)) {
		case 'B':
			parseBoostMessage(client.player().getSpells().get(Short.parseShort(message.substring(2))));
			break;
			
		case 'M':
			args = message.substring(2).split("\\|");
			parseMoveMessage(
					client.player().getSpells().get(Short.parseShort(args[0])),
					Byte.parseByte(args[1])
			);
			break;
		}
	}

	private void parseBoostMessage(Spell spell) throws CriticalException {
		assertFalse(spell == null, "unknown spell");
		assertFalse(spell.getLevel().getId() >= SpellTemplate.MAX_LEVELS, "max level");
		
		int cost = spell.getLevel().getId(); // better impl ?
		
		if (client.player().getStats().spellPoints() < cost) {
			client.write(SpellGameMessageFormatter.boostSpellErrorMessage());
		} else {
			client.player().getStats().addSpellPoints((short) -cost);
			spell.incrementLevel();
			
			client.service().repositories().spells().saveLater(spell);
			client.service().repositories().players().saveLater(client.player());
			
			client.write(SpellGameMessageFormatter.boostSpellSuccessMessage(
					spell.getTemplate().getId(),
					spell.getLevel().getId()
			));
			client.write(client.player().getStats().packet());
		}
	}

	private void parseMoveMessage(Spell spell, byte position) throws CriticalException {
		assertFalse(spell == null, "unknown spell");
		
		client.write(BasicGameMessageFormatter.noOperationMessage());
		
		client.player().getSpells().move(spell, position);
	}

}
