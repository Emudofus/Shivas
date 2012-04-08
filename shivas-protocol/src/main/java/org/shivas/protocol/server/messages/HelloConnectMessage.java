package org.shivas.protocol.server.messages;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class HelloConnectMessage implements Message {

	public MessageType getMessageType() {
		return MessageType.HELLO_CONNECT;
	}

	public void serialize(IoBuffer buf) {
	}

	public void deserialize(IoBuffer buf) {
	}

}
