package shoppino.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	//return today from midnight
	public static Date getToday(){
		  Calendar c = new GregorianCalendar();
		    c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
		    c.set(Calendar.MINUTE, 0);
		    c.set(Calendar.SECOND, 0);
		    c.set(Calendar.MILLISECOND, 0);
		    Date d1 = c.getTime(); //the midnight, that's the first second of the day.
		    
		    return d1;
	}
	
	public static Date addHours(int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
//		cal.add(Calendar.HOUR, -1);
		cal.add(Calendar.HOUR, amount);
		
		Date oneHourBack = cal.getTime();
		
		return oneHourBack;
	}
}