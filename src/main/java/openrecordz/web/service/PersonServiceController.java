package openrecordz.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import openrecordz.domain.Person;
import openrecordz.domain.validator.UserRegistrationServiceValidation;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.ShoppinoException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.security.service.UserService;
import openrecordz.security.web.authentication.UsernameUtils;
import openrecordz.service.PersonService;
import openrecordz.service.TenantService;

@Controller
public class PersonServiceController  implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	private PersonService personService;
	
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Value("$shoppino{imagerepo.url}")
	private String imagerepoUrl;
	
	@Value("$shoppino{imagerepo.search.url}")
	private String imagerepoSearchUrl;
	
	@Value("$shoppino{person.avatar.name}")
	String personPhotoName;
    
	@Value("$shoppino{default.search.service.page}")
	private int defaultPage;

	@Value("$shoppino{default.search.service.pagesize}")
	private int defaultPageSize;
	
	@Autowired
	UserRegistrationServiceValidation userRegistrationServiceValidation;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TenantService tenantService;
	
	@RequestMapping(value = "/people")
    public @ResponseBody Map search(HttpServletRequest request,
    		 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
    		 @RequestParam(value = "text", required=false) String text,
    		@RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction) throws ShoppinoException {        
        
        int page = defaultPage;
    	int pageSize = defaultPageSize;

    	if (request.getParameter("page")!=null)
    		page=Integer.parseInt(request.getParameter("page"));

    	if (request.getParameter("pagesize")!=null)
    		pageSize = Integer.parseInt(request.getParameter("pagesize"));
    	
    	
    	logger.info("Search people with query : " + query);            	    	
    	logger.debug("text : " + text);	  
    	
    	
    	Map returnVal= new HashMap();
    	
    	if (text!=null && !text.equals("")) {    		
    		query="{ $text: { $search: \""+text+"\" } } ";	
    	}
    	
	    	
    	List<Person> people= personService.findByQuery(query, page, pageSize, direction, sortFields);
    	for (Person person : people) {
			person.setEmail("***@***.**");
			person.setPhoto(imagerepoUrl+imagerepoSearchUrl+person.getPhoto());
		}
    	
		returnVal.put("people", people);
		
    	//count people
		long count = personService.countByQuery(query);
		returnVal.put("count", count);
	    		    
	        
    	
    	
    	return returnVal;

    }
	
	
    @RequestMapping(value = {"/people//{id}/", "/people//{id:.+}"})
    public @ResponseBody Map detail(@PathVariable String id, HttpServletRequest request) throws ResourceNotFoundException, UserNotExistsException { 	    
    	
    	id = UsernameUtils.getRealUsername(id);
    	
    	logger.debug("datail for user : " + id);
    	
    	Person person = personService.getByUsername(id);    	   
    	
    	person.setPhoto(imagerepoUrl+imagerepoSearchUrl+person.getPhoto());
//    	List<Person> people = new ArrayList<Person>();
//    	people.add(person);
    	
//    	long countCreatedBy = productService.countByCreatedBy(id, Statusable.STATUS_VISIBLE);
//    	
//    	long countLikes = productService.countByLikes(id, Statusable.STATUS_VISIBLE);
    	
    	Map ret = new HashMap();
    	
//    	model.addAttribute("people", people);
////    	model.addAttribute("countCreatedBy", countCreatedBy);
////    	model.addAttribute("countLikes", countLikes);
    	
//    	model.addAttribute("user", userService.getByUsername(id));
    	ret.put("people", person);
    
    
    	return ret;
    }   
    
    
    @RequestMapping(value = {"/people/me"})
    public @ResponseBody Map  meDetail( HttpServletRequest request) throws ResourceNotFoundException, UserNotExistsException { 	    
    	
    	return this.detail(authenticationService.getCurrentLoggedUsername(),  request);
    }   

    
    @RequestMapping(value = {"/people//{id}/photo", "/people//{id:.+}/photo"})
    public String photo(@PathVariable String id, Model model, HttpServletRequest request) throws ResourceNotFoundException, IOException {
    	
    	id = UsernameUtils.getRealUsername(id);
    	logger.debug("datail for user : " + id);
    	
    	String height= "50";
    	String weight="50";
    	
    	if (request.getParameter("h")!=null)
    		height=request.getParameter("h");
    	
    	if (request.getParameter("w")!=null)
    		weight=request.getParameter("w");
    
    	Person person = personService.getByUsername(id);    
    	return "redirect:"+imagerepoUrl+imagerepoSearchUrl+person.getPhoto()+"&h="+height+"&w="+weight;
    }
    	
    	
   
    
    @RequestMapping(value = "/people/me/updatephoto", method = RequestMethod.POST)
    public @ResponseBody String updateMephoto(@RequestParam(value="photo_file", required=false) MultipartFile photo
    					) throws ShoppinoException, IOException {
    	    	        
    	
    	if (photo!=null && !photo.isEmpty())
    		personService.updateLogo(authenticationService.getCurrentLoggedUsername(), personPhotoName, photo.getInputStream());
    	else
    		personService.removeLogo(authenticationService.getCurrentLoggedUsername());
    	
    	
    	logger.info("Photo saved for user : " + authenticationService.getCurrentLoggedUsername());

    	return "{\"status\" : \"success\"}";
    }

    @RequestMapping(value = "/people/me/update", method = RequestMethod.POST)
    public @ResponseBody String updateMe(    	
		 	@RequestParam("fullName") String fullName,
		 	@RequestParam("email") String email,
    		@RequestParam(value="properties", required=false) String properties
    		) 
    				throws ShoppinoException, IOException {
    	
    	 logger.debug("username : " + authenticationService.getCurrentLoggedUsername());
		 logger.debug("fullName : " + fullName);
		 logger.debug("email : " + email);
		 logger.debug("properties : " + properties);
		 
    	userRegistrationServiceValidation.validate(authenticationService.getCurrentLoggedUsername(), fullName, email);
    	
		Person p = personService.getByUsername(authenticationService.getCurrentLoggedUsername());
		personService.update(authenticationService.getCurrentLoggedUsername(), fullName, email, p.getPhoto());
		
		personService.setProperties(authenticationService.getCurrentLoggedUsername(), properties);
    	
    	logger.info("Person updated ");
    	
    	 return "{success:true}";
    }
    
    @RequestMapping(value = "/people/me/save", method = RequestMethod.POST)
    public @ResponseBody String SaveMe(    	
		 	@RequestParam(value="fullName", required=false) String fullName,
		 	@RequestParam(value="email",required=false) String email,
    		@RequestParam(value="properties", required=false) String properties
    		) 
    				throws ShoppinoException, IOException {
    	
    	 logger.debug("username : " + authenticationService.getCurrentLoggedUsername());
		 logger.debug("fullName : " + fullName);
		 logger.debug("email : " + email);
		 logger.debug("properties : " + properties);
		 
//    	userRegistrationServiceValidation.validate(authenticationService.getCurrentLoggedUsername(), fullName, email);
    	
		Person p = personService.getByUsername(authenticationService.getCurrentLoggedUsername());
		
		String fullNameToSave = p.getFullName();
		String emailToSave = p.getEmail();
		
		
		if (fullName !=null)
			fullNameToSave = fullName;
		
		if (email !=null)
			emailToSave = email;
		
		
		personService.update(authenticationService.getCurrentLoggedUsername(), fullNameToSave, emailToSave, p.getPhoto());
		
		if (properties!=null) 
			personService.setProperties(authenticationService.getCurrentLoggedUsername(), properties);
    	
    	logger.info("Person updated ");
    	
    	 return "{success:true}";
    }
    
    
    @RequestMapping(value = "/people/update", method = RequestMethod.POST)
    public @ResponseBody String update(
    		@RequestParam("username") String username,
		 	@RequestParam("fullName") String fullName,
		 	@RequestParam("email") String email,
    		@RequestParam(value="properties", required=false) String properties
    		) 
    				throws ShoppinoException, IOException {
    	
    	 logger.debug("username : " + username);
		 logger.debug("fullName : " + fullName);
		 logger.debug("email : " + email);
		 logger.debug("properties : " + properties);
		 
    	userRegistrationServiceValidation.validate(username, fullName, email);
    	
		Person p = personService.getByUsername(username);
		personService.update(username, fullName, email, p.getPhoto());
		
		personService.setProperties(username, properties);
    	
    	logger.info("Person updated ");
    	
    	 return "{success:true}";
    }
    
    
    @RequestMapping(value = {"/people//{username}/", "/people//{username:.+}"},method = RequestMethod.DELETE)
    public @ResponseBody String delete(
    		@PathVariable String username
    		) 
    	 throws  UserNotExistsException, ResourceNotFoundException {
    	
    	 logger.debug("username : " + username);	
		 
		personService.delete(username);
		userService.delete(username);
    	
    	logger.info("Person and user deleted..This is an unsafe operation. Contents created or modified by this user can be corrupted.. ");
    	
    	 return "{success:true}";
    }
    
    @RequestMapping(value = "/people/updatephoto", method = RequestMethod.POST)
    public @ResponseBody String updatephoto(@RequestParam("username") String username, @RequestParam(value="photo_file", required=false) MultipartFile photo
    					) throws ShoppinoException, IOException {
    	    	        
    	
    	if (photo!=null && !photo.isEmpty())
    		personService.updateLogo(username, personPhotoName, photo.getInputStream());
    	else
    		personService.removeLogo(username);
    	
    	
    	logger.info("Photo saved for user : " + username);

    	return "{\"status\" : \"success\"}";
    }
    
    
//    @Deprecated
//    @RequestMapping(value = "/people/uploadphoto", method = RequestMethod.GET)
//    public String getUploadPhoto() {
//    	return "people-upload-photo-service";
//    }
    
//    @Deprecated
//    @RequestMapping(value = "/people/uploadphoto", method = RequestMethod.POST)
//    public @ResponseBody String uploadPhoto(@RequestParam("photo_file") MultipartFile photo
//    					) throws ShoppinoException, IOException {
//    	    	        
//    	
//    	if (!photo.isEmpty())
//    		personService.updateLogo(authenticationService.getCurrentLoggedUsername(), personPhotoName, photo.getInputStream());
//    	else
//    		personService.removeLogo(authenticationService.getCurrentLoggedUsername());
//    	
//    	
//    	logger.info("Photo saved for user : " + authenticationService.getCurrentLoggedUsername());
//
//    	return "{\"status\" : \"success\"}";
//    }
    
    
    @RequestMapping(value = "/people/join", method = RequestMethod.POST)
    public @ResponseBody String join(@RequestParam("username") String username
    					) throws ShoppinoException, IOException {
    	
    	  personService.joinCurrentTenant(username);
    	
    	  logger.info("User : " + username + " joined tenant " + tenantService.getCurrentTenantName());
    	    	        
    	  return "{\"status\" : \"success\"}";
    }
    
    
    @RequestMapping(value = "/people/disjoin", method = RequestMethod.POST)
    public @ResponseBody String disjoin(@RequestParam("username") String username
    					) throws ShoppinoException, IOException {
    	
    	  personService.disjoinCurrentTenant(username);
    	    	   
    	  logger.info("User : " + username + " disjoined tenant " + tenantService.getCurrentTenantName());
    	    	        
    	  return "{\"status\" : \"success\"}";
    }
}