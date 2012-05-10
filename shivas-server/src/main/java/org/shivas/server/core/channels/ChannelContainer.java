package org.shivas.server.core.channels;

import java.util.HashMap;

import javax.inject.Singleton;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.core.events.EventDispatcher;

import com.google.inject.Provider;

@Singleton
public class ChannelContainer extends HashMap<ChannelEnum, Channel> {

	private static final long serialVersionUID = 7308322591902221064L;
	
	private Provider<EventDispatcher> eventDispatchers;
	
	public ChannelContainer() {
		put(ChannelEnum.General, new GeneralChannel());
		put(ChannelEnum.Trade, new PublicChannel(ChannelEnum.Trade, eventDispatchers.get()));
	}

}
