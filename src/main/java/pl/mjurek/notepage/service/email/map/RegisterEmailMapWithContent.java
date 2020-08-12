package pl.mjurek.notepage.service.email.map;

import pl.mjurek.notepage.service.function.url.UrlProviderToEmail;

import java.util.HashMap;
import java.util.Map;

public class RegisterEmailMapWithContent {
  public static Map<String, String> getMap(String email, StringBuffer patch, String activateKey) {
    String url = UrlProviderToEmail.provideURL(patch);
    String subject = "Registration link";
    String content = url + "/verification?key=" + activateKey;
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put("email", email);
    resultMap.put("subject", subject);
    resultMap.put("content", content);
    return resultMap;
  }
}
