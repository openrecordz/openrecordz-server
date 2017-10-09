package openrecordz.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
* This class send notifications about
* changes to the OpenRecordz servlet context.
* Must be configured in the deployment descriptor for the web
* application.
*/

public class OpenRecordzContextListener  implements ServletContextListener {

	private Log log = LogFactory.getLog(this.getClass());
	

	/** Notification that the Aldo web application initialization
	 ** process is starting.
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		log.debug("Shoppino context initialized");
		log.info("Shoppino servlet context path : " + event.getServletContext().getContextPath());
		log.info("Shoppino servlet context real path : " + event.getServletContext().getRealPath("/"));
	}


	 /**
     ** Notification that the Aldo servlet context is about to be shut down.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		log.debug("Shoppino context destroyed");
	}
		
		 

}
