package shoppino.service;

import java.io.InputStream;
import java.util.List;

import shoppino.domain.Person;
import shoppino.exception.EmailAlreadyInUseException;
import shoppino.exception.ResourceNotFoundException;
import shoppino.exception.ShoppinoException;





public interface PersonService {
	
	public List<Person> findByQuery(String queryStr, Integer page, Integer size, String direction, String sortField);
	
	public long countByQuery(String queryStr);
	
	
	
	public Person getByUsername(String username) throws ResourceNotFoundException;
	
	public Person getByEmail(String email) throws ResourceNotFoundException;
	
	public String add(String username, String fullName, String email) throws EmailAlreadyInUseException, ShoppinoException;
	
	public Person updateLogo(String id, String name, InputStream in) throws ShoppinoException;
	
	public String update(String username, String fullName, String email, String photo) throws ResourceNotFoundException,EmailAlreadyInUseException;
	
	public void joinCurrentTenant(String username)throws ResourceNotFoundException;
	
	public void disjoinCurrentTenant(String username)throws ResourceNotFoundException;
	
	public void delete(String username) throws ResourceNotFoundException;
	
	public boolean exists(String username);
	
	public String updateSettings(String username, boolean publishOnFb) throws ResourceNotFoundException;
//	public List<Person> findAll();
	
	public void removeLogo(String id) throws ShoppinoException;
	
	
	public <T> void addProperty(String personId, String id, String displayName, List<T> values) throws ResourceNotFoundException;
	
	public <T> void addProperty(String personId, String id, String displayName, T values) throws ResourceNotFoundException; 
	
	public <T> void updateProperty(String personId, String id, String displayName, T values) throws ResourceNotFoundException;

	public void setProperties(String personId, String propertiesAsJson) throws ResourceNotFoundException, ShoppinoException;
	
	public void clearCache();
}