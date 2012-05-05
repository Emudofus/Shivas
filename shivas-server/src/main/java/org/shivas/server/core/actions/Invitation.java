package org.shivas.server.core.actions;

public interface Invitation extends Action {
	void accept() throws ActionException;
	void decline() throws ActionException;
}
