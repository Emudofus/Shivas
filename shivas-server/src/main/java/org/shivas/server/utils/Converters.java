package org.shivas.server.utils;

import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.BaseCharacterType;
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
	
	public static Function<Player, BaseCharacterType> PLAYER_TO_BASECHARACTERTYPE = new Function<Player, BaseCharacterType>() {
		public BaseCharacterType apply(Player input) {
			return input.toBaseCharacterType();
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
	
	public static Function<GameItem, ItemTemplate> GAMEITEM_TO_ITEMTEMPLATE = new Function<GameItem, ItemTemplate>() {
		public ItemTemplate apply(GameItem input) {
			return input == null ?
						null :
						input.getTemplate();
		}
	};
	
	public static Function<ItemEffect, ItemEffect> ITEMEFFECT_COPY = new Function<ItemEffect, ItemEffect>() {
		public ItemEffect apply(ItemEffect input) {
			return input.copy();
		}
	};
	
	public static final int RADIX = 16;
	
	public static Function<ItemEffect, String> ITEMEFFECT_TO_STRING = new Function<ItemEffect, String>() {
		public String apply(ItemEffect input) {
			return Integer.toString(input.getEffect().value(), RADIX) + "," +
				   Integer.toString(input.getBonus(), RADIX);
		}
	};
	
	public static Function<String, ItemEffect> STRING_TO_ITEMEFFECT = new Function<String, ItemEffect>() {
		public ItemEffect apply(String input) {
			int index = input.indexOf(',');

			ItemEffectEnum effect = ItemEffectEnum.valueOf(Integer.parseInt(input.substring(0, index), RADIX));
			short bonus = Short.parseShort(input.substring(index + 1), RADIX);
			
			return new ItemEffect(effect, bonus);
		}
	};
}
