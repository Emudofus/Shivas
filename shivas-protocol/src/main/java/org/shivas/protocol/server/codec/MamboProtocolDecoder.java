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
		if (buf.remaining() < 4) {
			return false;
		}
		
		MessageType type = MessageType.valueOf(buf.getShort());
		int length = buf.getShort();
		
		if (buf.remaining() < length || type == MessageType.NIL) {
			return false;
		}
		
		Message message = factory.get(type);
		if (message != null) {
			message.deserialize(buf);
			
			out.write(message);
		}
		
		return true;
	}

}
