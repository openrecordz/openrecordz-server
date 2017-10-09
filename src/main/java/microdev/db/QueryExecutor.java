/*
 * Created on 21-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package microdev.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author andrea
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryExecutor {
	private Log log = LogFactory.getLog(QueryExecutor.class);
	
	private Connection conn = null;
	
	/** TODO */
	public void execute(String query) {
		// TODO
	}
	
	public List executeQuery(String query) {
		return executeQuery(query, null);
	}
	
	public List executeQuery(String query, QueryTips tips) {
		
		// aggrega temporaneamente i risultati
		ArrayList vect = new ArrayList();
		
		String order_query = "";
		String limit_query = "";
		if ( tips != null ) {
			order_query = tips.getOrderQuery();
			limit_query = tips.getLimitQuery();
		}
		String final_query = query + order_query + limit_query;
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionUtil.currentConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
													ResultSet.CONCUR_READ_ONLY);
			//log.debug("executing query: " + final_query);
			
			rs = stmt.executeQuery(final_query);
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				//ResultRecord rec = new ResultRecord();
				Map rec = new LinkedHashMap();
				for ( int i=1; i <= meta.getColumnCount(); i++ ) {
					
//					log.debug("meta.getColumnLabel(i):" + meta.getColumnLabel(i));
//					log.debug("meta.getColumnClassName(i):" + meta.getColumnClassName(i));
//					log.debug("meta.getColumnType(i):" + meta.getColumnType(i));
//					log.debug("meta.getColumnTypeName(i):" + meta.getColumnTypeName(i));
//					
//					log.debug("rs.getObject(i):" + rs.getObject(i));
//					log.debug("rs.getObject(i).getClass():" + rs.getObject(i).getClass());
					rec.put(meta.getColumnLabel(i),rs.getObject(i));
				}
				//log.debug("added record: " + rec);
				vect.add(rec);
			}
			rs.close();
		} catch (Exception e) {
			log.error("",e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException ignore) {}
				stmt = null;
			}
			//In realt� la connessione dovrebbe essere chiusa dal ConnectionFilter
			//In questo caso sarebbe meglio chiedere la connessione ad ibatis e lasciarla
			//chiudere a lui. 
			//Per motivi di tempo � stato utilizzato un workaround ovvero si � deciso
			//di bypassare il connection filter e di fare il post processing alla fine di questo metodo.
			//Per due motivi: il primo � che non avrebbe senso aggiungere il connection filter di 
			//questo modulo al web.xml, il secondo � che si ha bisogno di una soluzione immediata
			//La soluzione migliore � quella iniziale
			ConnectionUtil.closeConnection();
		}
		if ( vect.size() > 0 ) {
			//return (ResultRecord[]) vect.toArray(new ResultRecord[vect.size()]);
			return vect;
		}
		return null;
	}
	
	public List executeQueryTyped(String query, Class type, QueryTips tips){
		
		// aggrega temporaneamente i risultati
		ArrayList vect = new ArrayList();
		String order_query = "";
		String limit_query = "";
		if ( tips != null ) {
			order_query = tips.getOrderQuery();
			limit_query = tips.getLimitQuery();
			log.debug("order query:" + order_query);
			log.debug("limit query:" + limit_query);
		}
		String final_query = query + order_query + limit_query;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionUtil.currentConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
													ResultSet.CONCUR_READ_ONLY);
			
			log.debug(final_query);
			rs = stmt.executeQuery(final_query);
			while (rs.next()) {
				BasicRowProcessor proc = new BasicRowProcessor();
				Object obj = proc.toBean(rs, type);
				//log.debug("added object: " + obj);
				vect.add(obj);
			}
			
			rs.close();
		} catch (Exception e) {
			log.error("",e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException ignore) {}
				stmt = null;
			}
			//In realt� la connessione dovrebbe essere chiusa dal ConnectionFilter
			//In questo caso sarebbe meglio chiedere la connessione ad ibatis e lasciarla
			//chiudere a lui. 
			//Per motivi di tempo � stato utilizzato un workaround ovvero si � deciso
			//di bypassare il connection filter e di fare il post processing alla fine di questo metodo.
			//Per due motivi: il primo � che non avrebbe senso aggiungere il connection filter di 
			//questo modulo al web.xml, il secondo � che si ha bisogno di una soluzione immediata
			//La soluzione migliore � quella iniziale
			ConnectionUtil.closeConnection();
		}
		if ( vect.size() > 0 ) {
			//return (ResultRecord[]) vect.toArray(new ResultRecord[vect.size()]);
			return vect;
		}
		return null;
	}
	
}
