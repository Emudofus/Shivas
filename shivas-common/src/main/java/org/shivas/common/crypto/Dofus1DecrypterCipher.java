package org.shivas.common.crypto;

import org.shivas.common.StringUtils;

public class Dofus1DecrypterCipher implements Cipher {
	
	private String key;
	
	public Dofus1DecrypterCipher(String key) {
		this.key = key;
	}

	public byte[] cipher(byte[] bytes) throws CipherException {
		return null;
	}

	public String cipher(String string) throws CipherException {
        StringBuilder decrypted = new StringBuilder(string.length() / 2);
		int length = StringUtils.EXTENDED_ALPHABET.length();
		
		int PKey, ANB, ANB2, somme1, somme2, APass, AKey;
        
        for (int i = 0; i < string.length(); i+=2)
        {
        	PKey = key.charAt(i/2);
			ANB = StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(i));
			ANB2 = StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(i+1));
			
			somme1 = ANB + length;
			somme2 = ANB2 + length;
			
			APass = somme1 - PKey;
			if(APass < 0)APass += 64;
			APass = APass << 4;
			
			AKey = somme2 - PKey;
			if(AKey < 0)AKey += 64;
			
			char PPass = (char)(APass + AKey);
			
			decrypted.append(PPass);
		}
        
		return decrypted.toString();
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
