package org.shivas.server.utils;

import com.google.common.base.Function;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.data.entity.NpcAnswer;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.*;
import org.shivas.server.core.GameActor;
import org.shivas.server.database.models.*;

public class Converters {
    public static final Function<NpcAnswer,Integer> NPCANSWER_TO_ID = new Function<NpcAnswer, Integer>() {
        @Override
        public Integer apply(NpcAnswer input) {
            return input.getId();
        }
    };

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

	public static final Function<Player, BasePartyMemberType> PLAYER_TO_BASEPARTYMEMBERTYPE = new Function<Player, BasePartyMemberType>() {
		public BasePartyMemberType apply(Player input) {
			return input.toBasePartyMemberType();
		}
	};
	
	public static Function<GameActor, BaseRolePlayActorType> GAMEACTOR_TO_BASEROLEPLAYACTORTYPE = new Function<GameActor, BaseRolePlayActorType>() {
		public BaseRolePlayActorType apply(GameActor arg0) {
			return arg0.toBaseRolePlayActorType();
		}
	};
	
	public static Function<ItemEffect, BaseItemEffectType> ITEMEFFECT_TO_BASEITEMEFFECTTYPE = new Function<ItemEffect, BaseItemEffectType>() {
		public BaseItemEffectType apply(ItemEffect arg0) {
			return arg0.toBaseItemEffectType();
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
		    return input.toString(RADIX);
		}
	};
	
	public static Function<String, ItemEffect> STRING_TO_ITEMEFFECT = new Function<String, ItemEffect>() {
		public ItemEffect apply(String input) {
            return ItemEffect.valueOf(input, RADIX);
		}
	};
	
	public static Function<Contact, BaseContactType> CONTACT_TO_BASEFRIENDTYPE = new Function<Contact, BaseContactType>() {
		public BaseContactType apply(Contact input) {
			return input.toBaseContactType();
		}
	};
	
	public static Function<Gift, BaseGiftType> GIFT_TO_BASEGIFTTYPE = new Function<Gift, BaseGiftType>() {
		public BaseGiftType apply(Gift input) {
			return input.toBaseGiftType();
		}
	};
	
	public static Function<Gift, Long> GIFT_TO_ID = new Function<Gift, Long>() {
		public Long apply(Gift input) {
			return input.getId();
		}
	};

    public static Function<StoredItem, StoreItemType> STOREDITEM_TO_STOREITEMTYPE = new Function<StoredItem, StoreItemType>() {
        public StoreItemType apply(StoredItem input) {
            return input.toStoreItemType();
        }
    };

    public static Function<Player, BaseGuildMemberType> PLAYER_TO_BASEGUILDMEMBERTYPE = new Function<Player, BaseGuildMemberType>() {
        public BaseGuildMemberType apply(Player input) {
            return input.getGuildMember().toBaseGuildMemberType();
        }
    };

    public static Function<GuildMember, BaseGuildMemberType> GUILDMEMBER_TO_BASEGUILDMEMBERTYPE = new Function<GuildMember, BaseGuildMemberType>() {
        public BaseGuildMemberType apply(GuildMember input) {
            return input.toBaseGuildMemberType();
        }
    };
}
