package org.shivas.server.core.friends;

public class AlreadyAddedException extends Exception {

	private static final long serialVersionUID = 5889798330671967456L;

	public AlreadyAddedException() {
		super();
	}

	public AlreadyAddedException(String message) {
		super(message);
	}

	public AlreadyAddedException(Throwable cause) {
		super(cause);
	}

	public AlreadyAddedException(String message, Throwable cause) {
		super(message, cause);
	}

}
