package openrecordz.mail.service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import openrecordz.exception.ResourceNotFoundException;
import openrecordz.queue.JmsEmailQueueSender;
import shoppino.service.EnvironmentService;
import shoppino.service.TenantService;
import shoppino.service.impl.ConfigThreadLocal;

public class MailServiceImpl implements MailService{

	private static Log log = LogFactory.getLog(MailServiceImpl.class);	

//	public static String CLASSPATH_MAIL_PATH = "/mail/";
	public static String CLASSPATH_MAIL_PATH = "/email_templates/";
	
	public static String DEFAULT_MAIL_EXTENSION = ".vm";
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("$mail{mail.default.from}")
	private String defaultFrom;
	
	@Value("$mail{mail.default.bcc}")
	private String defaultBcc;
	
//	@Value("$mail{mail.enabled}")
//	private String enabledString;
	
	@Value("$shoppino{scripting.filesystem.path}")
	public String fileSystemScriptsPath;
	
	private boolean enabled;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired(required=false)
	JmsEmailQueueSender jmsEmailQueueSender;
	
	EmailMessageType emailMessageType;
	
	@Autowired
	private TenantService tenantService;
	
	@Value("$shoppino{shoppino.url}")
	private String shoppinoUrl;
	
	@Value("$shoppino{shoppino.hostname}")
	private String shoppinoHostname;
	
	@Autowired
	private EnvironmentService environmentService;
							   
	@Autowired
	@Qualifier(value="tenantSettingMappingCustomDataMessageSource")
	MessageSource messageSource;
	
	
	public static String CONFIG_THREADLOCAL_MAIL_ENABLED_KEY = "mailEnabled";
	
	
	public MailServiceImpl() {
//		enabled = Boolean.parseBoolean(enabledString);
	}
	
	@Override
	public void sendMail(String from, String to, String subject, String msg) {
		if (enabled) {
			SimpleMailMessage message = new SimpleMailMessage();
			 
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			javaMailSender.send(message);	
		}
	}
	
	@Override
	public void sendMail(String to, String subject, String msg) {
		if (enabled) {
			SimpleMailMessage message = new SimpleMailMessage();
			 
			message.setFrom(defaultFrom);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			javaMailSender.send(message);	
		}
	}

	@Override
	public void sendMail(final String to, final String subject, final String mailType,final Map parameters, final boolean isHtml) {
		this.sendMail(to, null, subject, mailType, parameters, isHtml);
	}
	@Override
	public void sendMail(final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml) {
		this.sendMail(null, to, bcc, subject, mailType, parameters, isHtml);
	}
	@Override
	public void sendMail(final String from, final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml) {
		this.sendMail(null,from, to, bcc, subject, mailType, parameters, isHtml);
	}
	@Override
	public void sendMail(final Map<String,String> configMap,final String from, final String to, final String bcc, final String subject, final String mailType,final Map parameters, final boolean isHtml) {
		 if (enabled) {
		        MimeMessagePreparator preparator = new MimeMessagePreparator() {
		           public void prepare(MimeMessage mimeMessage) throws Exception {
		        	   
		        	   if (configMap!=null && javaMailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
		        		   JavaMailSenderImpl jmsi=((org.springframework.mail.javamail.JavaMailSenderImpl)javaMailSender);

		        		   if (configMap.containsKey("host"))
		        			   jmsi.setHost(configMap.get("host").toString());
		        		   
		        		   if (configMap.containsKey("port"))
		        			   jmsi.setPort(Integer.valueOf(configMap.get("port").toString()));
		        		   
		        		   if (configMap.containsKey("username"))
		        			   jmsi.setUsername(configMap.get("username").toString());
		        		   
		        		   if (configMap.containsKey("password"))
		        			   jmsi.setPassword(configMap.get("password").toString());
		        		   
		        		   if (configMap.containsKey("defaultEncoding"))
		        			   jmsi.setDefaultEncoding(configMap.get("defaultEncoding").toString());
		        		   
		        		   
		        		   if (configMap.containsKey("defaultEncoding"))
		        			   jmsi.setDefaultEncoding(configMap.get("defaultEncoding").toString());
		        		   
		        	   }
		        	   
//		              MimeMessageHelper message = new MimeMessageHelper(mimeMessage,"UTF-8");
		        	   MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		              
		        	   String finalFrom = null;
		        	   
		        	   if (from==null || from.equals(""))
		        		   finalFrom= defaultFrom;
		               else
		            	   finalFrom= from;
//			            	  message.setFrom(from); 
		        	   
		        	   message.setFrom(finalFrom);
		        	   
		        	   		        	  
		        	  if (to.contains(","))
		        		  message.setTo(to.split(","));
		              else
		            	  message.setTo(to);
		              
		        			        		 
		        	  String mergedBcc = null;
		        	  
		        	  
		        	  if (defaultBcc!=null)
		        		  mergedBcc = defaultBcc;
		        	  
		        	  if (bcc!=null)
		        		  mergedBcc = mergedBcc + "," + bcc;
		        	  
		        	  
		        	  
		              if (mergedBcc.contains(","))
		            	  message.setBcc(mergedBcc.split(","));
		              else		            	  
		            	  message.setBcc(mergedBcc);
		              
		              
//		        	  //bcc override defaultBcc
//		              if (bcc!=null) {
//		        		  if (bcc.contains(","))
//		        			  message.setBcc(bcc.split(","));
//		        		  else
//		        			  message.setBcc(bcc);
//		        	  }

		        	  
		              message.setSubject(subject);
		              
		            
		              
		              
		              Map model = new HashMap();
		              model.put("model", parameters);
		              
		              Map info = new HashMap();
		              info.put("hostname", shoppinoHostname);
		              info.put("url", shoppinoUrl);
		              info.put("tenantName", tenantService.getCurrentTenantName());
		              info.put("tenantDisplayName", tenantService.getCurrentTenantDisplayName());
		              info.put("tenantUrl", environmentService.getTenantUrl());
		              model.put("info", info);
		              
		              
		              
		              String mailTempletePath = null;
		              
		              String pathClassPathMail = CLASSPATH_MAIL_PATH +  mailType + DEFAULT_MAIL_EXTENSION;
//		              String pathClassPathMail = mailType + DEFAULT_MAIL_EXTENSION;
		              log.debug("pathClassPathMail : "  + pathClassPathMail);
		              
		              
		              //TODO puo generare un conflitto di template tra quelli contenuti in class path e quelli in cloud code
		              Resource classPathMail = new ClassPathResource(pathClassPathMail);
			      		if (classPathMail.exists()) {
			      			log.debug("pathClassPathMail found here: "  + pathClassPathMail);
			      			mailTempletePath = classPathMail.getFile().getAbsolutePath();
			      			log.debug("classPathMailTemplate: " + mailTempletePath);
			      		}
			      		
			      		
			      		String pathFileSystemMail = fileSystemScriptsPath + tenantService.getCurrentTenantName() + CLASSPATH_MAIL_PATH + mailType + DEFAULT_MAIL_EXTENSION;
//			      		String pathFileSystemMail = fileSystemScriptsPath + tenantService.getCurrentTenantName() + File.separator + mailType + DEFAULT_MAIL_EXTENSION;
			    		log.debug("pathFileSystemMail : "  + pathFileSystemMail);
			    		
			    		Resource fileSystemMail = new FileSystemResource(pathFileSystemMail);
			    		if (fileSystemMail.exists()) {
			    			log.debug("fileSystemMail found here: "  + pathFileSystemMail);
			    			mailTempletePath = fileSystemMail.getFile().getAbsolutePath();
			    			log.debug("fileSystemMailTemplate: " + mailTempletePath);
			    		}
			    		
			    		boolean templateFromDb = false;
			    		String dbMailTemplateAsString = null;
			    		try {
			    			dbMailTemplateAsString = messageSource.getMessage(String.format(EmailSettingsConstant.TENANT_SETTINGS_MAIL_SEND_EMAIL_TEMPLATE_KEY,tenantService.getCurrentTenantName(),mailType), null, Locale.getDefault());
			    			log.debug("dbMailTemplate found here: "  + String.format(EmailSettingsConstant.TENANT_SETTINGS_MAIL_SEND_EMAIL_TEMPLATE_KEY,tenantService.getCurrentTenantName(),mailType));
//			    			mailTempletePath = dbMailTemplate;
			    			templateFromDb = true;
			    			log.debug("dbMailTemplate: " + dbMailTemplateAsString);
			    			
			    		}catch (NoSuchMessageException nsme) {
							log.debug(nsme);
						}
			    		
			    	
			    		if (mailTempletePath==null && dbMailTemplateAsString==null)
			    			throw new ResourceNotFoundException("Mail with name : " + mailType +" not found in classpath, filesystem and databases");
			    		
			    		
			    		String text = null;
			    		
			    		if(templateFromDb) {
//			    			String s = "We are using $project $name to render this.";
				            StringWriter w = new StringWriter();
				    		VelocityContext velocityContext = new VelocityContext(model);
							velocityEngine.evaluate(velocityContext, w, "mystring", dbMailTemplateAsString );
							text = w.toString();
			    		}else {
			    			text = VelocityEngineUtils.mergeTemplateIntoString(
					                 velocityEngine, mailTempletePath, "utf-8", model);
			    		}
			    		
			    		
			    		log.debug("text mail: " + text);
						
//		              String text = VelocityEngineUtils.mergeTemplateIntoString(
//				                 velocityEngine, "mail/"+mailType+".vm", "utf-8", model);
		              
		              
//		              message.setText(text, true);
//		              message.setText("La tua password per Dressique è stata resettata.", false);
		              message.setText(text, isHtml);
//		              message.setText(MimeUtility.encodeText(text, "utf-8", "B"), false);
		              
		              log.info("Sending email from: " + finalFrom + ", to: " + to + ", bcc: " + mergedBcc + ", subject: "+ subject );
		              log.info(" and test: "+text);
		           }
		        };
		        this.javaMailSender.send(preparator);
		        
		  }
	 }
	 
	@Override
	  public void sendMail(final String to, final String subject, final String mailType,final Map parameters) {
		  this.sendMail(to, subject, mailType, parameters, false);
	  }
	
	
	
	
	
	
	
	@Override
	  public void sendQueuedMail(String to, Map parameters, EmailMessageType mailType) {
	
		 if (enabled) {
			  switch (mailType) {
			  	case REGISTRATION: jmsEmailQueueSender.sendRegistrationMail(to, "Benvenuto");			  	
			  	break;
			  	case LIKE: jmsEmailQueueSender.sendLikeMail(to, parameters.get("refId").toString(), parameters.get("from").toString(), "Hai ricevuto apprezzamenti");	
			  	break;
			  	case ABUSE: jmsEmailQueueSender.sendAbuseMail(to, "Nuovo abuso");	
			  	break;
			  	case PRODUCT_CREATED: jmsEmailQueueSender.sendProductCreatedMail(to, parameters.get("product_id").toString(), "Complimenti!!");	
			  	break;
			  default: 
				  log.error("SendQueuedMail has an invalid mailType");			  		
			  	break; 
			  }
		 }
			  
	  }
	  
	  
	@Override
	public void sendQueuedMail(String from, String to, String bcc,
			String subject,  final String templateMailName, Map parameters,
			boolean isHtml) {
		  if (enabled) {
			 jmsEmailQueueSender.sendMail(from, to, bcc, subject, templateMailName, parameters, isHtml);	
			 
		  }
		
	}
	
	
	  
//	  public void sendSimpleMail(final String to, final String subject, final String mailType,final Map parameters) {
//		  if (enabled) {
//			  SimpleMailMessage message = new SimpleMailMessage();
//              message.setTo(to);
//              message.setSubject(subject);
//              message.setFrom(defaultFrom); // could be parameterized...
//              Map model = new HashMap();
//              model.put("model", parameters);
//              String text = VelocityEngineUtils.mergeTemplateIntoString(
//              velocityEngine, "mail/"+mailType+".vm", model);
//              message.setText(text);
//		      
//		      this.javaMailSender.send(message);
//		  }
//	     }

	public boolean isEnabled() {
		return enabled && Boolean.parseBoolean(ConfigThreadLocal.get().get(CONFIG_THREADLOCAL_MAIL_ENABLED_KEY).toString());
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
		 
	  
	  
	
//	public static void main( String[] args )
//    {
//    	ApplicationContext context = 
//             new ClassPathXmlApplicationContext("shoppino-mail-config.xml");
// 
//    	MailServiceImpl mm = (MailServiceImpl) context.getBean("mailMail");
//        mm.sendMail("from@no-spam.com",
//    		   "to@no-spam.com",
//    		   "Testing123", 
//    		   "Testing only \n\n Hello Spring Email Sender");
// 
//    }
	  
	  
	  
//  	mailService.sendMail("noreply@dressique.com","andrea.leo83@gmail.com",  "testcode", "body");
//  	mailService.sendMail("andrea.leo83@gmail.com",  "testcode2", "body2");
//  	Map m =new HashMap();
//  	m.put("username", "ciccio");
//  	mailService.sendMail("andrea.leo83@gmail.com", "forgot_password",m );
	
}
