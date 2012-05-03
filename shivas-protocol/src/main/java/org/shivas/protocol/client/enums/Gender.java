package org.shivas.protocol.client.enums;

public enum Gender {
	MALE,
	FEMALE;
	
	public static Gender valueOf(int ordinal) {
		for (Gender gender : values()) {
			if (gender.ordinal() == ordinal) return gender;
		}
		return null;
	}
}
