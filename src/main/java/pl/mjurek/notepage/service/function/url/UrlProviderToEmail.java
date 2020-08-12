package pl.mjurek.notepage.service.function.url;

public class UrlProviderToEmail {
  public static String provideURL(StringBuffer stringBuffer) {
    int lastIndex = stringBuffer.lastIndexOf("/");
    String result = String.valueOf(stringBuffer);
    result = result.substring(0, lastIndex);
    return result;
  }
}
