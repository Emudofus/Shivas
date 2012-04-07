package org.shivas.common.crypto;

public class CipherException extends Exception {
	private static final long serialVersionUID = -6353580169221904200L;

	public CipherException(Exception e) {
		super(e);
	}
	
	public CipherException(String message) {
		super(message);
	}
}
