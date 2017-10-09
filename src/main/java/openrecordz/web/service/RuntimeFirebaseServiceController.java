package openrecordz.web.service;
//package shoppino.web.service;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import shoppino.exception.ShoppinoException;
//import shoppino.runtime.service.FirebaseService;
//
//
//@Controller
////@RequestMapping(value = "/runtime/firebase")
//public class RuntimeFirebaseServiceController implements BaseServiceController {
//	
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	@Autowired
//	FirebaseService firebaseService;
//   
//    @RequestMapping(value = "/runtime/firebase/generateToken", method = RequestMethod.POST)  
//	 public @ResponseBody String create(Model model, HttpServletRequest request) throws ShoppinoException {
//   	
//    	logger.debug("request.getParameterMap() : " + request.getParameterMap());
//    	
//    	return firebaseService.generateToken(request.getParameterMap());
//            
//   }
//    
//}