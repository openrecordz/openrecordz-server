//package shoppino.security.tags;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.jsp.tagext.Tag;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.GenericTypeResolver;
//import org.springframework.security.access.expression.SecurityExpressionHandler;
//import org.springframework.security.taglibs.authz.JspAuthorizeTag;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//public class MTJspAuthorizeTag extends JspAuthorizeTag implements Tag {
//	
//	
//	
//	 @SuppressWarnings({ "unchecked", "rawtypes" })
////	 @Override
//	    SecurityExpressionHandler<FilterInvocation> getExpressionHandler() throws IOException {
//	        ApplicationContext appContext = WebApplicationContextUtils
//	                .getRequiredWebApplicationContext(getServletContext());
//	        Map<String, SecurityExpressionHandler> handlers = appContext
//	                .getBeansOfType(SecurityExpressionHandler.class);
//
//	        for (SecurityExpressionHandler h : handlers.values()) {
//	            if (FilterInvocation.class.equals(GenericTypeResolver.resolveTypeArgument(h.getClass(),
//	                    SecurityExpressionHandler.class))) {
//	                return h;
//	            }
//	        }
//
//	        throw new IOException("No visible WebSecurityExpressionHandler instance could be found in the application "
//	                + "context. There must be at least one in order to support expressions in JSP 'authorize' tags.");
//	    }
//
//}
