package shoppino.web.filter;

	import java.io.IOException;

	import javax.servlet.Filter;
	import javax.servlet.FilterChain;
	import javax.servlet.FilterConfig;
	import javax.servlet.ServletException;
	import javax.servlet.ServletRequest;
	import javax.servlet.ServletResponse;
	import javax.servlet.http.HttpServletRequest;

	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;


	public class TenantFilter implements Filter {

		private Log log = LogFactory.getLog(this.getClass());
		private static String RESOURCES_PATH ="resources";

		public void doFilter(ServletRequest req, ServletResponse res,
				FilterChain chain) throws IOException, ServletException {
			
//			http://www.mkyong.com/spring-mvc/spring-mvc-handler-interceptors-example/
//			spring interceptor
			
			HttpServletRequest request = (HttpServletRequest) req;

			// Get the IP address of client machine.
			
			String localAddress = request.getLocalAddr();
			String localPort = String.valueOf(request.getLocalPort());


			// Log the IP address and current timestamp.
			String uri=request.getRequestURI();
			log.debug("Uri : " + uri);
			
			String contextPath=request.getContextPath();
			log.debug("contextPath: " +contextPath);
			
			String[] uriSplit = uri.split("/");
			if (!uri.contains(RESOURCES_PATH) && uriSplit.length>3) {
				log.debug("tenant : " + uriSplit[1]) ;
				
			}
			
			
			String remoteddr=request.getRemoteAddr();
			chain.doFilter(req, res);
			
			
		}

		public void init(FilterConfig config) throws ServletException {
			//nothing to do
		}

		public void destroy() {
			//add code to release any resource
		}

}
