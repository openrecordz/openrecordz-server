
package shoppino.queue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import shoppino.mail.service.EmailMessageType;
import shoppino.service.TenantService;

/**
 * @author Lorenzo
 *
 */
public class JmsEmailQueueSender {
	private JmsTemplate jmsTemplate;
	private String queue;
	
	@Autowired
	TenantService tenantService;
	
	private static Log log = LogFactory.getLog(JmsEmailQueueSender.class);	

	
//	protected String text;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		
	}
	
	public void setConnectionFactory(ConnectionFactory cf) {
		this.jmsTemplate = new JmsTemplate(cf);
	}
	
	public void setQueue(String queue) {
		this.queue = queue;
	}
	
	
//	public void sendWithConversionRegistrationRequest(RegistrationRequest registrationRequest, String ticket, String subject) {
//		
//	    Map<String, String> map = new HashMap<String, String>();
//	    map.put("type",EmailMessageType.REGISTRATIONCONFIRMATION.toString());
//	    //Map<String, String> context = new HashMap<String, String>();
//	    map.put("Name", registrationRequest.getFirstName());
//	    map.put("Surname", registrationRequest.getLastName());
//	    map.put("Password", registrationRequest.getPassword());
//	    map.put("Username", registrationRequest.getUsername());
//	    map.put("ticket", ticket);
//	    map.put("contextPath", confirmRegistration);
//	    map.put("Subject",subject);
//		//map.put("object",context);
//	    
//	    
//	    this.jmsTemplate.convertAndSend(this.queue, map);
//	}
	
	public void sendRegistrationMail(String to, String subject) {
		
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("type", EmailMessageType.REGISTRATION.toString());
	    map.put("to", to);
	    map.put("subject", subject);
	    map.put("mailType", "registration");
	    map.put("tenantName", tenantService.getCurrentTenantName());	   
//	    map.put("parameters", parameters);	 
	    
	    this.jmsTemplate.convertAndSend(this.queue, map);
	}

	
	public void sendLikeMail(String to, String refId, String from, String subject) {
		
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("type", EmailMessageType.LIKE.toString());
	    map.put("to", to);
	    map.put("subject", subject);
	    map.put("refId", refId);
	    map.put("from", from);
	    map.put("mailType", "like");	    
	    map.put("tenantName", tenantService.getCurrentTenantName());
//	    map.put("parameters", parameters);	 
	    
	    this.jmsTemplate.convertAndSend(this.queue, map);
	}
	
	
public void sendAbuseMail(String to, String subject) {
		
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("type", EmailMessageType.ABUSE.toString());
	    map.put("to", to);
	    map.put("subject", subject);	    
	    map.put("mailType", "abuse");	    
	    map.put("tenantName", tenantService.getCurrentTenantName());
	    this.jmsTemplate.convertAndSend(this.queue, map);
	}


public void sendProductCreatedMail(String to, String productId, String subject) {
		
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("type", EmailMessageType.PRODUCT_CREATED.toString());
	    map.put("to", to);
	    map.put("subject", subject);
	    map.put("productId", productId);	
	    map.put("mailType", "product_created");	    
	    map.put("tenantName", tenantService.getCurrentTenantName());
	    this.jmsTemplate.convertAndSend(this.queue, map);
	}



public void sendMail(String from, String to, String bcc, String subject, String templateMailName, Map parameters, boolean isHtml) {
	
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("type", EmailMessageType.GENERIC.toString());
    map.put("from", from);
    map.put("to", to);
    map.put("bcc", bcc);
    
    map.put("subject", subject);
    map.put("templateMailName", templateMailName);	    
    
    String parametersAsString = null;
    if (parameters!=null && parameters.size()>0) {
		ObjectMapper mapper = new ObjectMapper();
		
		//convert JSON string to Map
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		log.debug("Serializing email parameters: " + parameters.toString());
		try {
			mapper.writeValue(os, parameters);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//		e.printStackTrace();
			log.error("Error serializing email parameter. Parameter Map : " + parameters.toString(), e);
		}
		parametersAsString = new String(os.toByteArray());
    }
    map.put("parametersAsString", parametersAsString);	
    map.put("isHtml", isHtml);
    map.put("tenantName", tenantService.getCurrentTenantName());
    
    this.jmsTemplate.convertAndSend(this.queue, map);
}



//public void sendWithConversionInvite(InviteContact inviteContact) {
//		
//		
//	 	Map<String, String> map = new HashMap<String, String>();
//	    map.put("type",EmailMessageType.INVITETOCONNECT.toString());
//	    map.put("fromuser", inviteContact.getUser().getFamilyName()+" "+inviteContact.getUser().getGivenName());
//	    map.put("to", inviteContact.getTouseremail());
//	    map.put("message", inviteContact.getMessage());
//	    map.put("ticket", inviteContact.getInviteticket());
//	    map.put("contextPath", acceptInvite);
//	    
//	    this.jmsTemplate.convertAndSend(this.queue, map);
//	}
//	
//public void sendWithConversionResetPassword(User user) {
//	
//	
// 	Map<String, String> map = new HashMap<String, String>();
//    map.put("type",EmailMessageType.RESETPASSWORD.toString());
//    map.put("to", user.getUsername());
//    map.put("ticket", user.getResetPasswordTicket());
//    map.put("contextPath", resetPasswordConfirm);
//    
//    this.jmsTemplate.convertAndSend(this.queue, map);
//}
//
//
//
//	public String getConfirmRegistration() {
//		return confirmRegistration;
//	}
//	
//	public void setConfirmRegistration(String confirmRegistration) {
//		this.confirmRegistration = confirmRegistration;
//	}
//
//
//
//
//	public String getAcceptInvite() {
//		return acceptInvite;
//	}
//	public void setAcceptInvite(String acceptInvite) {
//		this.acceptInvite = acceptInvite;
//	}
//
//
//
//
//	public String getResetPasswordConfirm() {
//		return resetPasswordConfirm;
//	}
//	public void setResetPasswordConfirm(String resetPasswordConfirm) {
//		this.resetPasswordConfirm = resetPasswordConfirm;
//	}
//
//
//
//
//	public class MyMessageCreator implements MessageCreator
//	{
//
//		 
//		
//		private String text;
//		public Message createMessage(Session session) throws JMSException {
//				
//				
//				return session.createTextMessage(text);
//		}
//		
//		public void setText(String text){
//			this.text=text;
//			
//		}

	
 
//}
	
	
	
}




