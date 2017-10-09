package shoppino.security.rememberme;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class MTPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private String cookieName = SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
    private Boolean useSecureCookie = null;
    String cookieDomain = null;
    List<String> cookiesDomain = null;
//    private Method setHttpOnlyMethod;

	protected final Log logger = LogFactory.getLog(getClass());

	public MTPersistentTokenBasedRememberMeServices() {
		super();
	}
	public MTPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService,
            PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
	}
	
	/**
     * Sets the cookie on the response.
     *
     * By default a secure cookie will be used if the connection is secure. You can set the {@code useSecureCookie}
     * property to {@code false} to override this. If you set it to {@code true}, the cookie will always be flagged
     * as secure. If Servlet 3.0 is used, the cookie will be marked as HttpOnly.
     *
     * @param tokens the tokens which will be encoded to make the cookie value.
     * @param maxAge the value passed to {@link Cookie#setMaxAge(int)}
     * @param request the request
     * @param response the response to add the cookie to.
     */
    public void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getCookiePath(request));
        
        cookie.setDomain(cookieDomain);
        
        if (useSecureCookie == null) {
            cookie.setSecure(request.isSecure());
        } else {
            cookie.setSecure(useSecureCookie);
        }

//        if(setHttpOnlyMethod != null) {
//            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
//        } else if (logger.isDebugEnabled()) {
//            logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
//        }
        
        response.addCookie(cookie);
        
        if(cookiesDomain!=null) {
	        for (String cd : cookiesDomain) {
	        	  Cookie other = (Cookie) cookie.clone();
	        	  other.setDomain(cd);
	        	  logger.debug("Added cookie for domain : " + cd);
	        	  response.addCookie(other);
			}
        }
        
    }
    
    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
    
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
    	super.onLoginSuccess(request, response, successfulAuthentication);
    }

    
    /**
     * Whether the cookie should be flagged as secure or not. Secure cookies can only be sent over an HTTPS connection
     * and thus cannot be accidentally submitted over HTTP where they could be intercepted.
     * <p>
     * By default the cookie will be secure if the request is secure. If you only want to use remember-me over
     * HTTPS (recommended) you should set this property to {@code true}.
     *
     * @param useSecureCookie set to {@code true} to always user secure cookies, {@code false} to disable their use.
     */
    public void setUseSecureCookie(boolean useSecureCookie) {
        this.useSecureCookie = useSecureCookie;
    }
	public String getCookieDomain() {
		return cookieDomain;
	}
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}
	public List getCookiesDomain() {
		return cookiesDomain;
	}
	public void setCookiesDomain(List<String> cookiesDomain) {
		this.cookiesDomain = cookiesDomain;
	}
    
    
	

    
}
