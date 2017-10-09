/*
 * Created on 17-mag-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package microdev.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author andrea
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringUtils {
	private static Log log = LogFactory.getLog(StringUtils.class);
	
	public static String to_s(Object ob) {
		return ToStringBuilder.reflectionToString(ob, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public static String inspect(Object ob) {
		return ToStringBuilder.reflectionToString(ob, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public static String removeExtraSpaces(String source) {
		String clean_s = null;
		clean_s = source.trim().replaceAll("\\s+", " ");
		return clean_s;
	}

	public static String escapeHTML(String src) {
		return StringEscapeUtils.escapeHtml(src);
	}
	
	public static String unescapeHTML(String src) {
		return StringEscapeUtils.unescapeHtml(src);
	}
	
	public static String urlDecode(String src) {
		String res = null;
		try {
			res = URLDecoder.decode(src, "UTF-8");
		} catch (Exception e) {
			log.error("", e); //.printStackTrace();
		}
		return res;
	}
	
	public static String urlEncode(String src) throws Exception {
		return URLEncoder.encode(src, "UTF-8");
	}

}
