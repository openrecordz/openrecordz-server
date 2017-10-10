package openrecordz.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import openrecordz.domain.Category;
import openrecordz.exception.ResourceNotFoundException;




public interface CategoryService {
	
	public static String CATEGORY_SEPARATOR = "/";
	
//	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
//	public String add(String path) throws ResourceNotFoundException;
	
	
	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	public String add(String path, String otype, Integer order, Map<String, String> labels, Integer visibility);
	
	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	@Deprecated
	public String add(String path, String otype, Integer order, Map<String, String> labels, Boolean allowUserContentCreation);
	
//	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
//	@Deprecated
//	public String add(String path, String otype, Integer order);
	
	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	public void delete(String path) throws ResourceNotFoundException;
	
	
	//ATTENZIONE CANCELLA ANCHE TUTTE LE CATEGORIE CONDIVISE
//	@PreAuthorize("hasMTRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
	public void deleteAll();
	
//	@Deprecated
//	public String add(String name, String parent) throws ResourceNotFoundException;
	
	public Category getById(String id) throws ResourceNotFoundException;
	
	@Deprecated
	public Category getById(String id, String locale) throws ResourceNotFoundException;
	@Deprecated
	public Category getByName(String name, String locale) throws ResourceNotFoundException;
	
	public List<Category> findAll();

	@Deprecated
	public List<Category> findAll(String locale);
	
	
	
	
	
	public List<Category> findAll(int page, int size);

	@Deprecated
	public List<Category> findAll(int page, int size, String locale);


	public List<Category> getChildren(String parentCategoryId) throws ResourceNotFoundException;

	@Deprecated
	public List<Category> getChildren(String parentCategoryId, String locale) throws ResourceNotFoundException;
	
	public void clearCache();
	
	public void refreshCache();
}
