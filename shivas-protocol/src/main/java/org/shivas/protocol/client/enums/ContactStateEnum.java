package org.shivas.protocol.client.enums;

public enum ContactStateEnum {
	UNKNOWN(-1),
	SOLO(1),
	MULTI(2);
	
	int value;
	public int value() { return value; }
	private ContactStateEnum(int value) {
		this.value = value;
	}
	
	public static ContactStateEnum valueOf(int value) {
		for (ContactStateEnum v : values()) {
			if (v.value == value) return v;
		}
		return null;
	}
}
