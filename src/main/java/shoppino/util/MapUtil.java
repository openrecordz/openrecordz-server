package shoppino.util;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapUtil {
 public static String mapToString(Map<String, String> map) {
   StringBuilder stringBuilder = new StringBuilder();

   for (String key : map.keySet()) {
    if (stringBuilder.length() > 0) {
     stringBuilder.append("&");
    }
    Object value = map.get(key);
    try {
     stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
     stringBuilder.append("=");
     stringBuilder.append(value != null ? URLEncoder.encode(String.valueOf(value), "UTF-8") : "");
    } catch (UnsupportedEncodingException e) {
     throw new RuntimeException("This method requires UTF-8 encoding support", e);
    }
   }

   return stringBuilder.toString();
  }

  public static Map<String, String> stringToMap(String input) {
   Map<String, String> map = new HashMap<String, String>();

   String[] nameValuePairs = input.split("&");
   for (String nameValuePair : nameValuePairs) {
    String[] nameValue = nameValuePair.split("=");
    try {
     map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
     nameValue[1], "UTF-8") : "");
    } catch (UnsupportedEncodingException e) {
     throw new RuntimeException("This method requires UTF-8 encoding support", e);
    }
   }

   return map;
  }
  
  public static StringBuffer createQueryStringFromMap(Map m, String ampersand) {
      StringBuffer aReturn = new StringBuffer("");
      Set aEntryS = m.entrySet();
      Iterator aEntryI = aEntryS.iterator();

      while (aEntryI.hasNext()) {
          Map.Entry aEntry = (Map.Entry) aEntryI.next();
          Object o = aEntry.getValue();

          if (o == null) {
              append(aEntry.getKey(), "", aReturn, ampersand);
          } else if (o instanceof String) {
              append(aEntry.getKey(), o, aReturn, ampersand);
          } else if (o instanceof String[]) {
              String[] aValues = (String[]) o;

              for (int i = 0; i < aValues.length; i++) {
                  append(aEntry.getKey(), aValues[i], aReturn, ampersand);
              }
          } else {
              append(aEntry.getKey(), o, aReturn, ampersand);
          }
      }

      return aReturn;
  }
  /**
   * Appends new key and value pair to query string
   *
   * @param key parameter name
   * @param value value of parameter
   * @param queryString existing query string
   * @param ampersand string to use for ampersand (e.g. "&" or "&amp;")
   *
   * @return query string (with no leading "?")
   */
  private static StringBuffer append(Object key, Object value,
                                     StringBuffer queryString,
                                     String ampersand) {
      if (queryString.length() > 0) {
          queryString.append(ampersand);
      }

      // Use encodeURL from Struts' RequestUtils class - it's JDK 1.3 and 1.4 compliant
//      queryString.append(RequestUtils.encodeURL(key.toString()));
      queryString.append(key.toString());
      queryString.append("=");
//      queryString.append(RequestUtils.encodeURL(value.toString()));
      queryString.append(value.toString());

      return queryString;
  }
}

