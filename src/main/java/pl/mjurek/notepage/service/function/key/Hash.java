package pl.mjurek.notepage.service.function.key;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public final class Hash {
  public static String encodePassword(String pass) {
    return DigestUtils.sha256Hex(pass).substring(0, 40);
  }

  public static String getKey() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 20;
    Random random = new Random();

    return random
        .ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
