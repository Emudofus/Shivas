package org.shivas.common.crypto;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 10/10/12
 * Time: 11:24
 */
public class Base64EncrypterCipher implements Cipher {
    @Override
    public byte[] cipher(byte[] bytes) throws CipherException {
        return Base64.encode(bytes).getBytes();
    }

    @Override
    public String cipher(String string) throws CipherException {
        return Base64.encode(string.getBytes());
    }

    @Override
    public String cipherString(byte[] bytes) throws CipherException {
        return Base64.encode(bytes);
    }
}
