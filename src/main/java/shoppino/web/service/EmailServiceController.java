package shoppino.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.mail.service.MailService;
import shoppino.security.service.AuthenticationService;

@Controller
public class EmailServiceController implements BaseServiceController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	
	@Autowired
	MailService mailService;
	
	
	@Autowired
	private AuthenticationService authenticationService;
	
		
    @RequestMapping(value = "/emails/testsend", method = RequestMethod.GET)
    public @ResponseBody String showAddProduct() {
         
    	mailService.sendMail("andrealeo@libero.it", "test obj", "test");
        return "success";
    }
	

}