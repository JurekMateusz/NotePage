package pl.mjurek.notepage.service.encoding;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Encode {
    private static Encode instance;
    private static Cipher cipher;
    private static Key aesKey;
    private static final String key = "Bar12345Bar12345";

    private Encode() {
    }

    public static Encode getInstance() {
        if (instance == null) {
            instance = new Encode();

            aesKey = new SecretKeySpec(key.getBytes(), "AES");
            try {
                cipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
        }
        return new Encode();
    }

    public String encode(String text) {
        String result = "";
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            result = new String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String decode(String encrypted) {
        String result = "";
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            result = new String(cipher.doFinal(encrypted.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
