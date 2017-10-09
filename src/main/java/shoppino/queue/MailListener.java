package shoppino.queue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import openrecordz.domain.Person;
import openrecordz.domain.Tenant;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.mail.service.EmailMessageType;
import openrecordz.mail.service.MailService;
import shoppino.service.PersonService;
import shoppino.service.TenantService;
import shoppino.service.UrlService;



/**
 * @author Andrea Leo
 *
 */


public class MailListener implements MessageListener {

	private static final Log log = LogFactory.getLog(MailListener.class);

	
	@Autowired
	MailService mailService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	UrlService urlService;

	@Value("$shoppino{shoppino.url}")
	private String shoppinoUrl;
	
	
//	@Autowired
//	ProductService productService;
	
	@Autowired
	TenantService tenantService;

	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			
			try {
				String messageType=((MapMessage)message).getString("type");
				
				String to = ((MapMessage)message).getString("to");
				String subject = ((MapMessage)message).getString("subject");
				String mailType = ((MapMessage)message).getString("mailType");
				String tenantName = ((MapMessage)message).getString("tenantName");
				
				tenantService.setCurrentTenant(new Tenant(tenantName));
				
				if(messageType.equals(EmailMessageType.REGISTRATION.toString()))
				{
					log.debug("Received registration mail from queue to " + to + " and subject " + subject);
					
					Map<String, Object> properties = new HashMap<String, Object>();

					Person p = personService.getByEmail(to);
					Map<String, Object> mapPerson = new BeanMap(p); 
					properties.put("person", mapPerson);
					
					//email validation
					String url = urlService.save("/users/"+p.getUsername()+"/validate");
//					String url = urlService.save("/users/"+p.getUsername()+"/validate/f");
					log.debug("validationEmail : " +url);
					properties.put("validationEmail",url);
					
					
					properties.put("shoppinoUrl",shoppinoUrl);
					mailService.sendMail(to, subject, mailType, properties);
					log.debug("Registration email sent from queue with to " + to + " and subject " + subject);
				
				} 
				else if (messageType.equals(EmailMessageType.GENERIC.toString())) {
					
					
					String from = ((MapMessage)message).getString("from");
					String bcc = ((MapMessage)message).getString("bcc");
//					Map parameters = (Map)((MapMessage)message).getObject("parameters");
					String parametersAsString = ((MapMessage)message).getString("parametersAsString");
					 Map parameters = new HashMap();
//					 Map<String,String> parameters = new HashMap<String,String>();
					 

					 if (parametersAsString!=null && !parametersAsString.equals("")) {
						 log.debug("Deserializing email parameters : " + parametersAsString);
						 ObjectMapper mapper = new ObjectMapper();
					 
						//convert JSON string to Map
						 try {
							parameters = mapper.readValue(parametersAsString, 
							    new TypeReference<HashMap>(){});
//							new TypeReference<HashMap<String,String>>(){});
						} catch (IOException e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
							log.error("Error converting parametersAsString with value : " + parametersAsString +" to Json", e);
							
						}
				 
						
					 
					 }
					
					boolean isHtml = ((MapMessage)message).getBoolean("isHtml");
					String templateMailName = ((MapMessage)message).getString("templateMailName");
					
					log.debug("Generic email retrieved from queue with to " + to + " and subject " + subject + " with templateMailName " +templateMailName);
					
					mailService.sendMail(from, to, bcc, subject, templateMailName, parameters, isHtml);
					
				} else {
					log.error("Unknow messageType : " + messageType);
					throw new IllegalArgumentException("Unknow messageType : " + messageType);
				}
			
				 log.info("Mail sent from queue with to " + to + " and subject " + subject);
		} catch (JMSException e) {
			log.error("Error sending mail ", e);
		} catch (ResourceNotFoundException e) {
			log.error("Error sending mail ", e);
		}
		
		} else
			throw new IllegalArgumentException("Message must be of type MapMessage");
		}
}

