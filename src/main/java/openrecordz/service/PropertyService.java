package openrecordz.service;

import org.springframework.security.access.prepost.PreAuthorize;

import openrecordz.exception.ResourceNotFoundException;




//@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
public interface PropertyService  {
	
//	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	//aop cloud code
	@PreAuthorize("isAuthenticated()")
	public String add(String key, String value);
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	//aop cloud code
	@PreAuthorize("isAuthenticated()")
	public String update(String key, String value) throws ResourceNotFoundException;
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	@PreAuthorize("isAuthenticated()")
	public String get(String key) throws ResourceNotFoundException;
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	//do not use delete for JS (reserved word)
	@PreAuthorize("isAuthenticated()")
	public void remove(String key) throws ResourceNotFoundException;
	
}
