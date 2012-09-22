package org.shivas.server.utils;

import com.google.common.base.Predicate;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.stores.PlayerStore;
import org.shivas.server.database.models.Contact;
import org.shivas.server.database.models.GameItem;

public final class Filters {
	private Filters() {}
	
	public static Predicate<GameItem> EQUIPED_ITEM_FILTER = new Predicate<GameItem>() {
		public boolean apply(GameItem input) {
			return input == null ?
						false :
						input.getPosition().equipment();
		}
	};
	
	public static Predicate<Contact> FRIEND_CONTACT_FILTER = new Predicate<Contact>() {
		public boolean apply(Contact input) {
			return input == null ?
						false :
						input.getType() == Contact.Type.FRIEND;
		}
	};
	
	public static Predicate<Contact> ENNEMY_CONTACT_FILTER = new Predicate<Contact>() {
		public boolean apply(Contact input) {
			return input == null ?
						false :
						input.getType() == Contact.Type.ENNEMY;
		}
	};

    public static Predicate<GameActor> STOREACTOR_FILTER = new Predicate<GameActor>() {
        public boolean apply(GameActor input) {
            return input instanceof PlayerStore;
        }
    };
}
