package shoppino.messagesource.customdata.mongodb;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;

import openrecordz.domain.customdata.CustomData;
import shoppino.security.service.AuthenticationService;
import shoppino.service.CustomDataService;

public class CustomDataMessageSource extends AbstractMessageSource implements MessageSource {

protected Log log = LogFactory.getLog(getClass());
//	
//	@Autowired
//	@Qualifier(value="customDataServiceWithCheckDisabled")
	private CustomDataService customDataService;
	
	private AuthenticationService authenticationService;
	
//	protected List<String> basenames = new ArrayList<String>();
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
//		for (String basename : basenames) {
//            List<String> paths = getPath(locale);
//            for (String loc : paths) {
		final String codeFinal = code;
		
		log.debug("code : " + code);
//		try {
//			List<CustomData> messages  = authenticationService.runAs(new AuthenticationService.RunAsWork<List<CustomData>>() {
//				@Override
//			    public List<CustomData> doWork() throws Exception {
//					return customDataService.findByQuery("{\"code\":\""+codeFinal+"\"}", shoppino.domain.MessageSource.MESSAGE_SOURCE_CLASS_NAME);
//			    }
//				//TODO does it correct to log with admin?
//			}, authenticationService.getCurrentLoggedUsername(),"admin");
	
			List<CustomData> messages =	customDataService.findByQueryInternal("{\"code\":\""+codeFinal+"\"}", openrecordz.domain.MessageSource.MESSAGE_SOURCE_CLASS_NAME);
			
			if (messages!=null && messages.size()>0){
				if (messages.size()==1){
					log.info("found message with code : " + code + " and value : " + messages.get(0).get("message").toString());
					return new MessageFormat((messages.get(0).get("message").toString() != null ? messages.get(0).get("message").toString() : ""), locale);
				} else
					throw new RuntimeException("There are more than one message for this code : " + code);
				
			}
//		} catch (AuthenticationException e) {
//			log.error("error resolveCode",e);
//			
//		}
//                Map<String, MessageFormat> formatMap = messages.get(basename + loc);
//                if (formatMap != null) {
//                    MessageFormat format = formatMap.get(code);
//                    if (format != null) {
//                        return format;
//                    }
//                }
////            }
//        }

		log.debug("message with code : " + code + " not found");
		
        return null;
	}

	public CustomDataService getCustomDataService() {
		return customDataService;
	}

	public void setCustomDataService(CustomDataService customDataService) {
		this.customDataService = customDataService;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	
}
