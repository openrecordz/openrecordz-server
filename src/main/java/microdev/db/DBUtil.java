package microdev.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class DBUtil {

	// convert 01/12/2002 || 01-12-2002
	// in 2002-12-01
	public static String date2mysql(String d) {
		if (d.equals("")) return d;
		d = d.replace('-','/');
		int i_1 = d.indexOf("/");
		String day = d.substring(0,i_1);
		int i_2 = d.indexOf("/",i_1+1);
		String month = d.substring(i_1+1,i_2);
		String year = d.substring(i_2+1);
		return year+"-"+month+"-"+day;
	}

	// convert 2002-12-01 || 2002/12/01
	// in 01/12/2002
	public static String mysql2date(String d) {
		if (d.equals("")) return d;
		d = d.replace('-','/');
		int i_1 = d.indexOf("/");
		String year = d.substring(0,i_1);
		int i_2 = d.indexOf("/",i_1+1);
		String month = d.substring(i_1+1,i_2);
		String day = d.substring(i_2+1);
		return day+"/"+month+"/"+year;
	}
	
	public static String formatDateToDatabase(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static int daysBetween(String d1, String d2) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date1, date2;
		int difference = 0;
		try {
			date1=df.parse(d1);
			date2=df.parse(d2);
			long diff = date2.getTime() - date1.getTime();
			diff = diff/(1000*60*60*24);
			difference = (int) diff;
		} catch(Exception e) {
			throw e;
		}
		return difference;
	}

	public static String compact(String s) {
		// 1 - elimina dalla stringa i caratteri CR LF TAB
		// 2 - lascia uno spazio soltanto al posto
		//     di pi� spazi adiacenti

		s = s.replace((char) 13,(char) 32); //cr
		s = s.replace((char) 10,(char) 32); //lf
		s = s.replace((char) 9,(char) 32); //tab
		StringTokenizer st = new StringTokenizer(s);
		StringBuffer r = new StringBuffer();
		while (st.hasMoreTokens()) {
			r.append(st.nextToken()+" ");
		}
		s = r.toString();
		return s.trim();
	}

	public static String addslashes(String s) {
		if (s==null) return s;
		if (s.equals("")) return s;
		StringBuffer res = new StringBuffer("");
		for (int i=0;i<=s.length()-1;i++) {
			if (s.charAt(i)=='\\') {
				res.append("\\");
			}
			res.append(s.charAt(i));
		}
		s = res.toString();
		res = new StringBuffer("");
		for (int i=0;i<=s.length()-1;i++) {
			if (s.charAt(i)=='\'') {
				res.append("\\");
			}
			res.append(s.charAt(i));
		}
		s = res.toString();
		res = new StringBuffer("");
		for (int i=0;i<=s.length()-1;i++) {
			if (s.charAt(i)=='\"') {
				res.append("\\");
			}
			res.append(s.charAt(i));
		}
		s = res.toString();
		return s;
	}

	public static String pathDefrag(String path) {
		String query = "";
		int queryIndex = path.indexOf("?");
		if (queryIndex!=-1) {
			query = path.substring(queryIndex);
			path = path.substring(0,queryIndex);
		}
		while (true) {
			path = path.replaceAll("/\\./","/");
			if (path.matches(".*/\\./.*")) continue;
			else break;
		}

		while (true) {
			path = path.replaceAll("/[^\\./]*/\\.\\./","/");
			if (path.matches(".*/[^\\./]*/\\.\\./.*")) continue;
			else break;
		}
		//path = path.replaceAll("/[\\./]*/","/");
		return path+query;
		
		//String query = "";
		//int queryIndex = path.indexOf("?");
		//if (queryIndex!=-1) {
		//	query = path.substring(queryIndex);
		//	path = path.substring(0,queryIndex);
		//}
		//while (true) {
		//	path = path.replaceAll("/\\./","/");
		//	if (path.matches(".*/\\./.*")) continue;
		//	else break;
		//}

		//while (true) {
		//	path = path.replaceAll("/[^\\./]*/\\.\\./","/");
		//	if (path.matches(".*/[^\\./]*/\\.\\./.*")) continue;
		//	else break;
		//}
		//return path+query;
	}
}