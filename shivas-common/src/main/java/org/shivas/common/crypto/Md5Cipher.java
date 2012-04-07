package org.shivas.common.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Cipher implements Cipher {
	
	private static final String ALGORITHM_NAME = "MD5";
	
	private MessageDigest md;
	
	public Md5Cipher() throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance(ALGORITHM_NAME);
	}

	public byte[] cipher(byte[] bytes) throws CipherException {
		return md.digest(bytes);
	}

	public String cipher(String string) throws CipherException {
		return String.format("%1$032x", new BigInteger(1, md.digest(string.getBytes())));
	}

	public String cipherString(byte[] bytes) throws CipherException {
		return String.format("%1$032x", new BigInteger(1, md.digest(bytes)));
	}

}
