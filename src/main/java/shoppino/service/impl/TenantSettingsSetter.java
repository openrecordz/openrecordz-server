package shoppino.service.impl;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import openrecordz.domain.Statusable;
import openrecordz.domain.Tenant;

public class TenantSettingsSetter {

	public static String TENANT_SETTINGS_DISPLAYNAME_KEY = "tenants.settings.%s.displayname";
	
	public static String TENANT_SETTINGS_CURRENT_SITETYPE_KEY = "tenants.settings.%s.sitetype";
	
	public static String TENANT_SETTINGS_CURRENT_SITE_DEFAULT_STATUS_KEY = "tenants.settings.%s.defaultstatus";
	
	public static String TENANT_SETTINGS_CURRENT_SEND_REGISTRATION_EMAIL_KEY = "tenants.settings.%s.registration.send.email";
	public static boolean TENANT_SETTINGS_DEFAULT_SEND_REGISTRATION_EMAIL_VALUE = true;

	public static String TENANT_SETTINGS_USERS_SHOW_CROSS_KEY = "tenants.settings.%s.users.crosstenant.show";
//	public static boolean TENANT_SETTINGS_DEFAUL_USERS_SHOW_CROSS_VALUE = true;
	public static boolean TENANT_SETTINGS_DEFAUL_USERS_SHOW_CROSS_VALUE = false;
	
	public static String TENANT_SETTINGS_CURRENT_SEND_PRODUCT_ADDED_EMAIL_KEY = "tenants.settings.%s.product.added.send.email";
	public static boolean TENANT_SETTINGS_DEFAULT_SEND_PRODUCT_ADDED_EMAIL_VALUE = true;
	
	public static String TENANT_SETTINGS_CURRENT_SEND_FORGOT_PASSWORD_EMAIL_KEY = "tenants.settings.%s.signin.forgotpassword.send.email";
	public static boolean TENANT_SETTINGS_DEFAULT_SEND_FORGOT_PASSWORD_EMAIL_EMAIL_VALUE = true;
	
	
	private Log log = LogFactory.getLog(this.getClass());


	MessageSource messageSource;
	
	
	public String getTenantDisplayName(String tenantName) {
		//default
//		String currentTenantType = Tenant.PUBLIC_TENANT_TYPE;
		
		String currentTenantDisplayName = messageSource.getMessage(String.format(TENANT_SETTINGS_DISPLAYNAME_KEY,tenantName), null, null, Locale.getDefault());
		
		return currentTenantDisplayName;
	}
	
	public String getTenantType(String tenantName) {
		//default
//		String currentTenantType = Tenant.PUBLIC_TENANT_TYPE;
		
		String currentTenantType = messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SITETYPE_KEY,tenantName), null, Tenant.MODERATE_TENANT_TYPE, Locale.getDefault());
		
		return currentTenantType;
	}
	
	public Integer getTenantDefaultStatus(String tenantName) {

		
		Integer currentTenantDefaultStatus = Integer.valueOf(messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SITE_DEFAULT_STATUS_KEY,tenantName), null, Statusable.STATUS_VISIBLE.toString(), Locale.getDefault()));
		
		return currentTenantDefaultStatus;
	}

	
	public Boolean isSendRegistrationEmail(String tenantName) {

		String currentTenantSendRegistrationEmailAsString = messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SEND_REGISTRATION_EMAIL_KEY,tenantName), null, String.valueOf(TENANT_SETTINGS_DEFAULT_SEND_REGISTRATION_EMAIL_VALUE), Locale.getDefault());
		log.debug("currentTenantSendRegistrationEmailAsString "+ currentTenantSendRegistrationEmailAsString );
		
		boolean sendRegistrationEmail = Boolean.parseBoolean(currentTenantSendRegistrationEmailAsString); 
		log.debug("sendRegistrationEmail "+ sendRegistrationEmail );
		
		return sendRegistrationEmail;
		
	}
	
	
	public Boolean isCrossUsersEnabled(String tenantName) {

		String currentTenantCrossUsersEnabledAsString = messageSource.getMessage(String.format(TENANT_SETTINGS_USERS_SHOW_CROSS_KEY,tenantName), null, String.valueOf(TENANT_SETTINGS_DEFAUL_USERS_SHOW_CROSS_VALUE), Locale.getDefault());
		log.debug("TENANT_SETTINGS_DEFAUL_USERS_SHOW_CROSS_VALUE "+ TENANT_SETTINGS_DEFAUL_USERS_SHOW_CROSS_VALUE );
		
		boolean crossUsersEnabledAs = Boolean.parseBoolean(currentTenantCrossUsersEnabledAsString); 
		log.debug("crossUsersEnabledAs "+ crossUsersEnabledAs );
		
		return crossUsersEnabledAs;
		
	}
	
	
	public Boolean isSendEmailAfterProductAdded(String tenantName) {

		String currentTenantSendEmailAfterProductAddedAsString = messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SEND_PRODUCT_ADDED_EMAIL_KEY,tenantName), null, String.valueOf(TENANT_SETTINGS_DEFAULT_SEND_PRODUCT_ADDED_EMAIL_VALUE), Locale.getDefault());
		log.debug("currentTenantSendEmailAfterProductAddedAsString "+ currentTenantSendEmailAfterProductAddedAsString );
		
		boolean sendEmailAfterProductAdded = Boolean.parseBoolean(currentTenantSendEmailAfterProductAddedAsString); 
		log.debug("sendEmailAfterProductAdded "+ sendEmailAfterProductAdded );
		
		return sendEmailAfterProductAdded;
		
	}
	
	
	
	public Boolean isSendEmailForgotPasswordEnabled(String tenantName) {

		String currentTenantSendEmailForgotPasswordEnabledAsString = messageSource.getMessage(String.format(TENANT_SETTINGS_CURRENT_SEND_FORGOT_PASSWORD_EMAIL_KEY,tenantName), null, String.valueOf(TENANT_SETTINGS_DEFAULT_SEND_FORGOT_PASSWORD_EMAIL_EMAIL_VALUE), Locale.getDefault());
		log.debug("currentTenantSendEmailForgotPasswordEnabledAsString "+ currentTenantSendEmailForgotPasswordEnabledAsString );
		
		boolean sendEmaillForgotPasswordEnabled = Boolean.parseBoolean(currentTenantSendEmailForgotPasswordEnabledAsString); 
		log.debug("sendEmaillForgotPasswordEnabled "+ sendEmaillForgotPasswordEnabled );
		
		return sendEmaillForgotPasswordEnabled;
		
	}
	
	

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
}
