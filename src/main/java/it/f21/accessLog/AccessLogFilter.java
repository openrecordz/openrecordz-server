package it.f21.accessLog;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author andrea
 */







//NON USATO
//
//VIENE USATO AUDITINGINTERCEPTOR
//
//
//
//
//
//
//
//
//
//
//
//============



public class AccessLogFilter implements Filter {
	private FilterConfig config = null;
	private Log log = LogFactory.getLog(AccessLogFilter.class);
	
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		try {
			 /* ex.
			  * httpRequest.getRemoteHost(): 87.7.33.250
			  * httpRequest.getRemoteAddr(): 87.7.33.250
			  * httpRequest.getRequestURL(): http://www.realtrack.it:8080/erp-dev/fleets/track_veicoli/startTrack.do?id=37
			  * httpRequest.getRequestURI(): /erp-dev/fleets/track_veicoli/startTrack.do
			  * httpRequest.getQueryString(): id=37
			  */
    		
			AccessLog al = new AccessLog();
			
			al.setDate(new Date());
			
			al.setRemote_host(httpRequest.getRemoteHost());
			al.setRemote_addr(httpRequest.getRemoteAddr());
			al.setRequest_uri(httpRequest.getRequestURI());
			if ( httpRequest.getQueryString() != null )
				al.setQuery_string(httpRequest.getQueryString());
			al.setRequest_url(httpRequest.getRequestURL().toString());
			
//			httpRequest.getH
//			HttpSession session = httpRequest.getSession(true);
//    		Utente user = (Utente) session.getAttribute("user");
    		
//			if ( user != null ) {
//				al.setUsername(user.getUsername());
//				al.setUser_id(user.getId_utente());
//				al.setOrganization_id(0);
//			}
			
			AccessLogManager man = new AccessLogManager();
			man.serialize(al);
		} catch (Exception ex) {
			log.error("",ex);
		}
		try {
		chain.doFilter(request, response);
		} catch (Exception ex) {
			log.error("",ex);
		}

	}
	
	public void destroy() {
		config = null;
	}
}
