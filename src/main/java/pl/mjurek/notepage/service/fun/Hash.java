package pl.mjurek.notepage.service.fun;

import org.apache.commons.codec.digest.DigestUtils;

public class Hash {
    public static String encodePassword(String pass) {
        return DigestUtils.sha1Hex(pass).substring(0, 40);
    }

    public static String getKey() {
        String key = String.valueOf((int) System.currentTimeMillis() % 3333);
        key = DigestUtils.md5Hex(key);
        return key.substring(0, 20);
    }
}
