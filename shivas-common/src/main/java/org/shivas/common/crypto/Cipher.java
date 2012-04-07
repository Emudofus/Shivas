package org.shivas.common.crypto;

public interface Cipher {
	byte[] cipher(byte[] bytes) throws CipherException;
	String cipher(String string) throws CipherException;
	String cipherString(byte[] bytes) throws CipherException;
}
