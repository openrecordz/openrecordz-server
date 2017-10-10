package openrecordz.security.service;

import openrecordz.security.domain.User;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.exception.UsernameAlreadyInUseException;

public interface UserService {

	public User getByUsername(String username) throws UserNotExistsException;
	
	public void add(String username, String password) throws UsernameAlreadyInUseException;
	
	public void update(String username, String newPassword) throws UserNotExistsException;
	
	public void delete(String username) throws UserNotExistsException;
	
	public boolean exists(String username);
	
	
	//this must be into Authorization Service
	@Deprecated
	public void addRole(String username, String role) throws UserNotExistsException ;
	
//	this must be into Authorization Service
//	deletes of roles of a user
	@Deprecated
	public void deleteRoles(String username) throws UserNotExistsException ;
	
}
