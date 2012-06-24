package org.shivas.server.utils;

import org.shivas.data.entity.ItemEffect;
import org.shivas.protocol.client.types.BaseItemEffectType;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.server.core.GameActor;
import org.shivas.server.database.models.GameItem;
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
	
	public static Function<ItemEffect, BaseItemEffectType> ITEMEFFECT_TO_BASEITEMEFFECTTYPE = new Function<ItemEffect, BaseItemEffectType>() {
		public BaseItemEffectType apply(ItemEffect arg0) {
			return new BaseItemEffectType(arg0.getEffect(), arg0.getBonus());
		}
	};
	
	public static Function<GameItem, BaseItemType> GAMEITEM_TO_BASEITEMTYPE = new Function<GameItem, BaseItemType>() {
		public BaseItemType apply(GameItem arg0) {
			return arg0.toBaseItemType();
		}
	};
	
	public static Function<ItemEffect, ItemEffect> ITEMEFFECT_COPY = new Function<ItemEffect, ItemEffect>() {
		public ItemEffect apply(ItemEffect input) {
			return input.copy();
		}
	};
}
