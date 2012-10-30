package org.shivas.core.services;

import java.util.List;

import com.google.common.collect.Lists;

public class ServiceListenerContainer<C extends Client<?>> implements ServiceListener<C> {
	
	private final List<ServiceListener<C>> listeners = Lists.newArrayList();
	
	public void addListener(ServiceListener<C> listener) {
		listeners.add(listener);
	}

	@Override
	public void connected(C client) {
		for (ServiceListener<C> listener : listeners) {
			listener.connected(client);
		}
	}

	@Override
	public void disconnected(C client) {
		for (ServiceListener<C> listener : listeners) {
			listener.disconnected(client);
		}
	}

}
