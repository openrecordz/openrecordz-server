package shoppino.mail.service;

import java.util.Map;

public interface MailService {

	public void sendMail(String from, String to, String subject, String msg);
	
	public void sendMail(String to, String subject, String msg);
	
	public void sendMail(final String to, final String subject, final String mailType,final Map parameters);

	public void sendMail(final String to, final String subject, final String mailType,final Map parameters, final boolean isHtml);
	
	public void sendMail(final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml);
	
	public void sendMail(final String from, final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml);
	
	
//	<property name="host" value="$mail{mail.host}" />
//	<property name="port" value="$mail{mail.port}" />
//	<property name="username" value="$mail{mail.username}" />
//	<property name="password" value="$mail{mail.password}" />
//  	<property name="defaultEncoding" value="UTF-8"/>
//	<property name="javaMailProperties">
//	   <props>
//       	      <prop key="mail.smtp.auth">true</prop>
//       	     <!--  <prop key="mail.smtp.starttls.enable">true</prop> -->
//       	      <prop key="mail.smtp.starttls.enable">false</prop> 
//       	   </props>
//	</property>
//	
	public void sendMail(final Map<String,String> configMap,final String from, final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml);
	
	
	
	
	
	
	public void sendQueuedMail(String to, Map parameters, EmailMessageType mailType);
	
	
	//easy to invocate from script
	public void sendQueuedMail(final String from, final String to, final String bcc, final String subject, final String templateMailName, final Map parameters, final boolean isHtml);
}
