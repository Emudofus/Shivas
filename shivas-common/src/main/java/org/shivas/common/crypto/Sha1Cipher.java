package org.shivas.common.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Cipher implements Cipher {
	
	private static final String ALGORITHM_NAME = "SHA-1";
	
	private MessageDigest md;
	
	public Sha1Cipher() {
		try {
			md = MessageDigest.getInstance(ALGORITHM_NAME);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] cipher(byte[] bytes) throws CipherException {
		return md.digest(bytes);
	}

	public String cipher(String string) throws CipherException {
		return String.format("%1$040x", new BigInteger(1, md.digest(string.getBytes())));
	}

	public String cipherString(byte[] bytes) throws CipherException {
		return String.format("%1$040x", new BigInteger(1, md.digest(bytes)));
	}

}
