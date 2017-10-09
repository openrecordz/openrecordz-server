package openrecordz.web.admin;

import it.f21.accessLog.AccessLog;
import it.f21.accessLog.AccessLogManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuditingAdminController implements BaseAdminController {

	protected final Log logger = LogFactory.getLog(getClass());
	

	@Autowired
	AccessLogManager accessLogManager;
    
    @RequestMapping(value = {"/auditing"})
    public ModelAndView last(Model model, HttpServletRequest request) {    	
    	
    	List<AccessLog> last = accessLogManager.last();
    	model.addAttribute("audits", last);
    	
    	return new ModelAndView("auditing-last", "model", model);
    }    
    
    
 
}