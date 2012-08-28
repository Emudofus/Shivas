package org.shivas.server.core.interactions;

public interface Invitation extends Action {
	void accept() throws ActionException;
	void decline() throws ActionException;
}
