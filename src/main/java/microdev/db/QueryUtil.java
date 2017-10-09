/*
 * Created on 22-giu-2006
 *
 * Procura Business Intelligence (Gnosis)
 * (C)2006 Links M&T SPA
 */
package microdev.db;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** @author andrea */

public class QueryUtil {
	private static Log log = LogFactory.getLog(QueryUtil.class);
	
	public static long getCountByResults(List results) {
		if ( results == null ) return 0;
		if ( results.size() == 0 ) return 0;
		long num = 0;
		try {
//			log.debug("(ResultRecord) results.get(0):" + (ResultRecord) results.get(0));
//			log.debug("( (Integer) ( (ResultRecord) results.get(0) ):" +  ( (ResultRecord) results.get(0) ) );
//			log.debug("getCol().getclass:" +  ((ResultRecord) results.get(0)).getCol(DBConst.COUNT_FIELD).getClass() );
			
			num = ( (Long) ( (Map) results.get(0) ).get(DBConst.COUNT_FIELD) ).longValue();
		} catch (Exception e) {
			log.error("", e);
		}
		return num;
	}
}
