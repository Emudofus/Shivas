package org.shivas.server.core.commands.types;

import org.atomium.repository.BaseEntityRepository;
import org.atomium.util.Filter;
import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;
import org.shivas.server.database.models.Player;

public class PlayerType implements Type {

	public static final Player DEFAULT_VALUE = null;
	
	private final BaseEntityRepository<Integer, Player> players;
	
	public PlayerType(BaseEntityRepository<Integer, Player> players) {
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
		Player player;
		try {
			int playerId = Integer.parseInt(string);
			player = players.find(playerId);
		} catch (NumberFormatException e) {
			player = players.filter(new Filter<Player>() {
				public Boolean invoke(Player arg1) throws Exception {
					return arg1.getName().equals(string);
				}
			}).get(0);
		}
		
		if (player == null) {
			throw new ParsingException("unknown player " + string);
		} else {
			return player;
		}
	}

}
