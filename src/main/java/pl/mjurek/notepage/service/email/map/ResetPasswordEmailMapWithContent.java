package pl.mjurek.notepage.service.email.map;

import pl.mjurek.notepage.service.function.url.UrlProviderToEmail;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordEmailMapWithContent {
  public static Map<String, String> getMap(String email, StringBuffer patch, String key) {
    String subject = "Reset password";
    String readyPatch = UrlProviderToEmail.provideURL(patch);
    String content =
        "If you want reset password click link bellow:\n "
            + readyPatch
            + "/reset_password?key="
            + key;
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put("subject", subject);
    resultMap.put("email", email);
    resultMap.put("content", content);
    return resultMap;
  }
}
