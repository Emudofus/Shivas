package org.shivas.protocol.server;

import org.apache.mina.core.buffer.IoBuffer;

public interface Message {
	MessageType getMessageType();
	
	void serialize(IoBuffer buf);
	void deserialize(IoBuffer buf);
}
