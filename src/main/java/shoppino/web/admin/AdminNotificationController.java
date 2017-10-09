//package shoppino.web.admin;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import shoppino.exception.ShoppinoException;
//import shoppino.notification.PushNotificationManager;
//
//@Controller
//public class AdminNotificationController implements BaseAdminController {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//
//	@Autowired
//	@Qualifier(value="gcmPushNotificationManager")
//	PushNotificationManager gcmPushNotificationManager;
//	
//	@Autowired
//	@Qualifier(value="apnsPushNotificationManager")
//	PushNotificationManager apnsPushNotificationManager;
//	
//	
//    
//    @RequestMapping(value = {"/notification/gcm/clean"})
//    public @ResponseBody String gcmClean(Model model, HttpServletRequest request) throws ShoppinoException {    	
//    	
//    	gcmPushNotificationManager.clean();
//    	
//    	return "{success:true}";
//    }    
//    
//    @RequestMapping(value = {"/notification/apns/clean"})
//    public @ResponseBody String apnsClean(Model model, HttpServletRequest request) throws ShoppinoException {    	
//    	
//    	apnsPushNotificationManager.clean();
//    	
//    	return "{success:true}";
//    }    
//    
//    @RequestMapping(value = {"/notification/clean"})
//    public @ResponseBody String clean(Model model, HttpServletRequest request) throws ShoppinoException {    	
//    	
//    	gcmPushNotificationManager.clean();
//    	apnsPushNotificationManager.clean();
//    	
//    	return "{success:true}";
//    }    
//    
// 
//}