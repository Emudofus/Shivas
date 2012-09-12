package org.shivas.server.core.interactions;

public class InteractionException extends Exception {

	private static final long serialVersionUID = -2019222601772278284L;

	public InteractionException() {
		super();
	}

	public InteractionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InteractionException(String arg0) {
		super(arg0);
	}

	public InteractionException(Throwable arg0) {
		super(arg0);
	}

}
