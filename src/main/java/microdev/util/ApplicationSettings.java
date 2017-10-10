/*
 * Created on 5-lug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package microdev.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author andrea
 */
public class ApplicationSettings {
	
	private Log log = LogFactory.getLog(ApplicationSettings.class);
	
	public static final String MAX_DOWNLOAD_ATTEMPTS = "max_download_attempts";
	
	public static final String DATABASE_CLASS = "database.class";
	public static final String DRIVER_URL = "database.driver.url";
	public static final String DATABASE_HOST = "database.host";
	public static final String DATBASE_PORT = "database.port";
	public static final String DATABASE_NAME = "database.name";
	public static final String DATABASE_USER = "database.user";
	public static final String DATABASE_PASSWORD = "database.passwd";
	
	
	private static ApplicationSettings instance = null;
	
	private ResourceBundle bundle = null;
	
	private String bundle_name = null;
	private String datasource = null;
	private String databaseClass = null;
	private String driverUrl = null;
	private String databaseHost = null;
	private String databasePort = null;
	private String databaseName = null;
	private String databaseUser = null;
	private String databasePasswd = null;
	
	private ApplicationSettings() {
		this.bundle_name = "application";
		init();
	}
	
	private void init() {
		bundle = ResourceBundle.getBundle(bundle_name);
		
//		printAllProperties();
		
		// from datasource
		try {
			datasource = bundle.getString("datasource");
		} catch (MissingResourceException e) {
//			log.debug("Non e' impostata un Datasource in questa configurazione");
		}
		// explicit
		try {
//			log.debug("Sto usando le impostazioni per la connessione esplicite al Database");
			databaseClass = bundle.getString("database.class");
			driverUrl = bundle.getString("database.driver.url");
			databaseHost = bundle.getString("database.host");
			databasePort = bundle.getString("database.port");
			databaseName = bundle.getString("database.name");
			databaseUser = bundle.getString("database.user");
			databasePasswd = bundle.getString("database.passwd");
		} catch (MissingResourceException e) {
			log.error("", e); //e.printStackTrace();
		}
		// check parameter's correctness
		//try {
		//	checkParams();
		//} catch (Exception e) {
		//	log.debug("", e);
		//}
	}
	
	private void printAllProperties() {
		Set<String> keys = bundle.keySet();
		for( String key : keys) {
			log.info(key + ": " + bundle.getString(key));
		}
		
	}
	
//	static void createFromBundle(String bundle) {
//		instance = new DataBaseSettings(bundle);
//	}
	
	public static synchronized ApplicationSettings getInstance() {
		if (instance == null) {
			instance = new ApplicationSettings();
		}
		return instance;
	}
	
	public String getProperty(String name) {
		return bundle.getString(name);
	}
	
	public int getPropertyAsInt(String name) {
		return Integer.parseInt(bundle.getString(name));
	}
	
	public boolean getPropertyAsBoolean(String name) {
		return Boolean.parseBoolean(bundle.getString(name).trim());
	}
	
	/**
	 * @return Returns the databaseClass.
	 */
	public String getDatabaseClass() {
		return databaseClass;
	}
	
	/**
	 * @param databaseClass The databaseClass to set.
	 */
	public void setDatabaseClass(String databaseClass) {
		this.databaseClass = databaseClass;
	}
	
	/**
	 * @return Returns the databaseHost.
	 */
	public String getDatabaseHost() {
		return databaseHost;
	}
	
	/**
	 * @param databaseHost The databaseHost to set.
	 */
	public void setDatabaseHost(String databaseHost) {
		this.databaseHost = databaseHost;
	}
	
	/**
	 * @return Returns the databaseName.
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * @param databaseName The databaseName to set.
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	/**
	 * @return Returns the databasePasswd.
	 */
	public String getDatabasePasswd() {
		return databasePasswd;
	}
	
	/**
	 * @param databasePasswd The databasePasswd to set.
	 */
	public void setDatabasePasswd(String databasePasswd) {
		this.databasePasswd = databasePasswd;
	}
	
	/**
	 * @return Returns the databasePort.
	 */
	public String getDatabasePort() {
		return databasePort;
	}
	
	/**
	 * @param databasePort The databasePort to set.
	 */
	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}
	
	/**
	 * @return Returns the databaseUser.
	 */
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	/**
	 * @param databaseUser The databaseUser to set.
	 */
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}
	
	/**
	 * @return Returns the datasource.
	 */
	public String getDatasource() {
		return datasource;
	}
	
	/**
	 * @param datasource The datasource to set.
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	/**
	 * @return Returns the driverUrl.
	 */
	public String getDriverUrl() {
		return driverUrl;
	}
	
	/**
	 * @param driverUrl The driverUrl to set.
	 */
	public void setDriverUrl(String driverUrl) {
		this.driverUrl = driverUrl;
	}
	
}
