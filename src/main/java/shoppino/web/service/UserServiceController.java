package shoppino.web.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.domain.Person;
import openrecordz.domain.Tenant;
import shoppino.exception.ResourceNotFoundException;
import shoppino.mail.service.MailService;
import shoppino.security.exception.AuthenticationException;
import shoppino.security.exception.UserNotExistsException;
import shoppino.security.service.AuthenticationService;
import shoppino.security.service.AuthorizationService;
import shoppino.security.service.UserService;
import shoppino.service.PersonService;
import shoppino.service.TenantService;
import shoppino.service.impl.TenantSettingsSetter;

@Controller
public class UserServiceController  implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	private PersonService personService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private TenantSettingsSetter tenantSettingsSetter;
	
	
	@Autowired
	TenantService tenantService;

	 @RequestMapping(value = "/users/me/forgot_password", method = RequestMethod.POST)
	    public @ResponseBody String passwordForgot(@RequestParam("email") String email) throws UserNotExistsException, ResourceNotFoundException {
	    	
//	    	passwordForgotValidation.validate(passwordForgotForm, result);
	    	

	    	
	    	Person pToModify = personService.getByEmail(email);
	    	
	    	String newPassword=RandomStringUtils.randomAlphanumeric(6);
	    	logger.info("generated new password for user : " + pToModify.getUsername() + " with passwd : "+ newPassword);
	    	

	    	userService.update(pToModify.getUsername(), newPassword);
	    	
	    	if (tenantSettingsSetter.isSendEmailForgotPasswordEnabled(tenantService.getCurrentTenantName())) {
		    	//send mail
		    	try {
			    	Map parameters = new HashMap();    	
					parameters.put("person", pToModify);		
			    	parameters.put("newPassword", newPassword);
			    	mailService.sendMail(email, null, "Password dimenticata", "forgot_password",parameters, false);
//			    	mailService.sendMail(email, null, "Password dimenticata", "/email_templates/forgot_password",parameters, false);
//			    	mailService.sendMail(email, "Password dimenticata", "forgot_password", parameters);
		    	} catch (Exception e) {
					logger.warn("Error sending password_forgot email for user : " + pToModify.getUsername(),e);
				}
	    	}else {
	    		logger.info("Forgot password mail is disabled for tenant : " + tenantService.getCurrentTenantName());
			}
		    
		    	
		    	 return "{success:true}";
		    	    	
	    }
    
   
	 @RequestMapping(value = "/users/me/change_password", method = RequestMethod.POST)
	    public @ResponseBody String change_Password(
	    		@RequestParam("old_password") String oldPassword
	    		,@RequestParam("new_password") String newPassword) throws UserNotExistsException, AuthenticationException {
	    	
	    	
		 	authenticationService.authenticate(authenticationService.getCurrentLoggedUsername(), oldPassword);
		 
	    	userService.update(authenticationService.getCurrentLoggedUsername(), newPassword);
	        
	    	logger.info("Password changed for user : " + authenticationService.getCurrentLoggedUsername()+ ". New password : " + newPassword);
	    	
	    	
	    	String httpAuth = authenticationService.getCurrentLoggedUsername() + ":" + newPassword;
			logger.debug("http auth : " + httpAuth);
			
			String encoding = Base64.encodeBase64String(httpAuth.getBytes());
			logger.debug("encoded basic auth : " + encoding);
			String decoded = new String(Base64.decodeBase64(encoding));
			logger.debug("decoded basic auth : " + decoded);	
			
	    	 return "{success:true,basicAuth:\""+encoding+"\"}";
	    }
	 
	 @RequestMapping(value = "/users/addrole", method = RequestMethod.POST)
	 @Deprecated //use saverole
	    public @ResponseBody String addrole(@RequestParam("username") String username,@RequestParam("role") String role) throws UserNotExistsException, ResourceNotFoundException {
		 
		 logger.debug("username : " + username);
		 logger.debug("role : " + role);
		 
		 String finalRole = role;
		 
		 if (!tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE))
			 finalRole=role+"@"+tenantService.getCurrentTenantName();
		 
		 authorizationService.addRole(username, finalRole);
		 
		 logger.info("finalRole :" + finalRole);
		 
	   	 return "{success:true}";
	 }
	 
	 
	 
	 @RequestMapping(value = "/users/getroles", method = RequestMethod.GET)
	    public @ResponseBody Collection<? extends GrantedAuthority> getRoles(@RequestParam("username") String username) throws UserNotExistsException, ResourceNotFoundException {
		 
	
		 
		return authorizationService.getAuthorities(username);
		 	 
	 }
	 
	 
	 
	 @RequestMapping(value = "/users/saveroles", method = RequestMethod.POST)
	    public @ResponseBody String saverole(@RequestParam("username") String username,@RequestParam("roles") List<String> roles) throws UserNotExistsException, ResourceNotFoundException {
		 
		 logger.debug("username : " + username);
		 logger.debug("roles : " + roles);
		 
		 logger.debug("CurrentLoggedUsername: "+ authenticationService.getCurrentLoggedUsername());
		 authorizationService.deleteRoles(username);
		 
		 for (String role : roles) {
			 String finalRole = role;
			 
			 if (!tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE))
				 finalRole=role+"@"+tenantService.getCurrentTenantName();
			 
			 authorizationService.addRole(username, finalRole);
			 
			 logger.info("finalRole :" + finalRole);
		}
		
		 
	   	 return "{success:true}";
	 }
    
}