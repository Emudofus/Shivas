package org.shivas.server.services;

public class CriticalException extends Exception {

	private static final long serialVersionUID = -4403759598755996325L;

	public CriticalException() {
	}
	
	public CriticalException(String message, Object... args) {
		super(String.format(message, args));
	}

	public CriticalException(String message, Throwable cause) {
		super(message, cause);
	}

	public CriticalException(String message) {
		super(message);
	}

	public CriticalException(Throwable cause) {
		super(cause);
	}
	
}
