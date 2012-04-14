package org.shivas.protocol.server;

public enum MessageType {
	NIL(0),
	HELLO_CONNECT(1), 
	SERVER_STATUS_UPDATE(2), 
	ACCOUNT_CHARACTERS_REQUEST(3), 
	ACCOUNT_CHARACTERS(4), 
	CLIENT_CONNECTION(5), 
	CLIENT_DECONNECTION(6);
	
	int value;
	MessageType(int value) { this.value = value; }
	public int value() { return value; }
	public static MessageType valueOf(int value) {
		for (MessageType v : values())
			if (v.value == value) return v;
		return null;
	}
}
