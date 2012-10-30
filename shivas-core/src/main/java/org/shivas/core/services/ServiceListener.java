package org.shivas.core.services;

public interface ServiceListener<C extends Client<?>> {
	void connected(C client);
	void disconnected(C client);
}
