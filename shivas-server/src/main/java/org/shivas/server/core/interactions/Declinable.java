package org.shivas.server.core.interactions;

public interface Declinable extends Interaction {
	void decline() throws InteractionException;
}
