package openrecordz.web.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.exception.ShoppinoException;
import openrecordz.service.impl.ScriptServiceMultiEngineImpl;

@Controller
public class ScriptAdminController implements BaseAdminController {

	protected final Log logger = LogFactory.getLog(getClass());
	

	@Autowired
	@Qualifier(value="scriptService")
	ScriptServiceMultiEngineImpl scriptServiceMultiEngineImpl;
	
	
	
    
    @RequestMapping(value = {"/scripting/resetengines"})
    public @ResponseBody String gcmClean(Model model, HttpServletRequest request) throws ShoppinoException {    	
    	
    	scriptServiceMultiEngineImpl.resetEngines();
    	
    	return "{success:true}";
    }    
    
}