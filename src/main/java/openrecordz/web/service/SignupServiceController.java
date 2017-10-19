package openrecordz.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import openrecordz.domain.Person;
import openrecordz.domain.validator.UserRegistrationServiceValidation;
import openrecordz.exception.EmailAlreadyInUseException;
import openrecordz.exception.OpenRecordzException;
import openrecordz.security.domain.User;
import openrecordz.security.exception.AuthenticationException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.exception.UsernameAlreadyInUseException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.security.service.UserService;
import openrecordz.service.ImageService;
import openrecordz.service.PersonService;
import openrecordz.service.RegistrationService;
import openrecordz.service.TenantService;
import openrecordz.web.exception.ValidationException;
import openrecordz.web.form.UserRegistrationForm;

@Controller
//@RequestMapping("/{tenant}/service/v1")
public class SignupServiceController implements BaseServiceController {

	@Autowired
//	UserRegistrationValidation userRegistrationValidation;
	UserRegistrationServiceValidation userRegistrationServiceValidation;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	RegistrationService registrationService;
	
//	@Autowired
//	SocialService socialService;
	
	@Autowired
	TenantService tenantService;
	
	//link fb connectio to the user
//	@Autowired
//	ConnectionFactoryLocator connectionFactoryLocator;
	//link fb connectio to the user
//	@Autowired
//	UsersConnectionRepository usersConnectionRepository;
	
	@Autowired
	ImageService imageService;
	
	@Value("$platform{person.avatar.path}")
	String personPhotoPath;
	
	@Value("$platform{person.avatar.name}")
	String personPhotoName;
	
	protected final Log logger = LogFactory.getLog(getClass());	
	
	
//	@Deprecated
//	@RequestMapping(value = "/signup", method = RequestMethod.GET)
//    public ModelAndView showForm() {        
//       return new ModelAndView("new-signup-service", "userRegistrationForm", new UserRegistrationForm());
//    }
//	
	@Deprecated
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public @ResponseBody Map processSubmit(
			HttpServletRequest request, HttpServletResponse response,
			WebRequest webRequest,
			UserRegistrationForm userRegistration,
		BindingResult result, 
		Model model) 
				throws UsernameAlreadyInUseException, EmailAlreadyInUseException, 
		AuthenticationException, ValidationException, 
		UserNotExistsException, OpenRecordzException {
      
		
		String accessToken = request.getParameter("facebookToken");
		logger.debug("facebookToken : " + accessToken);
		
		//se c'è accessToken e la password è vuota setFromSocial=true
		if (accessToken!=null && !accessToken.equals("") && (userRegistration.getPassword()==null || (userRegistration.getPassword()!=null && userRegistration.getPassword().equals("") ) ) )  {			logger.debug("From social. Setting auto generated password");
			userRegistration.setFromSocial(true);
		
		}
		
//		userRegistrationValidation.validate(userRegistration, result);
		userRegistrationServiceValidation.validate(userRegistration, result);
		
		if (result.hasErrors()) {
            throw new ValidationException("Signup validation error. " + result.toString());
        }
		
		//if fromSocial=true than autogenerate the password
		if (userRegistration.isFromSocial()==true) {
			String newPassword=RandomStringUtils.randomAlphanumeric(6);
			userRegistration.setPassword(newPassword);
			logger.debug("From social. Setted auto generated password : " + newPassword);
		}
						
		String username = registrationService.register(userRegistration.getUsername(), userRegistration.getFullName(), userRegistration.getEmail(), userRegistration.getPassword());
			
		//link fb connectio to the user
//			if (accessToken!=null && !accessToken.equals("")) {
//				try {
//					AccessGrant accessGrant = new AccessGrant(accessToken);
//					OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(Facebook.class);
//					Connection<?> connection = connectionFactory.createConnection(accessGrant);					
//					ConnectionRepository repository = usersConnectionRepository.createConnectionRepository(username);
//					repository.addConnection(connection);
//					logger.info("Linked a social connection to the user " + username);
//				}catch (Exception e) {
//					logger.error("Social connection error when linking social with user : "  +username , e );
//				}
//			}
			
//			ProviderSignInUtils.handlePostSignUp(userRegistration.getUsername(), webRequest);

			//autologin .. must be here before registrationService.updatePhotoFromSocial because connectionRepository.findAllConnections() use current user to find connections
			authenticationService.authenticate(username);			
			
			//update person photo from social. this call must be here
//			try {
//				socialService.updatePhotoFromSocial(username);
//			} catch (ShoppinoException e) {
//				logger.error("Error updating person photo from social for user : " + username);
//			}
			
			
			
			logger.info("Registration complete for user : " + username);	 
			
			
		
		
			User user = userService.getByUsername(username);
			String httpAuth = user.getUsername() + ":" + user.getPassword();
			logger.debug("http auth : " + httpAuth);
			
			String encoding = Base64.encodeBase64String(httpAuth.getBytes());
			logger.debug("encoded basic auth : " + encoding);
			
			Map returnValue = new HashMap();
			returnValue.put("basicAuth", encoding);
			returnValue.put("username", user.getUsername());
			
			return returnValue;							
	}
	
	
	
	
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
//	@RequestMapping(value = "/signupwithphoto", method = RequestMethod.POST)
	public ModelAndView processSubmitWithPhoto(
			HttpServletRequest request, HttpServletResponse response,
			WebRequest webRequest,
//			@Valid UserRegistrationForm userRegistration,
			UserRegistrationForm userRegistration,
			@RequestParam("photofile") MultipartFile photo,
		BindingResult result, 
		Model model) throws UsernameAlreadyInUseException, EmailAlreadyInUseException, AuthenticationException, ValidationException, UserNotExistsException, OpenRecordzException, IOException {
      
		logger.debug("Calling processSubmitWithPhoto");
			
		
		String accessToken = request.getParameter("facebookToken");
		logger.debug("facebookToken : " + accessToken);
		
		//se c'è accessToken e la password è vuota setFromSocial=true
		if (accessToken!=null && !accessToken.equals("") && (userRegistration.getPassword()==null || (userRegistration.getPassword()!=null && userRegistration.getPassword().equals("") ) ) )  {			logger.debug("From social. Setting auto generated password");
			userRegistration.setFromSocial(true);
		
		}
		
		
		userRegistrationServiceValidation.validate(userRegistration, result);
		
		if (result.hasErrors()) {
            throw new ValidationException("Signup validation error. " + result.toString());
        }
					
		
		
		//if fromSocial=true than autogenerate the password
		if (userRegistration.isFromSocial()==true) {
			String newPassword=RandomStringUtils.randomAlphanumeric(6);
			userRegistration.setPassword(newPassword);
			logger.debug("From social. Setted auto generated password : " + newPassword);
		}
				
		String username = registrationService.register(userRegistration.getUsername(), userRegistration.getFullName(), userRegistration.getEmail(), userRegistration.getPassword());
			
		//link fb connectio to the user			
//			if (accessToken!=null && !accessToken.equals("")) {
//				try {
//					AccessGrant accessGrant = new AccessGrant(accessToken);
//					OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(Facebook.class);
//					Connection<?> connection = connectionFactory.createConnection(accessGrant);					
//					ConnectionRepository repository = usersConnectionRepository.createConnectionRepository(username);
//					repository.addConnection(connection);
//					logger.info("Linked a social connection to the user " + username);
//				}catch (Exception e) {
//					logger.error("Social connection error when linking social with user : "  + username , e );
//				}
//			}
			
//			ProviderSignInUtils.handlePostSignUp(userRegistration.getUsername(), webRequest);

			//autologin .. must be here before registrationService.updatePhotoFromSocial because connectionRepository.findAllConnections() use current user to find connections
			authenticationService.authenticate(username);			
			
//				//update person photo from social. this call must be here
//				try {
//					socialService.updatePhotoFromSocial(userRegistration.getUsername());
//				} catch (ShoppinoException e) {
//					logger.error("Error updating person photo from social for user : " + userRegistration.getUsername());
//				}
			
			if (!photo.isEmpty()) {	  
				Person person = personService.updateLogo(username, personPhotoName, photo.getInputStream());				
			}
			
			logger.info("Registration complete for user : " + username);	 
			
			
		
		
			User user = userService.getByUsername(username);
			String httpAuth = user.getUsername() + ":" + user.getPassword();
			logger.debug("http auth : " + httpAuth);
			
			String encoding = Base64.encodeBase64String(httpAuth.getBytes());
			logger.debug("encoded basic auth : " + encoding);
			
			model.addAttribute("encoding", encoding);
			model.addAttribute("user", user);
			
			return new ModelAndView("signup-service", "model", model);
		
	}
   
}