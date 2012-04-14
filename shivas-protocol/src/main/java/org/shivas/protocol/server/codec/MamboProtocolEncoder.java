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

		IoBuffer buf1 = IoBuffer.allocate(DEFAULT_CAPACITY);
		buf1.setAutoExpand(true);
		
		message.serialize(buf1);
		
		int length = buf1.position();
		
		buf1.flip();
		
		IoBuffer buf = IoBuffer.allocate(4 + length);
		buf.putShort((short) message.getMessageType().value());
		buf.putShort((short)length);
		buf.put(buf1);
		
		buf.flip();
		
		out.write(buf);
	}

	public void dispose(IoSession arg0) throws Exception {
	}
}
