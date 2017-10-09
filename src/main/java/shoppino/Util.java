package shoppino;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
	
	public static String dateRFC822(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(
		        "EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		return sdf.format(date);
	}
	
	public static String date2String(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(
		        format);
		return sdf.format(date);
	}
	
	public static String date2String(Date date, int styleDate, int styleTime, Locale locale) {
		return SimpleDateFormat.getDateTimeInstance(
				styleDate, styleTime, locale)
                    .format(date);
	}

}
