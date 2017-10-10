package openrecordz.scripting;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.taglibs.authz.AbstractAuthorizeTag;

public class JsAuthorizeTag extends AbstractAuthorizeTag {

	protected Log log = LogFactory.getLog(getClass());
	
	@Override
	protected ServletRequest getRequest() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	protected ServletResponse getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
