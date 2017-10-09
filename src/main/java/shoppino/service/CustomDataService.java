package shoppino.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import openrecordz.domain.customdata.CustomData;
import openrecordz.domain.customdata.CustomDatable;
import openrecordz.exception.ResourceNotFoundException;




//@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
public interface CustomDataService  {
	
//	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")

	//aop cloud code
	//@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasRole('ROLE_RESTUSER')")
	public String add(String className, String json);
	
//	@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasRole('ROLE_RESTUSER')")
	public <U extends CustomDatable> String add(U object);
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	//aop cloud code
//	@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasRole('ROLE_RESTUSER')")
	public String update(String id, String className, String json) throws ResourceNotFoundException;
		
//	@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasRole('ROLE_RESTUSER')")
	public <U extends CustomDatable> String update(String id, U object)throws ResourceNotFoundException;
	
//	@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasRole('ROLE_RESTUSER')")
	public String patch(String id, String className, String json) throws ResourceNotFoundException;
	
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	@PreAuthorize("isAuthenticated()")
	public CustomData getById(String id) throws ResourceNotFoundException;
	
	public CustomData getByIdInternal(String id) throws ResourceNotFoundException;
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	//do not use delete for JS (reserved word)
	@PreAuthorize("isAuthenticated()")
	public void remove(String id) throws ResourceNotFoundException;
	
	@PreAuthorize("isAuthenticated()")
	public void removeAll(String className);
	
	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
	public void removeAll();
		
	
	
	
	
	
	@PreAuthorize("isAuthenticated()")
	public List<CustomData> findByQuery(String queryStr,String className);
	
//	not authenticated
	public List<CustomData> findByQueryInternal(String queryStr,String className);
	
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	@PreAuthorize("isAuthenticated()")
	public List<CustomData> findByQuery(String queryStr,String className, Integer page, Integer size, String direction, String sortField);
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER') or hasMTRole('ROLE_USER') or hasRole('ROLE_SUPERADMIN')")
	@PreAuthorize("isAuthenticated()")
	public List<CustomData> findByQuery(String queryStr,String className, Integer page, Integer size, String direction, String sortField, Integer status);
	
	public List<CustomData> findByQueryInternal(String queryStr,String className, Integer page, Integer size, String direction, String sortField, Integer status);
	
	public long countByQueryInternal(String queryStr,String className, Integer status);
	
	public List findClasses();
	
//	public void addLocation();
}
