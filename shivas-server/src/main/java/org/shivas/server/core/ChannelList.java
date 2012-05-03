package org.shivas.server.core;

import java.util.ArrayList;

import org.shivas.protocol.client.enums.Channel;

public class ChannelList extends ArrayList<Channel> {
	
	public static ChannelList parseChannelList(String string) {
		ChannelList channels = new ChannelList();
		for (int i = 0; i < string.length(); ++i) {
			Channel channel = Channel.valueOf(string.charAt(0));
			channels.add(channel);
		}
		return channels;
	}

	private static final long serialVersionUID = 2266000186378121022L;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); ++i) {
			sb.append(get(i).toChar());
		}
		return sb.toString();
	}
	
}
