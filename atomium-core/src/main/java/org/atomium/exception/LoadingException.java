package org.atomium.exception;

public class LoadingException extends Exception {

	private static final long serialVersionUID = -4788343960178894531L;

	public LoadingException() {
	}

	public LoadingException(String message) {
		super(message);
	}

	public LoadingException(Throwable cause) {
		super(cause);
	}

	public LoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
