package org.shivas.protocol.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.shivas.protocol.server.messages.AccountCharactersMessage;
import org.shivas.protocol.server.messages.AccountCharactersRequestMessage;
import org.shivas.protocol.server.messages.ClientConnectionMessage;
import org.shivas.protocol.server.messages.ClientDeconnectionMessage;
import org.shivas.protocol.server.messages.HelloConnectMessage;
import org.shivas.protocol.server.messages.ServerStatusUpdateMessage;

public class DefaultMessageFactory implements MessageFactory {
	
	static interface MessageMaker {
		Message make();
	}
	
	private Map<MessageType, MessageMaker> makers;

	public Message get(MessageType type) {
		MessageMaker maker = makers.get(type);
		return maker != null ? maker.make() : null;
	}
	
	public DefaultMessageFactory() {
		makers = new HashMap<MessageType, MessageMaker>();
		
		// add messages
		makers.put(MessageType.HELLO_CONNECT, new MessageMaker() {
			public Message make() {
				return new HelloConnectMessage();
			}
		});
		makers.put(MessageType.SERVER_STATUS_UPDATE, new MessageMaker() {
			public Message make() {
				return new ServerStatusUpdateMessage();
			}
		});
		makers.put(MessageType.ACCOUNT_CHARACTERS_REQUEST, new MessageMaker() {
			public Message make() {
				return new AccountCharactersRequestMessage();
			}
		});
		makers.put(MessageType.ACCOUNT_CHARACTERS, new MessageMaker() {
			public Message make() {
				return new AccountCharactersMessage();
			}
		});
		makers.put(MessageType.CLIENT_CONNECTION, new MessageMaker() {
			public Message make() {
				return new ClientConnectionMessage();
			}
		});
		makers.put(MessageType.CLIENT_DECONNECTION, new MessageMaker() {
			public Message make() {
				return new ClientDeconnectionMessage();
			}
		});
		
		makers = Collections.unmodifiableMap(makers);
	}
}
