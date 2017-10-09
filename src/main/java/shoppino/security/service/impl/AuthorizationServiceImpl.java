package shoppino.security.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import shoppino.domain.Tenant;
import shoppino.security.exception.UserNotExistsException;
import shoppino.security.service.AuthorizationService;
import shoppino.security.service.UserService;
import shoppino.service.TenantService;

public class AuthorizationServiceImpl implements AuthorizationService {

	protected final Log logger = LogFactory.getLog(getClass());

	public static String SUPER_ADMIN_ROLE_NAME = "ROLE_SUPERADMIN";
	public static String ADMIN_ROLE_NAME = "ROLE_ADMIN";
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDetailsManager userDetailsService;
	
	@Autowired
	TenantService tenantService;

	
	
	
	public boolean isSuperAdministrator(String username) {
		return (getAllAuthorities(username).contains(new SimpleGrantedAuthority(SUPER_ADMIN_ROLE_NAME)));
	}
	public boolean isAdministrator(String username) {
		return isSuperAdministrator(username) || (getAllAuthorities(username).contains(new SimpleGrantedAuthority(ADMIN_ROLE_NAME)));
	}
	
	private Collection<? extends GrantedAuthority> getAllAuthorities(String username) {
		//return all the autorities for all tenants
		return userDetailsService.loadUserByUsername(username).getAuthorities();
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities(String username) throws UserNotExistsException {
		
		//for user exists check
		userService.getByUsername(username);
		
		Collection<GrantedAuthority> tenantAuthorities = new ArrayList<GrantedAuthority>();
		
		try {
			Collection<? extends GrantedAuthority> authorities = userDetailsService.loadUserByUsername(username).getAuthorities();
			
			logger.debug("authorities : " + authorities);
			
			for (GrantedAuthority grantedAuthority : authorities) {
				
				//autority for public and private tenant
				if (!grantedAuthority.getAuthority().contains("@")){
					 tenantAuthorities.add(grantedAuthority);
					 logger.debug("added public authority : " + grantedAuthority);
					 continue;
				 }
				
				 if (tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE)) {
					 
				 }else if (tenantService.getCurrentTenantType().equals(Tenant.MODERATE_TENANT_TYPE)){
					 if (grantedAuthority.getAuthority().contains("@"+tenantService.getCurrentTenantName())){
						 tenantAuthorities.add(grantedAuthority);
						 logger.debug("added provate authority : " + grantedAuthority);
					 }
				 }
			 }
		}catch (UsernameNotFoundException unnfe) {
			//if userDetailsService.loadUserByUsername(username).getAuthorities(); has not authorities spring thorow stupid org.springframework.security.core.userdetails.UsernameNotFoundException: User alessiacarratta has no GrantedAuthority
			logger.warn("User : "+username + " has no autorities. " + unnfe.getMessage());
		}
		 
		return tenantAuthorities;
	}
	
	
	@Override
	@Deprecated
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void addRole(String username, String role) throws UserNotExistsException {
		//TODO use UserDetailsManager indeed mysql 
		userService.addRole(username, role);
	}

	@Override
	@Deprecated
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteRoles(String username) throws UserNotExistsException {
		userService.deleteRoles(username);
	}
	
//	@Override
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public void addAutority(String username, String role) throws UserNotExistsException {
//		userDetailsService.loadUserByUsername(username)..getAuthorities().add()
//	}

	
}
