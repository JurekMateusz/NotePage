package pl.mjurek.notepage.service.function.encoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class NoteContentEncoder {
  private static final String ALGORITHM = "AES";
  private static final String myEncryptionKey = "Bar12345Bar12345";
  private static final String UNICODE_FORMAT = "UTF8";

  public static String encrypt(String valueToEnc) {
    try {
      Key key = generateKey();
      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.ENCRYPT_MODE, key);
      byte[] encValue = c.doFinal(valueToEnc.getBytes());
      return Base64.getMimeEncoder().encodeToString(encValue);
    } catch (NoSuchAlgorithmException
        | InvalidKeyException
        | NoSuchPaddingException
        | BadPaddingException
        | UnsupportedEncodingException
        | IllegalBlockSizeException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String decrypt(String encryptedValue) {
    try {
      Key key = generateKey();
      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.DECRYPT_MODE, key);
      byte[] decodedValue = Base64.getMimeDecoder().decode(encryptedValue);
      byte[] decValue = c.doFinal(decodedValue);
      return new String(decValue);
    } catch (NoSuchAlgorithmException
        | InvalidKeyException
        | NoSuchPaddingException
        | BadPaddingException
        | UnsupportedEncodingException
        | IllegalBlockSizeException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static Key generateKey() throws UnsupportedEncodingException {
    byte[] keyAsBytes;
    keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
    return new SecretKeySpec(keyAsBytes, ALGORITHM);
  }
}
