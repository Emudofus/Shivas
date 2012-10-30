package org.shivas.core.core.services;

import java.util.Set;

import com.google.common.collect.Sets;

public class NetworkStatisticsCenter {

	public static class Entry implements Comparable<Entry> {
		public String packet;
		public long delta;
		
		@Override
		public int compareTo(Entry other) {
			return (int) (this.delta - other.delta);
		}
	}
	
	public static class Chrono {
		private final NetworkStatisticsCenter stats;
		private final String packet;
		private final long start;
		
		private long delta;
		
		public Chrono(NetworkStatisticsCenter stats, String packet) {
			this.stats = stats;
			this.packet = packet;
			this.start = System.currentTimeMillis();
		}
		
		public long getDelta() {
			return delta;
		}
		
		public void end() {
			delta = System.currentTimeMillis() - start;
			stats.put(packet, delta);
		}
	}
	
	private Set<Entry> entries = Sets.newTreeSet();
	
	public Chrono start(String packet) {
		return new Chrono(this, packet);
	}
	
	public void put(String packet, long delta) {
		Entry entry = new Entry();
		entry.packet = packet;
		entry.delta = delta;
		
		entries.add(entry);
	}
	
	public void clear() {
		entries.clear();
	}
	
	public long get(String header) {
		long sum = 0;
		int nb = 0;
		
		for (Entry entry : entries) {
			if (entry.packet.startsWith(header)) {
				sum += entry.delta;
				++nb;
			}
		}
		
		return nb == 0 ? 0 : sum / nb;
	}
	
}
