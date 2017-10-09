package microdev.db;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import shoppino.ApplicationSettings;



/**
 * @author andrea
 */
public class ConnectionFactory {
	
	private Log log = LogFactory.getLog(ConnectionFactory.class);
	
	private ApplicationSettings settings = null;
	
	public ConnectionFactory(String bundle) {
		settings = ApplicationSettings.getInstance();
	}
		
	public Connection openConnection() {
		Connection conn = null;
		if ( settings.getDatasource() != null ) {
			try {
				Context ctx = new InitialContext();
				if (ctx == null) {
					throw new Exception("Non riesco a recuperare il Context.");
				}
				DataSource ds = (DataSource) ctx.lookup(settings.getDatasource());
				if (ds != null) {
					conn = ds.getConnection();
				}
			} catch (Exception e) {
				log.error("",e);
			}
		} else {
			try {
				Class.forName(settings.getDatabaseClass());
				log.debug("Database... url: " + settings.getDriverUrl() + ", host: " + settings.getDatabaseHost() + ", port: " + settings.getDatabasePort() + ", database: " + settings.getDatabaseName() + ", user: " + settings.getDatabaseUser());
				String url = settings.getDriverUrl() + "://" +
				settings.getDatabaseHost() + ":" +
				settings.getDatabasePort() + "/" +
				settings.getDatabaseName(); // + "?useUnicode=true&characterEncoding=UTF-8";
				log.debug("db url: " + url);
				String user = settings.getDatabaseUser();
				String pwd = settings.getDatabasePasswd();
				return DriverManager.getConnection(url, user, pwd);
				
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return conn;
	}
}
