package org.shivas.server.database.models;

import org.atomium.EntityReference;
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
	private EntityReference<Integer, Account> owner, target;
	private Type type;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long pk) {
		this.id = pk.longValue();
	}

	public EntityReference<Integer, Account> getOwnerReference() {
		return owner;
	}
	
	public Account getOwner() {
		return owner.get();
	}

	public void setOwnerReference(EntityReference<Integer, Account> owner) {
		this.owner = owner;
	}
	
	public void setOwner(Account owner) {
		this.owner.set(owner);
	}

	public EntityReference<Integer, Account> getTargetReference() {
		return target;
	}
	
	public Account getTarget() {
		return target.get();
	}

	public void setTargetReference(EntityReference<Integer, Account> target) {
		this.target = target;
	}
	
	public void setTarget(Account target) {
		this.target.set(target);
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
