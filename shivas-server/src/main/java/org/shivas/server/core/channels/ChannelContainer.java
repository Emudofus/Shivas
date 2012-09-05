package org.shivas.server.core.channels;

import java.util.Collection;
import java.util.HashMap;

import javax.inject.Singleton;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.core.events.EventListener;

@Singleton
public class ChannelContainer extends HashMap<ChannelEnum, Channel> {

	private static final long serialVersionUID = 7308322591902221064L;
	
	private final Channel system;
	
	public ChannelContainer() {
		put(ChannelEnum.General, new GeneralChannel());
		put(ChannelEnum.Trade, new PublicChannel(ChannelEnum.Trade));
		put(ChannelEnum.Recruitment, new PublicChannel(ChannelEnum.Recruitment));
		put(ChannelEnum.Admin, new PublicChannel(ChannelEnum.Admin));
		
		system = new SystemChannel("(Global) %2$s");
	}
	
	public void subscribeAll(Collection<ChannelEnum> channels, EventListener listener){
		system.subscribe(listener);
		
		for (ChannelEnum c : channels){
			Channel channel = get(c);
			if (channel != null){
				channel.subscribe(listener);
			}
		}
	}
	
	public void unsubscribeAll(Collection<ChannelEnum> channels, EventListener listener){
		system.unsubscribe(listener);
		
		for (ChannelEnum c : channels){
			Channel channel = get(c);
			if (channel != null){
				channel.unsubscribe(listener);
			}
		}
	}
	
	public Channel system() {
		return system;
	}

}
