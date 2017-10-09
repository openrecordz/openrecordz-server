package microdev.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author andrea
 *
 */
public class ConnectionUtil {
	
	private static final String DEFAULT_BUNDLE = "application";
	
	private static Log log = LogFactory.getLog(ConnectionUtil.class);
	
	private static ConnectionFactory connectionFactory;
	
	public static void initConnectionFactory(String bundle) {
		if ( connectionFactory == null ) {
			connectionFactory = new ConnectionFactory(bundle);
		}
	}
	
	private static void initConnectionFactory() {
		if ( connectionFactory == null ) {
			connectionFactory = new ConnectionFactory(DEFAULT_BUNDLE);
		}
	}
	
	public static final ThreadLocal connection = new ThreadLocal();
	
	public static Connection currentConnection() {
		Connection conn = (Connection) connection.get();
		// Open a new Connection, if this Thread has none yet
		if (conn == null) {
			try {
				log.debug("initializing ConnectionFactory.");
				initConnectionFactory();
				conn = connectionFactory.openConnection();
				log.debug("new connection created: " + conn);
			} catch (Exception e ) { log.error("", e); }
			connection.set(conn);
		}
		
		log.debug("Current Thread: " + Thread.currentThread());
		log.debug("ThreadLocal: " + connection);
		log.debug("conn: " + conn);
		return conn;
	}
	
// ============================
	// QUESTA IMPLEMENTAZIONE (CHE ESTENDE THREADLOCAL) E' INTERESSANTISSIMA
	// PRESA DA QUI:
	// http://www.ibm.com/developerworks/java/library/j-threads3.html
//	public class ConnectionDispenser { 
//		  private static class ThreadLocalConnection extends ThreadLocal {
//		    public Object initialValue() {
//		      return DriverManager.getConnection(ConfigurationSingleton.getDbUrl());
//		    }
//		  }
//
//		  private static ThreadLocalConnection conn = new ThreadLocalConnection();
//
//		  public static Connection getConnection() {
//		    return (Connection) conn.get();
//		  }
//		}
// ==========================
	
	public static void closeConnection() {
		Connection conn = (Connection) connection.get();
		if ( conn != null ) {
			try {
				conn.close();
			} catch (Exception ignore) {}
			connection.set(null);
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try { rs.close(); } catch (SQLException ignore) {}
			rs = null;
		}
	}
	
	public static void closeStatement(Statement stat) {
		if (stat != null) {
			try { stat.close(); } catch (SQLException ignore) {}
			stat = null;
		}
	}
	
	/** @deprecated use closeStatement*/
	public static void closePreparedStatement(PreparedStatement stat) {
		if (stat != null) {
			try { stat.close(); } catch (SQLException ignore) {}
			stat = null;
		}
	}
}
