package org.shivas.server.services;

public interface ServiceListener<C extends Client<?>> {
	void connected(C client);
	void disconnected(C client);
}
