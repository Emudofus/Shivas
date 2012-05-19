package org.shivas.server.core.channels;

import java.util.HashMap;

import javax.inject.Singleton;

import org.shivas.protocol.client.enums.ChannelEnum;

@Singleton
public class ChannelContainer extends HashMap<ChannelEnum, Channel> {

	private static final long serialVersionUID = 7308322591902221064L;
	
	public ChannelContainer() {
		put(ChannelEnum.General, new GeneralChannel());
		put(ChannelEnum.Trade, new PublicChannel(ChannelEnum.Trade));
	}

}
