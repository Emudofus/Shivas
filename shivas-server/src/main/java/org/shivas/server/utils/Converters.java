package org.shivas.server.utils;

import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.server.core.GameActor;
import org.shivas.server.database.models.Player;

import com.google.common.base.Function;

public class Converters {
	private Converters() {}

	public static Function<Player, Integer> PLAYER_TO_ID = new Function<Player, Integer>() {
		public Integer apply(Player arg0) {
			return arg0.id();
		}
	};
	
	public static Function<GameActor, BaseRolePlayActorType> GAMEACTOR_TO_BASEROLEPLAYACTORTYPE = new Function<GameActor, BaseRolePlayActorType>() {
		public BaseRolePlayActorType apply(GameActor arg0) {
			return arg0.toBaseRolePlayActorType();
		}
	};
}
