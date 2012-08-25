package org.shivas.server.utils;

import org.shivas.common.random.Dice;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.data.entity.ConstantItemEffect;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.data.entity.WeaponItemEffect;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.protocol.client.types.BaseContactType;
import org.shivas.protocol.client.types.BaseItemEffectType;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.BaseSpellType;
import org.shivas.server.core.GameActor;
import org.shivas.server.database.models.Contact;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.Spell;

import com.google.common.base.Function;

public class Converters {
	private Converters() {}

	public static Function<Player, Integer> PLAYER_TO_ID = new Function<Player, Integer>() {
		public Integer apply(Player arg0) {
			return arg0.getPublicId();
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
			if (arg0 instanceof ConstantItemEffect) {
				return new BaseItemEffectType(arg0.getType(), ((ConstantItemEffect) arg0).getBonus());
			} else if (arg0 instanceof WeaponItemEffect) {
				return new BaseItemEffectType(arg0.getType(), ((WeaponItemEffect) arg0).getDice());
			}
			return null;
		}
	};
	
	public static Function<ItemEffect, ItemEffectEnum> ITEMEFFECT_TO_ENUM = new Function<ItemEffect, ItemEffectEnum>() {
		public ItemEffectEnum apply(ItemEffect input) {
			return input.getType();
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

	public static final Function<Spell, BaseSpellType> SPELL_TO_BASESPELLTYPE = new Function<Spell, BaseSpellType>() {
		public BaseSpellType apply(Spell input) {
			return input == null ?
						null :
						input.toBaseSpellType();
		}
	};
	
	public static final int RADIX = 16;
	
	public static Function<ItemEffect, String> ITEMEFFECT_TO_STRING = new Function<ItemEffect, String>() {
		public String apply(ItemEffect input) {
		    if (input instanceof ConstantItemEffect) {
				return Integer.toString(input.getType().value(), RADIX) + "," +
					   Integer.toString(((ConstantItemEffect) input).getBonus(), RADIX);
			} else if (input instanceof WeaponItemEffect) {
				return Integer.toString(input.getType().value(), RADIX) + "," +
					   ((WeaponItemEffect) input).getDice().toString(RADIX);
			}
			return null;
		}
	};
	
	public static Function<String, ItemEffect> STRING_TO_ITEMEFFECT = new Function<String, ItemEffect>() {
		public ItemEffect apply(String input) {
			int index = input.indexOf(',');

			ItemEffectEnum effect = ItemEffectEnum.valueOf(Integer.parseInt(input.substring(0, index), RADIX));
			
			if (!effect.isWeaponEffect()) {
				short bonus = Short.parseShort(input.substring(index + 1), RADIX);
				
				return new ConstantItemEffect(effect, bonus);
			} else {
				Dice dice = Dofus1Dice.parseDice(input.substring(index + 1), RADIX);
				
				return new WeaponItemEffect(effect, dice);
			}
		}
	};
	
	public static Function<Contact, BaseContactType> CONTACT_TO_BASEFRIENDTYPE = new Function<Contact, BaseContactType>() {
		public BaseContactType apply(Contact input) {
			return input.toBaseContactType();
		}
	};
}
