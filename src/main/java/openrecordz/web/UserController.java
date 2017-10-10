package openrecordz.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import openrecordz.domain.Person;
import openrecordz.exception.ResourceNotFoundException;
import shoppino.security.exception.AuthenticationException;
import shoppino.security.service.AuthenticationService;
import shoppino.security.service.UserService;
import shoppino.security.web.authentication.UsernameUtils;
import shoppino.service.PersonService;

@Controller
public class UserController  {

	protected final Log logger = LogFactory.getLog(getClass());
	
	
	@Autowired
	private PersonService personService;
	
	
	
	@Value("$shoppino{imagerepo.url}")
	private String imagerepoUrl;
	
	@Value("$shoppino{imagerepo.search.url}")
	private String imagerepoSearchUrl;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	UserService userService;

    

    
    @RequestMapping(value = {"/users//{id}/photo", "/users//{id:.+}/photo"})
    public String photo(@PathVariable String id, Model model, HttpServletRequest request) throws ResourceNotFoundException, IOException {
    	
    	id = UsernameUtils.getRealUsername(id);

    	logger.debug("getting photo for user : " + id);
    	
    	String height= "50";
    	String weight="50";
    	
    	if (request.getParameter("h")!=null)
    		height=request.getParameter("h");
    	
    	if (request.getParameter("w")!=null)
    		weight=request.getParameter("w");
    
    	Person person = personService.getByUsername(id);    
    	return "redirect:"+imagerepoUrl+imagerepoSearchUrl+person.getPhoto()+"&h="+height+"&w="+weight;
    }
    
    
    @RequestMapping(value = {"/users//{id}/validate", "/users//{id:.+}/validate"})
    public String validate(@PathVariable String id, Model model, HttpServletRequest request) throws ResourceNotFoundException, IOException, AuthenticationException {
    	final String username = UsernameUtils.getRealUsername(id);
    	
    	String gotoUrl= "/?v";
    	
    	if (request.getParameter("goto")!=null){
    		gotoUrl=request.getParameter("goto");
    	}
    	
    	try {
	    	Boolean result = authenticationService.runAs(new AuthenticationService.RunAsWork<Boolean>() {
				@Override
		        public Boolean doWork() throws Exception {
						userService.addRole(username, "ROLE_VALIDUSER");
		            return true;
		        }
			}, authenticationService.getCurrentLoggedUsername(),"admin");
			
	    	return "redirect:"+gotoUrl;
    	
    	}catch (Throwable t) {
    		//duplicate validation
			logger.error("Erorr validating user : " + username,t);
			return "redirect:"+gotoUrl;
		}
    }
}