package org.shivas.protocol.server.codec;

import org.shivas.protocol.server.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MamboProtocolEncoder implements ProtocolEncoder {
	public static final int DEFAULT_CAPACITY = 1024;

	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
			throws Exception 
	{
		if (!(obj instanceof Message)) {
			throw new Exception("I can only encode Message");
		}
		
		Message message = (Message) obj;
		
		IoBuffer buf = IoBuffer.allocate(DEFAULT_CAPACITY);
		buf.position(8); // ID (int, 4 bytes) + LEN (int, 4 bytes) = 8 bytes
		message.serialize(buf);
		int length = buf.position() - 8;
		buf.position(0);
		buf.putEnumInt(message.getMessageType());
		buf.putInt(length);
		
		out.write(buf);
	}

	public void dispose(IoSession arg0) throws Exception {
	}
}
