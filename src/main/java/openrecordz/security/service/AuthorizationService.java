package openrecordz.security.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import openrecordz.security.exception.UserNotExistsException;

public interface AuthorizationService {

	public boolean isSuperAdministrator(String username);
	
	public boolean isAdministrator(String username);
	
	
	public Collection<? extends GrantedAuthority> getAuthorities(String username)throws UserNotExistsException;
	

	//this must be into Authorization Service
	public void addRole(String username, String role) throws UserNotExistsException ;
	
	public void deleteRoles(String username) throws UserNotExistsException;
}
