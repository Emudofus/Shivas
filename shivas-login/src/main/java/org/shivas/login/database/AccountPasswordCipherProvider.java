package org.shivas.login.database;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Md5Cipher;
import org.shivas.common.crypto.PlainTextCipher;
import org.shivas.common.crypto.Sha1Cipher;
import org.shivas.login.config.LoginConfig;

import com.google.inject.Provider;

@Singleton
public class AccountPasswordCipherProvider implements Provider<Cipher> {
	
	private Cipher cipher;
	
	@Inject
	public AccountPasswordCipherProvider(LoginConfig config) throws NoSuchAlgorithmException {
		if (config.getAccountPasswordCipherName().equals("SHA-1")) {
			cipher = new Sha1Cipher();
		} else if (config.getAccountPasswordCipherName().equals("MD5")) {
			cipher = new Md5Cipher();
		} else if (config.getAccountPasswordCipherName().equals("PLAIN")) {
			cipher = new PlainTextCipher();
		} else {
			throw new NoSuchAlgorithmException(config.getAccountPasswordCipherName());
		}
	}

	public Cipher get() {
		return cipher;
	}

}
