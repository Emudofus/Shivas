package org.shivas.server.core.channels;

import java.util.ArrayList;

import org.shivas.protocol.client.enums.ChannelEnum;

public class ChannelList extends ArrayList<ChannelEnum> {
	
	public static ChannelList parseChannelList(String string) {
		ChannelList channels = new ChannelList();
		for (int i = 0; i < string.length(); ++i) {
			ChannelEnum channel = ChannelEnum.valueOf(string.charAt(i));
			channels.add(channel);
		}
		return channels;
	}

	private static final long serialVersionUID = 2266000186378121022L;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); ++i) {
			sb.append(get(i).value());
		}
		return sb.toString();
	}
	
}
