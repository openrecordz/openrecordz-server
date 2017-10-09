/*
 * Created on 25-giu-2006
 *
 * Procura Business Intelligence (Gnosis)
 * (C)2006 Links M&T SPA
 */
package microdev.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @author andrea */

public class DBLayer {
	private static Log log = LogFactory.getLog(DBLayer.class);
	
	public static final String TRUE_MYSQL = "1"; // MYSQL
	public static final String TRUE_PG = "true"; // POSTGRESQL
	
	public static String true_value() {
		return TRUE_MYSQL;
	}
	
	public static String convertWordsForMysql(String words) {
		String _words[] = words.split("\\s+");
		StringBuffer new_words = new StringBuffer();
		for ( int i=0; i<_words.length; i++ ) {
			if ( !_words[i].equals("") )
				new_words.append("+" + _words[i] + " ");
		}
		
		log.debug("new_words:" + new_words.toString().trim());
		
		return new_words.toString().trim();
	}
	
}
