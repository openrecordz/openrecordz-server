package openrecordz.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import openrecordz.domain.Person;
import openrecordz.domain.Tenant;
import openrecordz.domain.validator.UserRegistrationValidation;
import openrecordz.exception.EmailAlreadyInUseException;
import openrecordz.exception.OpenRecordzException;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.TenantAlreadyInUseException;
import openrecordz.security.domain.User;
import openrecordz.security.exception.AuthenticationException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.exception.UsernameAlreadyInUseException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.security.service.AuthorizationService;
import openrecordz.security.service.UserService;
import openrecordz.service.CustomDataService;
import openrecordz.service.EnvironmentService;
import openrecordz.service.PersonService;
import openrecordz.service.RDBService;
import openrecordz.service.RegistrationService;
import openrecordz.service.TenantService;
import openrecordz.web.exception.ValidationException;
import openrecordz.web.form.UserRegistrationForm;

@Controller
public class TenantsServiceController implements BaseServiceController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	public static final String REGISTER_URL = "/tenants/registerapp";
	
	
	@Autowired
	RegistrationService registrationService;

//	@Autowired
//	SocialService socialService;
	
	@Autowired
	AuthenticationService authenticationService ;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	RDBService rdbService;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	AuthorizationService authorizationService;
	
	@Autowired
	EnvironmentService environmentService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CustomDataService customDataService;
	
	@Autowired
	UserRegistrationValidation userRegistrationValidation;

	@Value("$platform{mysql.database.uri}")
	String databaseUri;
	
	@RequestMapping(value = "/tenants/me", method = RequestMethod.GET)
	public @ResponseBody List<String> list(WebRequest request, Model model) throws ResourceNotFoundException, UserNotExistsException {
		
		List<String> tenants= new ArrayList<String>();

		Person p = personService.getByUsername(authenticationService.getCurrentLoggedUsername());
			
		for (String ten : p.getTenants()) {
				if (!ten.startsWith("_"))
					tenants.add(ten);
		}
			
		Collections.reverse(tenants);
		
		return tenants;
    }
	
	@RequestMapping(value = "/tenants", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> listAll(WebRequest request, Model model,
			@RequestParam(value="featured", required=false, defaultValue="false") Boolean featured) throws SQLException  {
		
		if (featured) {
			return rdbService.select(databaseUri, "select * from tenant where featured="+featured+" order by created_on desc;");
		}else {
			return rdbService.select(databaseUri, "select * from tenant order by created_on desc;");
		}
	}
	
	
	
			
			
//	curl -X POST -u admin:admin -d 'name=test23&description=test23escr' http://localhost:9090/service/v1/tenants
	@RequestMapping(value = "/tenants", method = RequestMethod.POST)
	public @ResponseBody String registerAppAdd(
			HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("name") String tenantName,
			@RequestParam(value="description", required=false, defaultValue="") String tenantDescription
		) throws SQLException, UserNotExistsException, ResourceNotFoundException, AuthenticationException, ValidationException {
		
		logger.debug("tenantName: " + tenantName);
		logger.debug("tenantDescription: " + tenantDescription);
		
		if (tenantName.equals("") || tenantName.startsWith("_"))
			throw new ValidationException("App name not specified");
		

		if (tenantName.matches("^[a-zA-Z0-9]*$")==false)
			throw new ValidationException("App Name can't contains special charset");
		
		
		if (Integer.parseInt(rdbService.select(databaseUri, "select count(*) as mycount from tenant where name='"+tenantName+"'").get(0).get("mycount")) ==0){
			rdbService.update(databaseUri, "insert into tenant(name,description) values('"+tenantName+"', '"+tenantDescription+"')");

//		if (Integer.parseInt(rdbService.select("jdbc:mysql://localhost/"+databaseName+"?user="+databaseUser+"&password="+databasePassword, "select count(*) as mycount from tenant where name='"+tenantadd+"'").get(0).get("mycount")) ==0){
//			rdbService.update("jdbc:mysql://localhost/"+databaseName+"?user="+databaseUser+"&password="+databasePassword, "insert into tenant values('"+tenantadd+"')");
			
			final String curTenantName = tenantService.getCurrentTenantName();
			
			tenantService.setCurrentTenant(new Tenant(tenantName));
			personService.joinCurrentTenant(authenticationService.getCurrentLoggedUsername());
			
			
			 
//			 if (!tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE))
//				 finalRole=role+"@"+tenantService.getCurrentTenantName();
			 final String finalUsername = authenticationService.getCurrentLoggedUsername();
			Boolean myresult = authenticationService.runAs(new AuthenticationService.RunAsWork<Boolean>() {
				@Override
		        public Boolean doWork() throws Exception {
					String finalRole = "ROLE_ADMIN@" + tenantService.getCurrentTenantName();
					authorizationService.addRole(finalUsername, finalRole);
					
//					finalRole = "ROLE_ADMIN@" + curTenantName;
//					authorizationService.addRole(authenticationService.getCurrentLoggedUsername(), finalRole);
					
		            return true;
		        }
			}, authenticationService.getCurrentLoggedUsername(),"admin");
			
			logger.info("Tenant "+ tenantName + " created");
			
			return "{\"status\":\"success\"}";
		}else {
			throw new ValidationException("App name : " + tenantName +" already in use");
		}
		
		
	}

	
	
	
	
	
	
	
	
	
	

    @Deprecated
	@RequestMapping(value = REGISTER_URL, method = RequestMethod.POST)
	public @ResponseBody String registerApp(
			HttpServletRequest request, HttpServletResponse response,
			WebRequest webRequest,
//			@Valid UserRegistrationForm userRegistration,
			UserRegistrationForm userRegistration,
		BindingResult result) throws SQLException, UserNotExistsException, ValidationException, EmailAlreadyInUseException, UsernameAlreadyInUseException,  AuthenticationException, TenantAlreadyInUseException, OpenRecordzException {
      
		userRegistrationValidation.validate(userRegistration, result);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "tenant",
				null, "Il campo Nome App è richiesto.");
		
		if (userRegistration.getTenant().startsWith("_"))
			 result.rejectValue("tenant", null, "Il nome della App non può iniziare con un carattere \"_\"");
		
		if (result.hasErrors()) {
            throw new ValidationException("registerApp validation error. " + result.toString());
        }
		String appName = userRegistration.getTenant();
		
		//if (Integer.parseInt(rdbService.select("jdbc:mysql://localhost/"+databaseName+"?user="+databaseUser+"&password="+databasePassword, "select count(*) as mycount from tenant where name='"+appName+"'").get(0).get("mycount")) >=1){
		if (Integer.parseInt(rdbService.select(databaseUri, "select count(*) as mycount from tenant where name='"+appName+"'").get(0).get("mycount")) >=1){
//			throw new TenantAlreadyInUseException("App name : " + appName +" already in use111");
			throw new ValidationException("App name : " + appName +" already in use");
		}
		
		String username = null;
//		try {
			
//			if (userRegistration.isFromSocial()==true){
//				logger.debug("From social. Setting auto generated password");
//				String newPassword=RandomStringUtils.randomAlphanumeric(6);
//				userRegistration.setPassword(newPassword);
//				logger.debug("From social. Setted auto generated password : " + newPassword);
//			}
			
			if (appName.matches("^[a-zA-Z0-9]*$")==false)
				throw new ValidationException("App Name can't contains special charset");
			
			username = registrationService.register(userRegistration.getUsername(), userRegistration.getFullName(), userRegistration.getEmail(), userRegistration.getPassword());
			
			//autologin
			authenticationService.authenticate(username);		
			
			
			
			
			
//			if (Integer.parseInt(rdbService.select("jdbc:mysql://localhost/shoppino?user=root&password=root", "select count(*) as mycount from tenant where name='"+appName+"'").get(0).get("mycount")) ==0){
//				rdbService.update("jdbc:mysql://localhost/shoppino?user=root&password=root", "insert into tenant values('"+appName+"')");
				
			
				rdbService.update(databaseUri, "insert into tenant values('"+appName+"')");
//				rdbService.update("jdbc:mysql://localhost/"+databaseName+"?user="+databaseUser+"&password="+databasePassword, "insert into tenant values('"+appName+"'⁄)");							
				
				final String curTenantName = tenantService.getCurrentTenantName();
				
				//faccio il tenant moderato di default
				//cambiato TenantSettingsSeetter
//				cosi : String currentTenantType = messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SITETYPE_KEY,tenantName), null, Tenant.MODERATE_TENANT_TYPE, Locale.getDefault());
				
//				CustomData tenantType= new CustomDataImpl();
//				tenantType.put("code", String.format(TenantSettingsSetter.TENANT_SETTINGS_CURRENT_SITETYPE_KEY,curTenantName));
//				tenantType.put("message", "moderate");
//				tenantType.put("type", "_message_source");
//				customDataService.add(tenantType);
				
				
				
				tenantService.setCurrentTenant(new Tenant(appName));
				personService.joinCurrentTenant(username);
				
				
				 
//				 if (!tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE))
//					 finalRole=role+"@"+tenantService.getCurrentTenantName();
//				 
				final String finalUsername = username;
				Boolean myresult = authenticationService.runAs(new AuthenticationService.RunAsWork<Boolean>() {
					@Override
			        public Boolean doWork() throws Exception {
						String finalRole = "ROLE_ADMIN@" + tenantService.getCurrentTenantName();
						authorizationService.addRole(finalUsername, finalRole);
						
//						finalRole = "ROLE_ADMIN@" + curTenantName;
//						authorizationService.addRole(finalUsername, finalRole);
			            return true;
			        }
				}, authenticationService.getCurrentLoggedUsername(), "admin");
				
				 
				 
//				tenantService.setCurrentTenant(new Tenant(curTenantName));
				
			
		
//			ProviderSignInUtils.handlePostSignUp(username, webRequest);
								
//		} catch (UsernameAlreadyInUseException e) {
//			result.rejectValue("username", "user.duplicateUsername", "Username already in use");
//			
//			return "registerapp";
//		} catch (EmailAlreadyInUseException e) {
////				if (userRegistration.isFromSocial())
////					result.rejectValue("email", "user.duplicateEmailfromSocial");
////				else 
//					result.rejectValue("email", "user.duplicateEmail", "Email already in use");
//				
//			userRegistration.setFromSocial(false);
//			return "registerapp";
//		}catch (AuthenticationException ae) {
//			
//			return "redirect:/";
//		} catch (ShoppinoException se) {//catch email already in use. but is validated before by userRegistrationValidation.validate
//			
//			return "redirect:/";
//		}

		logger.info("User " + username + " registered. Tenant "+ appName + " created");
		
		User user = userService.getByUsername(username);
		String httpAuth = user.getUsername() + ":" + user.getPassword();
		logger.debug("http auth : " + httpAuth);
		
		String encoding = Base64.encodeBase64String(httpAuth.getBytes());
		logger.debug("encoded basic auth : " + encoding);
		
		
//		{"status":"success", "channel":"signup", "username":"${shp:escapeJSON(user.username)}", "basicAuth":"${encoding}"}
		
//		return "redirect:"+environmentService.getTenantUrl()+"/dashboard";
		return "{\"status\":\"success\",\"username\":\""+username+"\",\"basicAuth\":\""+encoding+"\"}";
						
	}
//	  
//	@RequestMapping(value = "/registerapp/add", method = RequestMethod.GET)
//	public String goRegisterAppAdd(){
//		return "registerapp-add";
//	}
//
	
	
	
	
   
}