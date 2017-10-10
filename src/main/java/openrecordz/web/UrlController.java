package openrecordz.web;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestOperations;

import openrecordz.exception.OpenRecordzException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.service.UserService;
import openrecordz.service.EnvironmentService;
import openrecordz.service.UrlService;

@Controller
public class UrlController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	UrlService urlService;
    
	@Autowired
	RestOperations restTemplate;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EnvironmentService environmentService;
	
//	use getandredirect
//	 @RequestMapping(value = "/urls/{uid}/r", method = RequestMethod.GET)    
// 	 public String urlRedirect(Model model, @PathVariable String uid, HttpServletRequest request,  HttpServletResponse response) throws ShoppinoException, URISyntaxException, UserNotExistsException {	   	    	
//    	
//		String url = urlService.getByUID(uid);
//    	logger.debug("Redirecting to : " + url);
//    	    
//
//    	return "redirect:" + url;
//    }
	 
	 
	 @RequestMapping(value = "/urls/{uid}/f", method = RequestMethod.GET)    
 	 public String urlForward(Model model, @PathVariable String uid,HttpServletRequest request) throws OpenRecordzException {	   	    	
    	
//		 String gotoUrl= null;
//	    	
//		if (request.getParameter("goto")!=null){
//			gotoUrl=request.getParameter("goto");
//		}
		
		String url = urlService.getByUID(uid);
		
//		if (gotoUrl!=null){
//			url=
//		}
		
    	logger.debug("Forwarding to : " + url);
    	
    	return "forward:" + url;
    }
	 
	 
	 @RequestMapping(value = "/urls/{uid}/g", method = RequestMethod.GET)    
 	 public String urlGet(Model model, @PathVariable String uid) throws OpenRecordzException {	   	    	
    	
		String url = urlService.getByUID(uid);
    	logger.debug("Getting to : " + url);
    	
    	restTemplate.execute(url, HttpMethod.GET, (RequestCallback)null, (ResponseExtractor)null,new HashMap());
    	
    	return "redirect:/";
    }
	 
	 

	 
//	 make a post at URL page identified by UID. I can authenticate the call with pusername parameter. The servire redirect always to homepage 
	 
	 @RequestMapping(value = "/urls/{uid}/p", method = RequestMethod.GET)    
 	 public String urlPost(Model model, @PathVariable String uid) throws OpenRecordzException, URISyntaxException, UserNotExistsException {	   	    	
    	
		String url = urlService.getByUID(uid);
    	logger.debug("Posting to : " + url);
    	
//    	HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
//    			DefaultHttpClient httpClient =
//    			 (DefaultHttpClient) requestFactory.getHttpClient();
//    			httpClient.getCredentialsProvider().setCredentials(
//    			 new AuthScope(host, port, AuthScope.ANY_REALM),
//    			  new UsernamePasswordCredentials("name", "pass"));
    			
//    	HttpHeaders createHeaders( String username, String password ){
//    		   return new HttpHeaders(){
//    		      {
//    		         String auth = username + ":" + password;
//    		         byte[] encodedAuth = Base64.encodeBase64(
//    		            auth.getBytes(Charset.forName("US-ASCII")) );
//    		         String authHeader = "Basic " + new String( encodedAuth );
//    		         set( "Authorization", authHeader );
//    		      }
//    		   };
//    		}
//    	
//    	restTemplate.postForLocation(url, null);
//    	restTemplate.execute(url, HttpMethod.POST, (RequestCallback)null, (ResponseExtractor)null,new HashMap());
    	
    	
    	HttpHeaders headers = new HttpHeaders();
    	
  
    	
    	if (extractParamsFromURL(url).containsKey("pusername")) {
    		
	    	String username = extractParamsFromURL(url).get("pusername");
	    	
	    	String plainCreds = username + ":"+ userService.getByUsername(username).getPassword();
	    	logger.debug("plainCreds : " + plainCreds);
	    	byte[] plainCredsBytes = plainCreds.getBytes();
	    	byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
	    	String base64Creds = new String(base64CredsBytes);
	    	
	    	
	    	headers.add("Authorization", "Basic " + base64Creds);
	    	
    	}
    	HttpEntity<String> request = new HttpEntity<String>(headers);
    	
    	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    	
    	logger.debug("urlPost response : " + response);
    	
    	return "redirect:/";
    }
	 


	    private Map<String, String> extractParamsFromURL(final String url) throws URISyntaxException {
	        return new HashMap<String, String>() {{
	            for(NameValuePair p : URLEncodedUtils.parse(new URI(url), "UTF-8")) 
	                put(p.getName(), p.getValue());
	        }};
	    }

	    
	    
	    
		 @RequestMapping(value = "/urls/{uid}/r", method = RequestMethod.GET)    
	 	 public String urlGetAndRedirectTo(Model model, @PathVariable String uid,HttpServletRequest request,  HttpServletResponse response) throws OpenRecordzException, URISyntaxException, UserNotExistsException {	   	    	
	    	
			String url = urlService.getByUID(uid);
	    	logger.debug("Getting to : " + url);
	    	
	    try {
			//aggiungo autenticazione se c'è
			if (extractParamsFromURL(url).containsKey("pusername")) {
	    		
		    	String username = extractParamsFromURL(url).get("pusername");
		    	
		    	String password = userService.getByUsername(username).getPassword();
		    	
		    	HttpHeaders headers = new HttpHeaders();
		    	
//		    	MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);	     		    
		     	
		     	
		     	//copio header della request nella request che devo fare con restTemplate
		     	Enumeration<String> headerNames = request.getHeaderNames();

				while (headerNames.hasMoreElements()) {

					String headerName = headerNames.nextElement();				

					Enumeration<String> headersTmp = request.getHeaders(headerName);
					while (headersTmp.hasMoreElements()) {
						String headerValue = headersTmp.nextElement();
						headers.add(headerName, headerValue);
					}

				}
		     	

		      	HttpEntity<String> requestHttpEntity = new HttpEntity<String>(headers);
		     	
//		        requestHttpEntity.
		        
		     	
		     	ResponseEntity<String> restResponse = restTemplate.exchange(environmentService.getTenantUrl()+"/j_spring_security_check?j_username="+username+"&j_password="+password+"&_spring_security_remember_me=true", HttpMethod.POST, requestHttpEntity, String.class);
		     	HttpHeaders restHeaders = restResponse.getHeaders();
		     	
//		    	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, String.class);
		     	
		     	List<HttpCookie> ccs=HttpCookie.parse(restHeaders.get("Set-Cookie").get(0));
		     	
//		          if (useSecureCookie == null) {
//		              cookie.setSecure(request.isSecure());
//		          } else {
//		              cookie.setSecure(useSecureCookie);
//		          }
		          
		     	  Cookie cookie = new Cookie(ccs.get(0).getName(), ccs.get(0).getValue());
		          cookie.setMaxAge((int) ccs.get(0).getMaxAge());
		          cookie.setPath(ccs.get(0).getPath());
		          
		          cookie.setDomain(ccs.get(0).getDomain());
		          
		         cookie.setSecure(request.isSecure());
		          
		    	response.addCookie(cookie);
		    	logger.debug("urlGet response : " + response);
			}
	
	    }catch (Exception e) {
		 	logger.error("Error authenticating url service for uid: " + uid+" I'm going at url without authentication", e);
		   return "redirect:" + url;
	   }
	    	
	    	return "redirect:" + url;
	    }
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 @RequestMapping(value = "/urls/{uid}/rext", method = RequestMethod.GET)    
	 	 public String test(Model model, @PathVariable String uid,HttpServletRequest request,  HttpServletResponse response) throws OpenRecordzException, URISyntaxException, UserNotExistsException {	   	    	
	    	
			String url = urlService.getByUID(uid);
	    	logger.debug("Getting to : " + url);
	    	
	    try {
			//aggiungo autenticazione se c'è
			if (extractParamsFromURL(url).containsKey("pusername")) {
	    		
		    	String username = extractParamsFromURL(url).get("pusername");
		    	
		    	String password = userService.getByUsername(username).getPassword();
		    	
		    	HttpHeaders headers = new HttpHeaders();
		    	
//		    	MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);	     		    
		     	
		     	
		     	//copio header della request nella request che devo fare con restTemplate
		     	Enumeration<String> headerNames = request.getHeaderNames();

				while (headerNames.hasMoreElements()) {

					String headerName = headerNames.nextElement();				

					Enumeration<String> headersTmp = request.getHeaders(headerName);
					while (headersTmp.hasMoreElements()) {
						String headerValue = headersTmp.nextElement();
						headers.add(headerName, headerValue);
					}

				}
		     	

		      	HttpEntity<String> requestHttpEntity = new HttpEntity<String>(headers);
		     	
//		        requestHttpEntity.
		        
		     	
		     	ResponseEntity<String> restResponse = restTemplate.exchange("http://bpp.devconsole.smart21.it/data.php?username=admin&password=drsadmin#", HttpMethod.GET, requestHttpEntity, String.class);
		     	HttpHeaders restHeaders = restResponse.getHeaders();
		     	
//		    	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, String.class);
		     	
		     	List<HttpCookie> ccs=HttpCookie.parse(restHeaders.get("Set-Cookie").get(0));
		     	
//		          if (useSecureCookie == null) {
//		              cookie.setSecure(request.isSecure());
//		          } else {
//		              cookie.setSecure(useSecureCookie);
//		          }
		          
		     	  Cookie cookie = new Cookie(ccs.get(0).getName(), ccs.get(0).getValue());
		          cookie.setMaxAge((int) ccs.get(0).getMaxAge());
		          cookie.setPath(ccs.get(0).getPath());
		          
		          cookie.setDomain(ccs.get(0).getDomain());
		          
		         cookie.setSecure(request.isSecure());
		          
		    	response.addCookie(cookie);
		    	logger.debug("urlGet response : " + response);
			}
	
	    }catch (Exception e) {
		 	logger.error("Error authenticating url service for uid: " + uid+" I'm going at url without authentication", e);
		   return "redirect:" + url;
	   }
	    	
	    	return "redirect:" + url;
	    }
		 

}
