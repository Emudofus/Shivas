package org.shivas.common.crypto;

public class Sha1SaltCipher extends Sha1Cipher {
	
	private static byte[] concat(byte[] first, byte[] second) {
		int i = 0, nb = first.length + second.length;
		byte[] result = new byte[nb];
		for (; i < first.length; ++i) {
			result[i] = first[i];
		}
		for (; i < nb; ++i) {
			result[i] = second[i - first.length];
		}
		return result;
	}
	
	private String salt;
	private byte[] bytes;

	public Sha1SaltCipher(String salt) {
		super();
		
		this.salt = salt;
		this.bytes = salt.getBytes();
	}

	@Override
	public byte[] cipher(byte[] bytes) throws CipherException {
		byte[] first = super.cipherString(bytes).getBytes();
		return super.cipher(concat(first, this.bytes));
	}

	@Override
	public String cipher(String string) throws CipherException {
		String first = super.cipher(string);
		return super.cipher(first + salt);
	}

	@Override
	public String cipherString(byte[] bytes) throws CipherException {
		byte[] first = super.cipherString(bytes).getBytes();
		return super.cipherString(concat(first, this.bytes));
	}

}
