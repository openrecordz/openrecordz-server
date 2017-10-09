package shoppino.web.interceptor;

import it.f21.accessLog.AccessLog;
import it.f21.accessLog.AccessLogManager;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import shoppino.security.service.AuthenticationService;
import shoppino.service.TenantService;



@Component("auditingInterceptor")
public class AuditingInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	AccessLogManager alm;
	
	static{
		BasicConfigurator.configure();
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		logger.debug("After rendering the view");
		
		try {
			 /* ex.
			  * httpRequest.getRemoteHost(): 87.7.33.250
			  * httpRequest.getRemoteAddr(): 87.7.33.250
			  * httpRequest.getRequestURL(): http://www.realtrack.it:8080/erp-dev/fleets/track_veicoli/startTrack.do?id=37
			  * httpRequest.getRequestURI(): /erp-dev/fleets/track_veicoli/startTrack.do
			  * httpRequest.getQueryString(): id=37
			  */
			
			if (!request.getRequestURL().toString().contains("/resources/")) {
				AccessLog al = new AccessLog();
				
				al.setDate(new Date());
				
				al.setRemote_host(request.getRemoteHost());
				al.setRemote_addr(request.getRemoteAddr());
				al.setRequest_uri(request.getRequestURI());
				if ( request.getQueryString() != null )
					al.setQuery_string(request.getQueryString());
				al.setRequest_url(request.getRequestURL().toString());
				
				al.setUser_agent(request.getHeader("user-agent"));
				
				Locale locale = request.getLocale();
				al.setUser_lang(locale.toString());
//				Thread t = Thread.currentThread();
//				System.out.println("t.getName()" +t.getName() +":"+ authenticationService.getCurrentLoggedUsername());
				String username = authenticationService.getCurrentLoggedUsername();
				al.setUsername(username);
				al.setTenant(tenantService.getCurrentTenantName());
	//			HttpSession session = request.getSession(true);
	//   		Utente user = (Utente) session.getAttribute("user");
	   		
	//			if ( user != null ) {
	//				al.setUsername(user.getUsername());
	//				al.setUser_id(user.getId_utente());
	//				al.setOrganization_id(0);
	//			}
				
	//			AccessLogManager man = new AccessLogManager();
				alm.serialize(al);
			}
		} catch (Throwable t) {
			logger.error("Error executing auditing interceptor ",t);
		}
		
		super.afterCompletion(request, response, handler, ex);
	}
}
