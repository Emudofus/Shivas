package org.shivas.common.crypto;

public class Ciphers {
	private Ciphers(){}
	
	public static Cipher combine(final Cipher c1, final Cipher c2) {
		return new Cipher() {
			public String cipherString(byte[] bytes) throws CipherException {
				return c2.cipherString(c1.cipherString(bytes).getBytes());
			}
			
			public String cipher(String string) throws CipherException {
				return c2.cipher(c1.cipher(string));
			}
			
			public byte[] cipher(byte[] bytes) throws CipherException {
				return c2.cipher(c1.cipher(bytes));
			}
		};
	}
}
