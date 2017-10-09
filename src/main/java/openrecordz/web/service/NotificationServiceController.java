package openrecordz.web.service;
//package shoppino.web.service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import shoppino.domain.Notification;
//import shoppino.exception.ShoppinoException;
//import shoppino.exception.TooManyNotificationException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.service.NotificationService;
//
//
//@Controller
//public class NotificationServiceController implements BaseServiceController {
//	
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	@Autowired
//    private NotificationService notificationService;
//
//	@Autowired
//	private AuthenticationService authenticationService;
//	
//	@Value("$shoppino{default.page}")
//	private int defaultPage = 0;
//	
//	@Value("$shoppino{default.pagesize}")
//	private int defaultPageSize = 20;
//    
////	  @RequestMapping(value = "/notifications/add", method = RequestMethod.POST)    
////	  	 public String add(Model model, @RequestParam("discussionId") String discussionId, @RequestParam("text") String text) throws ShoppinoException {	    	    			    		
////	    		
////			String id = commentService.add(text, discussionId, Product.class);
////			Comment	comment = commentService.getById(id);
////	    	logger.info("Added comment for discussionId " + discussionId + "with text " + text);
////	    	
////	    	
////	    	model.addAttribute("comment", comment);	    	
////	    	
////	    	return "add-comment-service";
////	    }
//	    
////	http://www.dressique.localhost.com:8880/shoppino-ultima/service/v1/notifications/send?type=uri&cURI=product:7723&message=ciccio&to=luca.conversano
////	http://default.frontiere21.it/service/v1/notifications/send?type=uri&cURI=product:7723&message=ciccio&to=luca.conversano
//	
//	//abbimo riscontrato dei problemi con encoding caratteri speciali se passati come paramentri get (vedi sopra). ok invece se i valori accentati vengono passati con parametri del post
//	 @RequestMapping(value = "/notifications/send", method = RequestMethod.POST)
//	 public String send(
//	    		@RequestParam(value="message", required=false) String message,
//	    		@RequestParam(value="type", required=true) String type,
//	    		@RequestParam(value="to", required=true) String to,
//	    		@RequestParam(value="force", required=false, defaultValue="false") Boolean force,
//	    		@RequestParam(value="cURI", required=false) String cURI) throws TooManyNotificationException  {
//
//		 logger.debug("message : " + message);
//		 logger.debug("type : " + type);
//		 logger.debug("to : " + to);
//		 logger.debug("cURI : " + cURI);
//		 logger.debug("force : " + force);
//		  
//		 logger.info("Sending notification with message: " + message + " type : " + type + " to : " + to + " cURI : "+ cURI + " force: " + force);
//		
////		 if (to!=null && to.equals(NotificationService.ALL_DEVICES))
////			 notificationService.send(authenticationService.getCurrentLoggedUsername(), message, type, cURI);
////		 else
//		
//		 
//		notificationService.send(message, type, to, cURI, force);
//		 
////		 notificationService.send(authenticationService.getCurrentLoggedUsername(), message, type, to, cURI);
//		 
//		 logger.info("Nofication sent with success");
//		 
//		 return "add-notification-service";	
//	    }
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 @RequestMapping(value = "/notifications/usersend", method = RequestMethod.POST)
//	 public String userSend(
//	    		@RequestParam(value="message", required=false) String message,
//	    		@RequestParam(value="type", required=true) String type,
//	    		@RequestParam(value="to", required=true) String to,
//	    		@RequestParam(value="json", required=false) String json,
//	    		@RequestParam(value="historyvisible", required=false, defaultValue="true") Boolean historyVisible,
//	    		@RequestParam(value="badge", required=false) Integer badge) throws JsonParseException, JsonMappingException, IOException  {
//
//		 logger.debug("message : " + message);
//		 logger.debug("type : " + type);
//		 logger.debug("to : " + to);
//		 
//		 logger.debug("badge : " + badge);
//		 logger.debug("json : " + json);
//		 logger.debug("historyVisible : " + historyVisible);
//		
////		 if (to!=null && to.equals(NotificationService.ALL_DEVICES))
////			 notificationService.send(authenticationService.getCurrentLoggedUsername(), message, type, cURI);
////		 else
//		
//		
//		 Map<String,String> data = new HashMap<String,String>();
//		 if (json!=null && !json.equals("")) {
//			 ObjectMapper mapper = new ObjectMapper();
//		 
//			//convert JSON string to Map
//			data = mapper.readValue(json, 
//			    new TypeReference<HashMap<String,String>>(){});
//	 
//			logger.debug("data as map " + data);
//		 
//		 }
//			
//		 notificationService.send(to, message, data, badge, type, historyVisible);
////		 notificationService.send(authenticationService.getCurrentLoggedUsername(), message, type, to, cURI);
//		 
//		 logger.info("Nofication sent with success");
//		 
//		 return "add-notification-service";	
//	    }
//	 
//		@RequestMapping(value = "/notifications/registerdevice", method = { RequestMethod.GET, RequestMethod.POST})    
//		public String registerDevice(Model model, @RequestParam("regId") String regId, @RequestParam("source") String type) throws ShoppinoException {	    	    			    		
//		
//			notificationService.register(regId, type);
//			
//			return "add-notification-service";
//		}
//		
//		@RequestMapping(value = "/notifications/unregisterdevice", method = { RequestMethod.GET, RequestMethod.POST})    
//		public String unregisterdevice(Model model, @RequestParam("regId") String regId, @RequestParam("source") String type) throws ShoppinoException {	    	    			    		
//		
//			notificationService.unregisterDevice(regId, type);
//			
//			return "add-notification-service";
//		}
//	
//		@RequestMapping(value = "/notifications/register", method = { RequestMethod.GET, RequestMethod.POST})    
//		public String register(Model model, @RequestParam("regId") String regId, @RequestParam("source") String type) throws ShoppinoException {	    	    			    		
//		
//			notificationService.register(regId, type);
//			
//			return "add-notification-service";
//		}
//		
//		@RequestMapping(value = "/notifications/unregister", method = { RequestMethod.GET, RequestMethod.POST})    
//		public String unregister(Model model, @RequestParam("regId") String regId, @RequestParam("source") String type) throws ShoppinoException {	    	    			    		
//		
//			notificationService.unregister(regId, type);
//			
//			return "add-notification-service";
//		}
//		
//		
//	
//	    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.GET)    
//	 	 public ModelAndView view(Model model, @PathVariable String notificationId) throws ShoppinoException {
//	    	
//	    	List<Notification> notifications = new ArrayList<Notification>();
//	    	
//	    	Notification notification = notificationService.view(notificationId);	    
//
//	    	notifications.add(notification);
//	    	
//	    	model.addAttribute("notifications", notifications);
//	    	
//	    	return new ModelAndView("notifications-service", "model", model);
//	    }
//	    
//	    
//	    @RequestMapping(value = "/notifications", method = RequestMethod.GET)    
//	 	 public ModelAndView find(Model model) throws ShoppinoException {	   	    	
//	    	
//	    	List<Notification> notifications = notificationService.findByTo(authenticationService.getCurrentLoggedUsername());	    
//
//	    	logger.debug("Getting notifications of user " + authenticationService.getCurrentLoggedUsername());
//	    	
//	    	model.addAttribute("notifications", notifications);
//	    	
//	    	return new ModelAndView("notifications-service", "model", model);
//	    }
//	    
//	    
////	    { "type":  /^operation/}
//	    
//	    
//	    @RequestMapping(value = "/notifications/operations", method = RequestMethod.GET)    
//	 	 public ModelAndView findOperations(Model model,HttpServletRequest request) throws ShoppinoException {	   	    	
//	    	
//	    	int page = defaultPage;
//	    	int pageSize = defaultPageSize;
////	    	String test = cdmessageSource.getMessage("test", null, Locale.getDefault());
//	    	if (request.getParameter("page")!=null)
//	    		page=Integer.parseInt(request.getParameter("page"));
//	    	
//	    	if (request.getParameter("pageSize")!=null)
//	    		pageSize = Integer.parseInt(request.getParameter("pageSize"));
//	    	
//	    	
//	    	logger.debug("page : " + page);
//	    	logger.debug("pageSize : " + pageSize);
//	    	
//	    	List<Notification> notifications = notificationService.findByType("operation/",page,pageSize);	    
//
//	    	logger.debug("Getting notifications of type operation");
//	    	
//	    	model.addAttribute("notifications", notifications);
//	    	
//	    	model.addAttribute("count", notificationService.countByType("operation/"));
//	    	
//	    	return new ModelAndView("notifications-service", "model", model);
//	    }
//	    
//	    @RequestMapping(value = "/notifications/hasnew", method = RequestMethod.GET)    
//	 	public ModelAndView hasNew(Model model) throws ShoppinoException {
//	    		    	
//	    	
//	    	int newNot = notificationService.hasNew(authenticationService.getCurrentLoggedUsername());
//
//	    	logger.debug("Getting new notifications for user " + authenticationService.getCurrentLoggedUsername() + " number : " + newNot);
//	    	
//	    	model.addAttribute("newNotification", newNot);
//	    	
//	    	return new ModelAndView("hasnew-notifications-service", "model", model);
//	    }
//	    
//	    
//	    
//	    
//}