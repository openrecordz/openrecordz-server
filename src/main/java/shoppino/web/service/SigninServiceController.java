package shoppino.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import shoppino.exception.ResourceNotFoundException;
import shoppino.security.exception.AuthenticationException;
import shoppino.security.service.AuthenticationService;
import shoppino.service.PersonService;
import shoppino.service.TenantService;

/**
 * @author andrea leo
 * 
 * SigninServiceController fornisce i metodi per autenticare un utente alla piattaforma.
 * Al momento la piattaforma supporta per i servizi la modalità di autenticazione BASIC AUTH
 *
 */
@Controller
public class SigninServiceController implements BaseServiceController {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private TenantService tenantService;
	
	protected final Log logger = LogFactory.getLog(getClass());	
	
	/**
	 * Il metodo signin (http GET) consente di autenticare un utente alla piattaforma attraverso il passaggio
	 * dei parametri username e password.
	 * Esempio di utilizzo : http://www.ciaotrip.it/service/v1/signin?username=username&password=password
	 * 
	 * Risposta per autenticazione avvenuta con successo :
	 * 	{"status":"success", "channel":"signin", "basicAuth":"YWRtaW46YWRtaW5hZG1pbg=="} 
	 * 
	 * @param username  L'username dell'utente
	 * @param password  La password dell'utente
	 * @return 
	 * @throws AuthenticationException nel caso di autenticazione fallita viene ritornato un messaggio con HTTP STATUS 401. 
	 * 	Di seguito un esempio di autenticazione fallita :
	 * 		{"status":401,"code":401,"message":"Authentication failure with username : errato and password : errata",
	 * 		"developerMessage":"Authentication failure with username : admin and password : admin","moreInfoUrl":"mailto:info@ciaotrip.it"}
	 * @throws ResourceNotFoundException 
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
    public @ResponseBody Map login(Model model, HttpServletRequest request,
    		@RequestParam("username") String username,
    		@RequestParam("password") String password) throws AuthenticationException, ResourceNotFoundException {
//        Map<String, Object> model = new HashMap<String, Object>();
    	
//		username = UsernameUtils.getRealUsername(username);
		
    	logger.debug("username : " + username);
    	logger.debug("password : " + password);
    	    	
    	authenticationService.authenticate(username, password);

    	String httpAuth = authenticationService.getCurrentLoggedUsername() + ":" + password;
		logger.debug("http auth : " + httpAuth);
		
		String encoding = Base64.encodeBase64String(httpAuth.getBytes());
		logger.debug("encoded basic auth : " + encoding);
		String decoded = new String(Base64.decodeBase64(encoding));
		logger.debug("decoded basic auth : " + decoded);		
		
		//join other tenant
//		don't use username var but authenticationService.getCurrentLoggedUsername() beacuse username could by an email....
		personService.joinCurrentTenant(authenticationService.getCurrentLoggedUsername());
		
		
		Map returnValue = new HashMap();
		returnValue.put("basicAuth", encoding);
		//username could be the email so it's not correct :)
		returnValue.put("username", authenticationService.getCurrentLoggedUsername());
		
		return returnValue;				
    }
	
//	@RequestMapping(value = "/signin", method = RequestMethod.POST)
////	@Deprecated
//    public ModelAndView login(Model model, HttpServletRequest request,
//    		@RequestParam("username") String username,
//    		@RequestParam("password") String password) throws AuthenticationException {
////        Map<String, Object> model = new HashMap<String, Object>();
//    	
//    	logger.debug("username : " + username);
//    	logger.debug("password : " + password);
//    	
//    	
//    	authenticationService.authenticate(username, password);
//    	
//    	return new ModelAndView("signin-service", "model", model);
//    }
   
}