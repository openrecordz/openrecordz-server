
package openrecordz.queue;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;

import openrecordz.domain.Notification;

/**
 * @author Lorenzo
 *
 */
@Repository
public class JmsQueueNotificationSender {
	private JmsTemplate jmsTemplate;
	private String queue;
	protected String text;
	
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
	
//	public void simpleSend (String text) {
//		
//		MyMessageCreator msgCreator = new MyMessageCreator();
//		msgCreator.setText(text);
//		this.jmsTemplate.send(this.queue, msgCreator);
//		
//	}
	
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
	
	public void sendPushNotification(Notification notification) {
		
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("type", notification.getType());
//	    map.put("Name", registrationRequest.getFirstName());
//	    map.put("Surname", registrationRequest.getLastName());
//	    map.put("Password", registrationRequest.getPassword());
//	    map.put("Username", registrationRequest.getUsername());
//	    map.put("ticket", ticket);
//	    map.put("contextPath", confirmRegistration);
//	    map.put("Subject",subject);
	    
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




