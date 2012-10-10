package org.shivas.common.crypto;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 10/10/12
 * Time: 11:21
 */
public class Base64DecrypterCipher implements Cipher {
    @Override
    public byte[] cipher(byte[] bytes) throws CipherException {
        try {
            return Base64.decode(bytes);
        } catch (Base64DecodingException e) {
            throw new CipherException(e);
        }
    }

    @Override
    public String cipher(String string) throws CipherException {
        try {
            return new String(Base64.decode(string));
        } catch (Base64DecodingException e) {
            throw new CipherException(e);
        }
    }

    @Override
    public String cipherString(byte[] bytes) throws CipherException {
        try {
            return new String(Base64.decode(bytes));
        } catch (Base64DecodingException e) {
            throw new CipherException(e);
        }
    }
}
