package org.shivas.common.crypto;

public class PlainTextCipher implements Cipher {

	public byte[] cipher(byte[] bytes) throws CipherException {
		return bytes;
	}

	public String cipher(String string) throws CipherException {
		return string;
	}

	public String cipherString(byte[] bytes) throws CipherException {
		return new String(bytes);
	}

}
