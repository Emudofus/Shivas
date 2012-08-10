package org.shivas.server.utils;

import org.shivas.server.database.models.Contact;
import org.shivas.server.database.models.GameItem;

import com.google.common.base.Predicate;

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
}
