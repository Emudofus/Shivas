package org.shivas.protocol.server;

public interface MessageFactory {
	Message get(MessageType type);
}
