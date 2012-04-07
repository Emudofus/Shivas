package org.shivas.common.crypto;

public abstract class CipherAdapter implements Cipher {

	public String cipher(String string) throws CipherException {
		return new String(cipher(string.getBytes()));
	}

	public String cipherString(byte[] bytes) throws CipherException {
		return new String(cipher(bytes));
	}

}
