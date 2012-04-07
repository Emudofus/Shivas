package org.shivas.common.crypto;

import org.shivas.common.StringUtils;

public class Dofus1EncrypterCipher implements Cipher {
	
	private String key;

	public Dofus1EncrypterCipher(String key) {
		this.key = key;
	}

	public byte[] cipher(byte[] bytes) throws CipherException {
		return null;
	}

	public String cipher(String string) throws CipherException {
        StringBuilder _Crypted = new StringBuilder(string.length() * 2);
        
        int j = StringUtils.EXTENDED_ALPHABET.length();

        for (int i = 0; i < string.length(); i++)
        {
            int PPass = string.charAt(i);
            int PKey = key.charAt(i);

            int APass = PPass >> 4;

            int AKey = PPass % 16;

			int ANB = (APass + PKey) % j;
            int ANB2 = (AKey + PKey) % j;

            _Crypted.append(StringUtils.EXTENDED_ALPHABET.charAt(ANB));
            _Crypted.append(StringUtils.EXTENDED_ALPHABET.charAt(ANB2));
        }
        return _Crypted.toString();
	}

	public String cipherString(byte[] bytes) throws CipherException {
		return null;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
}
