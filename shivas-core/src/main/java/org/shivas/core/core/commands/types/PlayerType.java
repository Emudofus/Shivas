package org.shivas.core.core.commands.types;

import org.atomium.repository.BaseEntityRepository;
import org.atomium.util.Filter;
import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;
import org.shivas.common.params.Types;
import org.shivas.core.database.models.Player;

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
            final String playerName = (String) Types.STRING.parse(string);

            player = players.find(new Filter<Player>() {
                public Boolean invoke(Player player) throws Exception {
                    return player.getName().equalsIgnoreCase(playerName);
                }
            });
        } catch (ParsingException e) {
            final Integer playerId = (Integer) Types.INTEGER.parse(string);

            player = players.find(playerId);
        }

        if (player == null) throw new ParsingException("unknown player " + string);
        return player;
	}

}
