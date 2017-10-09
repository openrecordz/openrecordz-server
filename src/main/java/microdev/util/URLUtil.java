package microdev.util;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class URLUtil {
	
	private static Log log = LogFactory.getLog(URLUtil.class);

	public static void main(String[] args) {
		try {
			Map map = getUrlMap("http://pippo.it/nino?a=2&c=3&k=dddd&d=1");
			dumpMap(map);
			//System.out.println("name is " + parmsMap.get("name"));
		}
		catch (UnsupportedEncodingException e) {
			log.error("", e); //.printStackTrace();
		}
	}

	public static Map getUrlMap(String completeURL) throws UnsupportedEncodingException {
		System.out.println("Complete URL: " + completeURL);
		int i = completeURL.indexOf("?");
		if (i > -1) {
			String searchURL = completeURL.substring(completeURL.indexOf("?") + 1);
			System.out.println("Search URL: " + searchURL);
			return buildMap(searchURL);
		}
		return new HashMap(); // returns an empty map
	}

	private static Map buildMap(String search) throws UnsupportedEncodingException {
		Map parmsMap = new HashMap<String,String>();
		String params[] = search.split("&");

		for (String param : params) {
			String temp[] = param.split("=");
			parmsMap.put(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
		}
		return parmsMap;
	}

	private static void dumpMap(Map<?,?> map) {
		System.out.println("--------");
		for (Map.Entry<?,?> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("--------");
	}


}
