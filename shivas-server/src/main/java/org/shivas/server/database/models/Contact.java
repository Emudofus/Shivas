package org.shivas.server.database.models;

import org.atomium.PersistableEntity;
import org.shivas.protocol.client.types.BaseFriendType;

public class Contact implements PersistableEntity<Long> {
	
	public static enum Type {
		FRIEND, ENNEMY;
		public static Type valueOf(int integer) {
			for (Type value : values()) if (value.ordinal() == integer) {
				return value;
			}
			return null;
		}
	}
	
	private long id;
	private Account owner, target;
	private Type type;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long pk) {
		this.id = pk.longValue();
	}
	
	public Account getOwner() {
		return owner;
	}
	
	public void setOwner(Account owner) {
		this.owner = owner;
	}
	
	public Account getTarget() {
		return target;
	}
	
	public void setTarget(Account target) {
		this.target = target;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public BaseFriendType toBaseFriendType() {
		return new BaseFriendType(); // TODO friends
	}

}
