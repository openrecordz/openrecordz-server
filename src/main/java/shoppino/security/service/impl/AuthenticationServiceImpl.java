package shoppino.security.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import shoppino.domain.Person;
import shoppino.exception.ResourceNotFoundException;
import shoppino.security.exception.AuthenticationException;
import shoppino.security.service.AuthenticationService;
import shoppino.service.PersonService;

public class AuthenticationServiceImpl implements AuthenticationService {
	
	protected final Log logger = LogFactory.getLog(getClass());

//	@Autowired
//	UserService userService;
	
//@Resource
//	@Resource(name="org.springframework.security.authenticationManager")
	private AuthenticationManager authenticationManager;
	
//	@Autowired
	UserDetailsManager userDetailsService;
		
	@Autowired
	PersonService personService;
	
	 
	public void authenticate(String username) throws AuthenticationException {	
		try {
			//		shoppino.security.domain.User user = userService.getByUsername(username);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			
			logger.debug("user username: " + user.getUsername());
			logger.debug("user password : " + user.getPassword());
			
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
			
//			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, (Collection)null);
			
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			logger.info("Authentication complete for user : " + username);
		}catch (Exception e) {
			throw new AuthenticationException("Authentication failure with username : " + username , e);
		}
		
	}
	
	public void authenticate(String username, String password) throws AuthenticationException {
		String userId = username;
		
		try{
			logger.debug("user username: " + username);
			logger.debug("user password : " + password);
			
			UserDetails user;
			
					
			try {
//				userId = username;	
				user = userDetailsService.loadUserByUsername(username);
			}catch (UsernameNotFoundException unfe) {
				try {
					Person p = personService.getByEmail(username);
					logger.debug("found user by email : " + username);
					userId = p.getUsername();	
					user = userDetailsService.loadUserByUsername(userId);
				}catch (ResourceNotFoundException rnfe) {
					throw unfe;
				}
			}
			
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, password, user.getAuthorities());
			
			Authentication authentication = authenticationManager.authenticate(token);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			logger.info("Authentication complete for user : " + userId);
			
		}catch (Exception e) {
			throw new AuthenticationException("Authentication failure with username : " + userId + " and password : " + password , e);
		}
	}
	
	@Override
	public void logout() {	
		logger.info("Logout for user : " + this.getCurrentLoggedUsername());
		SecurityContextHolder.getContext().setAuthentication(null);		
	}
	
	
	@Override
	public String getCurrentLoggedUsername() {
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String ) {
				return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
			} else {
				User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			    String name = user.getUsername(); //get logged in username	    
			    return name;
			}
		}else {
			return null;
		}
	}
	
	
	@Override
	public Authentication getAuthentication() {
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			return SecurityContextHolder.getContext().getAuthentication();
		}else {
			return null;
		}
	}

	public void clearCurrentSecurityContext() {
//		http://stackoverflow.com/questions/1013032/programmatic-use-of-spring-security
		SecurityContextHolder.clearContext();
	}
	
	
//	public UserDetailsManager getUserDetailsService() {
//		return userDetailsService;
//	}

	public void setUserDetailsService(UserDetailsManager userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

//	public AuthenticationManager getAuthenticationManager() {
//		return authenticationManager;
//	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	@Deprecated
	public boolean isAdministrator() {
		if (this.getCurrentLoggedUsername().equals(ADMIN_USERNAME))
			return true;
		else
			return false;
	}

	@Override
	public boolean isAuthenticated() {
		if (this.getCurrentLoggedUsername() != null && !this.getCurrentLoggedUsername().equals("anonymousUser"))
			return true;
		else 
			return false;
	}
	
	
	private boolean isAuthenticated(String username) {
		if (username != null && !username.equals("anonymousUser"))
			return true;
		else 
			return false;
	}
	
	
	
//	@Deprecated //with authenticationservice.login
//	  public void autoLogin(HttpServletRequest request, String username, String password) {
//	    try {
//	      logger.debug("SimpleSignInAdapter - autoLogin");
//	    	
//	      // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
//	      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//	      token.setDetails(new WebAuthenticationDetails(request));
//	      Authentication authentication = authenticationManager.authenticate(token);
//	      logger.debug("SimpleSignInAdapter - Logging in with :"+ authentication.getPrincipal());
//	      SecurityContextHolder.getContext().setAuthentication(authentication);
//	    } catch (Exception e) {
//	      SecurityContextHolder.getContext().setAuthentication(null);
//	      logger.error("SimpleSignInAdapter - Failure in autoLogin", e);
//	    }
//	  }
	
	
	
	
	
	/**
     * Execute a unit of work as a given user. The thread's authenticated user will be returned to its normal state
     * after the call.
     * 
     * @param runAsWork
     *            the unit of work to do
     * @param username
     *            the user ID
     * @return Returns the work's return value
	 * @throws AuthenticationException 
     */
    public <R> R runAs(RunAsWork<R> runAsWork, String originalUsername, String username) throws AuthenticationException
    {
    	final R result;
    	
//    	if (isAuthenticated()==false)  {
//    		throw new AuthenticationException("User not authenticated");
//    	} else {
//    		boolean originalIsAuthenticated = isAuthenticated();
//    		String originalUsername = this.getCurrentLoggedUsername();
    		logger.debug("originalUsername : " + originalUsername);
    		
    		try {
	    		
	    		//authenticate with new username
	    		this.authenticate(username);
	    		
	    		//execute task
	    		result = runAsWork.doWork();
	    		
	            return result;
	            
    		} catch (Throwable e) {
    			throw new RuntimeException("Error during run as.", e);
			} finally {
				//reset orginal username authentication
				 if (isAuthenticated(originalUsername) == true)
					 this.authenticate(originalUsername);
				 else 
					 this.logout();
		    }
//    	}
    	
    }        
}
