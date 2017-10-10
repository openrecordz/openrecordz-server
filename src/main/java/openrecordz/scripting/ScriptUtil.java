package openrecordz.scripting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScriptUtil {

	public static String getAsString(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return getAsString(formatter.parse(date), format);
	}
	
	public static String getAsString(Date date, String format) {
		  SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		  return sdf.format(date);
	}
	
	public static String getAsString(Date date, String format, String locale) {
		  SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.forLanguageTag(locale));
		  return sdf.format(date);
	}
	
}
 