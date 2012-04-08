package org.shivas.protocol.server.codec;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageFactory;
import org.shivas.protocol.server.MessageType;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MamboProtocolDecoder extends CumulativeProtocolDecoder {
	
	private final MessageFactory factory;
	
	public MamboProtocolDecoder(MessageFactory factory) {
		this.factory = factory;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buf, ProtocolDecoderOutput out) 
			throws Exception 
	{
		if (buf.remaining() < 8) {
			return false;
		}
		
		MessageType type = buf.getEnumInt(MessageType.class);
		int length = buf.getInt();
		
		if (buf.remaining() < length) {
			return false;
		}
		
		Message message = factory.get(type);
		message.deserialize(buf);
		
		out.write(message);
		
		return true;
	}

}
