//package shoppino.web.admin;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import shoppino.exception.ResourceNotFoundException;
//import shoppino.security.exception.AuthenticationException;
//import shoppino.security.service.AuthenticationService;
//
//@Controller
//public class UserAdminController implements BaseAdminController {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//
//	@Autowired
//    AuthenticationService authenticationService;
//    
//    @RequestMapping(value = {"/users//{username}/reload", "/users//{username:.+}/reload"})
//    public String reauthenticate(@PathVariable String username, Model model, HttpServletRequest request) throws ResourceNotFoundException, AuthenticationException {    	
//    	
//    	authenticationService.authenticate(username);
//    	
//    	return "redirect:/";
//    }    
//    
//    
// 
//}