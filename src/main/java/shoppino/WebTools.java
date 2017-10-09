package shoppino;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.json.simple.JSONObject;

import shoppino.service.TenantService;

import com.github.slugify.Slugify;

import openrecordz.util.MapUtil;
import openrecordz.util.ThumbnailUtil;

public class WebTools {
	
//	@Autowired
//	static TenantService tenantService;
	
	public static String escapeJSON(String value) {
		return JSONObject.escape(value);
	}
	
	public static String encodeURL(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value,"UTF-8"); 
	}
	
	public static String safelyEncodeURL(String value) throws UnsupportedEncodingException {
		try {
			if (value!=null) {
//				value=value.replace(" ", "-");
//				value=value.replace("/", "-");	//spring error with encoded /
//				value=value.replace("\\", "-");	//spring error with encoded /
//				value=value.replace("(", "-");	//spring error with encoded (
//				value=value.replace(")", "-");	//spring error with encoded )
				
				Slugify slg = new Slugify();

				value = slg.slugify(value);
			
				if (value.length()>130) //140 is the minimun
					value=value.substring(0,130);
			}
			
//			return URLEncoder.encode(value.toLowerCase(),"UTF-8");
			return value;
		}catch (Exception e) {
			return "";
		}
	}
	
	public static String decodeURL(String value) throws UnsupportedEncodingException {
		return URLDecoder.decode(value,"UTF-8"); 
	}
	
	public static Integer[] fitSize(Integer[] origSize, String destinatioWidthSize, String destinatioHeightSize) throws UnsupportedEncodingException {
		try{
			float[] destSize= new float[2];
	//		destSize[0] = Float.parseFloat(destinatioWidthSize);
	//		destSize[1] = Float.parseFloat(destinatioHeightSize);
			destSize[0] = Float.parseFloat(destinatioWidthSize+"");
			destSize[1] = Float.parseFloat(destinatioHeightSize+"");
			float[] orSize = new float[2];
			orSize[0] = origSize[0];
			orSize[1] = origSize[1];
			int[] result = ThumbnailUtil.fitSize(orSize, destSize);
			Integer[] resInt = new Integer[2];
			resInt[0] = result[0];
			resInt[1] = result[1];
			return resInt;
		}catch (Exception e) {
			return null;
		}
	}
	public static Integer calculateLeft(Integer origWidth, String destinatioWidthSize) throws UnsupportedEncodingException {
		return ( Integer.valueOf(destinatioWidthSize) - origWidth) / 2;
	}
	
	public static String verticalText(String text) throws UnsupportedEncodingException {
		return text.replace("","\n");
	}
	
	public static String capitalize(String text) {
		return WordUtils.capitalize(text);
	}
	public static String capitalizeFirst(String text) {
		return StringUtils.capitalize(text);
	}
	public static String rigenerateQueryString(Map requestParameters, String reqParameterKey, String reqParameterValue) {
//		if (queryString.contains(reqParameterKey))
//			queryString.replaceAll(reqParameterKey, replacement)
//		String queryString="";
//		for (Iterator iterator = requestParameter.entrySet().iterator(); iterator.hasNext();)  {  
//			Map.Entry entry = (Map.Entry) iterator.next();  
//			queryString=queryString+entry.getKey()+"="+entry.getValue();
////			System.out.println("parameter name:"+entry.getKey());  
////			System.out.println("value:"+entry.getValue());  
//		}
		if (requestParameters!=null)
			requestParameters.put(reqParameterKey, reqParameterValue);
		
		try {
//			return MapUtil.mapToString(requestParameters);
			return MapUtil.createQueryStringFromMap(requestParameters, "&").toString();
		}catch (Exception e) {
			return "";
		}
	}
	
	public static Boolean showBlock(Boolean allowBlock, TenantService tenantService) {
//		if (allowBlock && !tenantService.isDefaultTenant() || tenantService.isDefaultTenant())
		if (allowBlock)
			return true;
		else if (!allowBlock && tenantService.isDefaultTenant())
			return true;
		else
			return false;
	}
	
	public static String date2String(Date date, String format) {
		try {
			return Util.date2String(date,format);
		}catch (Exception e) {
			return date.toString();
		}
	}
	
	public static String date2stringbystyle(Date date, String styleDate, String styleTime, Locale locale) {
		try {
			return Util.date2String(date, Integer.parseInt(styleDate), Integer.parseInt(styleTime), locale);
		}catch (Exception e) {
			return date.toString();
		}
	}
	
	public static String truncate(String text, Integer maxCharacters) {
		if (text!=null && text.length() > maxCharacters)
			return text.substring(0, maxCharacters) +  " ...";
		else 
			return text;
	}
	
//	public static String generateURL(Product p) {
//		try {
//			String url = p.getDescription();
//			
//			if (url.length()>100) //140 is the minimun
//				url=url.substring(0,100);
//			
//			if (p.getShopName()!=null && !p.getShopName().equals("")) {
//				url = url + " " + p.getShopName();
//			}
//			
//			if (p.getShopCity()!=null && !p.getShopName().equals("")) {
//				url = url + " " + p.getShopCity();
//			}
//			
//			return safelyEncodeURL(url);
//		} catch (Exception e) {
//			try {
//				return safelyEncodeURL(p.getDescription());
//			} catch (Exception e1) {
//				return "";
//			}
//		}
//	}
	
}
