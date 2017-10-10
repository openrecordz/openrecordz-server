package openrecordz.aspect;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import openrecordz.domain.Person;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.mail.service.EmailSettingsConstant;
import openrecordz.mail.service.MailService;
import openrecordz.service.PersonService;
import openrecordz.service.TenantService;
import openrecordz.service.UrlService;
import openrecordz.service.impl.TenantSettingsSetter;

public class RegistrationMailSender {
	
	@Autowired
	MailService mailService;
	
	@Autowired
	PersonService personService;
	
//	@Autowired
//	@Qualifier("tenantSettingMappingMessageSource")
//	MessageSource messageSource;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	private TenantSettingsSetter tenantSettingsSetter;
	
	@Autowired
	@Qualifier(value="tenantSettingMappingCustomDataMessageSource")
	MessageSource messageSource;
	
	
	@Autowired
	UrlService urlService;
	
	@Value("$mail{mail.default.from}")
	private String defaultFrom;
	


//	not used...used generic from and bcc
	public static String TENANT_SETTINGS_MAIL_REGISTRATION_FROM_KEY = "tenants.settings.%s.registration.send.email.from";
//	not used...used generic from and bcc
	public static String TENANT_SETTINGS_MAIL_REGISTRATION_BCC_KEY = "tenants.settings.%s.registration.send.email.bcc";
	
	
	public static String TENANT_SETTINGS_MAIL_REGISTRATION_SUBJECT_KEY = "tenants.settings.%s.registration.send.email.subject";
	
	public static String TENANT_SETTINGS_MAIL_REGISTRATION_WELCOME_KEY = "tenants.settings.%s.registration.send.email.welcome";
	
	private static Log log = LogFactory.getLog(RegistrationMailSender.class);	
	
	public void sendMail(String username, String fullName, String email, String password, String newUsername) throws ResourceNotFoundException  {
		
		
		if (tenantSettingsSetter.isSendRegistrationEmail(tenantService.getCurrentTenantName())) {
			log.debug("sending registration mail for user : " + newUsername);
			
			Person p = personService.getByUsername(newUsername);
			//send mail
	    	try {
	    		

		    	Map params = new HashMap();
		    	
		    	params.put("person", p);
		    	
		    	String url = urlService.save("/users/"+p.getUsername()+"/validate?goto=http://www.openrecordz.com/email-verificata/");
				log.debug("validationEmail : " +url);
				params.put("validationEmail",url);
		    	
				params.put("tenant-email-welcome", 
						messageSource.getMessage(String.format(TENANT_SETTINGS_MAIL_REGISTRATION_WELCOME_KEY,tenantService.getCurrentTenantName()), null, null,Locale.getDefault()));
				
	    		mailService.sendQueuedMail(
	    				messageSource.getMessage(String.format(EmailSettingsConstant.TENANT_SETTINGS_MAIL_FROM_KEY,tenantService.getCurrentTenantName()), null, defaultFrom,Locale.getDefault()),
//	    				messageSource.getMessage(String.format(TENANT_SETTINGS_MAIL_REGISTRATION_FROM_KEY,tenantService.getCurrentTenantName()), null, null,Locale.getDefault()),
	    				p.getEmail(),     				
	    				messageSource.getMessage(String.format(EmailSettingsConstant.TENANT_SETTINGS_MAIL_BCC_KEY,tenantService.getCurrentTenantName()), null, null, Locale.getDefault()),
//	    				messageSource.getMessage(String.format(TENANT_SETTINGS_MAIL_REGISTRATION_BCC_KEY,tenantService.getCurrentTenantName()), null, null, Locale.getDefault()),
	    				messageSource.getMessage(String.format(TENANT_SETTINGS_MAIL_REGISTRATION_SUBJECT_KEY,tenantService.getCurrentTenantName()), null, "Benvenuto", Locale.getDefault()),
	    				"registration",
	    				params,
	    				true);
	    		
//	    		mailService.sendQueuedMail(p.getEmail(), null, EmailMessageType.REGISTRATION);
		    	
	    	} catch (Exception e) {
				log.warn("Error sending registration email for user : " + newUsername+ " and email: " + email, e);
			}
			
			log.info("Registration mail sent for user "+ newUsername + " and email : " + email);
		}else {
			log.info("Registration mail is disabled for tenant : " + tenantService.getCurrentTenantName());
		}
	}
		
}
