package org.shivas.server.core.commands.types;

import org.atomium.repository.EntityRepository;
import org.atomium.util.Filter;
import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;
import org.shivas.server.database.models.Player;

public class PlayerType implements Type {

	public static final Player DEFAULT_VALUE = null;
	
	private final EntityRepository<Integer, Player> players;
	
	public PlayerType(EntityRepository<Integer, Player> players) {
		this.players = players;
	}

	@Override
	public Class<?> getJavaClass() {
		return Player.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(final String string) throws ParsingException {
		try {
			int playerId = Integer.parseInt(string);
			return players.find(playerId);
		} catch (NumberFormatException e) {
			return players.filter(new Filter<Player>() {
				public Boolean invoke(Player arg1) throws Exception {
					return arg1.getName().equals(string);
				}
			});
		}
	}

}
