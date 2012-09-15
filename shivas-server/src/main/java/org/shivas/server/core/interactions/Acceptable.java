package org.shivas.server.core.interactions;

public interface Acceptable extends Interaction {
	void accept() throws InteractionException;
}
