package org.shivas.protocol.server.codec;

import org.shivas.protocol.server.DefaultMessageFactory;
import org.shivas.protocol.server.MessageFactory;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MamboProtocolCodecFactory implements ProtocolCodecFactory {
	private final MessageFactory factory;
	private final MamboProtocolDecoder decoder;
	private final MamboProtocolEncoder encoder;
	
	public MamboProtocolCodecFactory() {
		this.factory = new DefaultMessageFactory();
		this.decoder = new MamboProtocolDecoder(factory);
		this.encoder = new MamboProtocolEncoder();
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}

}
