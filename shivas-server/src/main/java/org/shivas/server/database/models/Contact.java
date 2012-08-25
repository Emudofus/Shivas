package org.shivas.server.database.models;

import org.atomium.PersistableEntity;
import org.shivas.protocol.client.enums.ContactStateEnum;
import org.shivas.protocol.client.types.BaseContactType;

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
	
	public boolean isReciprocal() {
		return target.getContacts().hasContact(owner);
	}
	
	public BaseContactType toBaseFriendType() {
		if (target.isConnected()) {
			Player player = target.getCurrentPlayer();
			
			return new BaseContactType(
					target.getNickname(),
					true, // is connected
					isReciprocal(),
					player.getName(),
					(short) player.getExperience().level(),
					(short) 0, // TODO alignment
					(byte) player.getBreed().getId(),
					player.getGender(),
					player.getLook().skin(),
					ContactStateEnum.SOLO
			);
		} else {
			return new BaseContactType(
					target.getNickname(),
					false,
					isReciprocal(),
					ContactStateEnum.UNKNOWN
			);
		}
	}

}
