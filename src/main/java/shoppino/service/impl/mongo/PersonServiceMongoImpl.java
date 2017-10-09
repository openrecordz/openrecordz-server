package shoppino.service.impl.mongo;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;

import openrecordz.domain.Person;
import openrecordz.domain.Searchable;
import openrecordz.domain.customdata.CustomData;
import openrecordz.domain.customdata.CustomDataImpl;
import openrecordz.domain.properties.Property;
import openrecordz.domain.properties.PropertyImpl;
import openrecordz.exception.EmailAlreadyInUseException;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.ShoppinoException;
import openrecordz.persistence.mongo.PersonRepository;
import shoppino.security.exception.AuthorizationRuntimeException;
import shoppino.security.service.AuthenticationService;
import shoppino.service.ImageService;
import shoppino.service.PersonService;
import shoppino.service.TenantService;


public class PersonServiceMongoImpl implements PersonService {
	
	protected Log log = LogFactory.getLog(getClass());
	
	@Value("$shoppino{default.avatar.path}")
	String defaultPhotoPath;
	
	@Autowired
	PersonRepository repository;

	@Autowired
	ImageService imageService;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	AuthenticationService authenticationService;
	

	@Autowired
	MongoOperations operations;
	
//	@Autowired
//	MongoOperations operations;
	
	@Override
	public List<Person> findByQuery(String queryStr, Integer page, Integer size, String direction, String sortField) {
		
		Integer skip = null;
		
		if (page!=null && size !=null) {
			skip = page*size;
			log.debug("skip row : " +skip);
		}

		
		BasicQuery query = new BasicQuery(queryStr);
		log.debug("direction : " + direction);
		log.debug("sortField : " + sortField);
		
		
//		if (checkReservedCharsEnabled && type.startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
//			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
//		
		
		
		if (query.getQueryObject().containsField(Searchable.TENANTS_FIELD))
			query.getQueryObject().removeField(Searchable.TENANTS_FIELD);
		
		query.addCriteria(Criteria.where(Searchable.TENANTS_FIELD).is(tenantService.getCurrentTenantName()));
		
			
		
				
		if (sortField!=null && direction!=null) {
			
			if (direction.equals("asc"))
				direction = Order.ASCENDING.toString();
			else if (direction.equals("des") || direction.equals("desc"))
				direction = Order.DESCENDING.toString();
			
			query.sort().on(sortField, Order.valueOf(direction));
		}
		
		if (skip!=null) {
			query.skip(skip).limit(size);
		}
		
		log.debug("query : " + query);
		
		return operations.find(query, Person.class);
	}
	
	
	
	@Override
	public long countByQuery(String queryStr){ 
		
		log.debug("queryStr : " + queryStr);

		
		BasicQuery query = new BasicQuery(queryStr);
//		
		
		
		if (query.getQueryObject().containsField(Searchable.TENANTS_FIELD))
			query.getQueryObject().removeField(Searchable.TENANTS_FIELD);
		
		query.addCriteria(Criteria.where(Searchable.TENANTS_FIELD).is(tenantService.getCurrentTenantName()));
		
			
		
				
		
		
		return operations.count(query, Person.class);
	}
	
	
	@Override
	@Cacheable(value="person", key="#username")
	public Person getByUsername(String username) throws ResourceNotFoundException {
		Person p = repository.findOne(username);
		if (p==null)
			throw new ResourceNotFoundException("Person " + username + " not found.");
		
		return p;
	}
	
	@Override
	public Person getByEmail(String email) throws ResourceNotFoundException {
		
		List<Person> people=repository.findByEmail(email);
		if (people.size()==0)
			throw new ResourceNotFoundException("Person not found with email : "  + email);
		
		if (people.size()>1)
			throw new RuntimeException("Found multiple people with email : "  + email);
		
		return people.get(0);
			
	}
	
	@Override
	public String add(String username, String fullName, String email) throws EmailAlreadyInUseException, ShoppinoException {
		if (exists(username))
			throw new ShoppinoException("Person already exists with username : " + username);
		
		try{
			Person pEmail = this.getByEmail(email);
			if (pEmail!=null)
				throw new EmailAlreadyInUseException(email);
		}catch (ResourceNotFoundException e) {}
		
		Person p = new Person(username);
		p.setFullName(fullName);
		p.setEmail(email);
		p.setPhoto(defaultPhotoPath);
		p.setDefaultPhoto(true);
//		p.setCreatedBy(authenticationService.getCurrentLoggedUsername());
//		p.setModifiedBy(authenticationService.getCurrentLoggedUsername());
		p.setCreatedBy(username);
		p.setModifiedBy(username);		
		p.setTenant(tenantService.getCurrentTenantName());//registration tenant
		p.addTenant(tenantService.getCurrentTenantName());//tenants where person is logged in
		//set modification date
		p.setModifiedOn(new Date());
		return repository.save(p).getUsername();
	}
	
	@CacheEvict(value = "person", key="#username")
	@Override
	public String update(String username, String fullName, String email, String photo) throws ResourceNotFoundException,EmailAlreadyInUseException {
		if (!exists(username))
			throw new ResourceNotFoundException("Person " + username + " not found.");
		
		try{
			Person pEmail = this.getByEmail(email);
			if (pEmail!=null && !pEmail.getUsername().equals(username))	//check if other person is me
				throw new EmailAlreadyInUseException(email);
		}catch (ResourceNotFoundException e) {}
		
		Person pOld= this.getByUsername(username);
		
		Person p = new Person(username);
		p.setFullName(fullName);
		p.setEmail(email);
		
		//in generale dovrebbe entrare sempre in questo if ma a causa di un bug alcuni person non hanno createdby quindi per questi entriamo in else e settiamo createdby ad username
		if (pOld!=null && pOld.getCreatedBy()!=null)
			p.setCreatedBy(pOld.getCreatedBy());
		else
			p.setCreatedBy(username);
		
		p.setModifiedBy(username);		
		//set modification date
		p.setModifiedOn(new Date());
		p.setTenant(tenantService.getCurrentTenantName());
		p.setTenants(pOld.getTenants());
		
		if (photo==null) {
			p.setPhoto(defaultPhotoPath);
			p.setDefaultPhoto(true);
		} else {
			p.setPhoto(photo);
			p.setDefaultPhoto(false);
		}
		return repository.save(p).getUsername();
	}

	@CacheEvict(value = "person", key="#username")
	@Override
	public void joinCurrentTenant(String username)
			throws ResourceNotFoundException {
		
		Person p = this.getByUsername(username);
		
		if (!p.getTenants().contains(tenantService.getCurrentTenantName())) {
			p.addTenant(tenantService.getCurrentTenantName());
			repository.save(p);
			log.info("User : " + username + " has joined tenant : " + tenantService.getCurrentTenantName());	
		}else {
			log.debug("User : " + username + " already belongs to tenant : " + tenantService.getCurrentTenantName());
		}
		
	}
	
	@CacheEvict(value = "person", key="#username")
	@Override
	public void disjoinCurrentTenant(String username)
			throws ResourceNotFoundException {
		
		Person p = this.getByUsername(username);
			
		if(p.getTenant()!=null && p.getTenant().equals(tenantService.getCurrentTenantName())) {
			p.setTenant(null);		
			repository.save(p);
			log.info("User : " + username + " has disjoined core tenant : " + tenantService.getCurrentTenantName());
		}
		
		if (p.getTenants().contains(tenantService.getCurrentTenantName())) {
			p.getTenants().remove(tenantService.getCurrentTenantName());
			repository.save(p);
			log.info("User : " + username + " has disjoined tenant : " + tenantService.getCurrentTenantName());	
		}else {
			log.debug("User : " + username + " doesn't belong to tenant : " + tenantService.getCurrentTenantName());
		}
		
	}
	
	@CacheEvict(value = "person", key="#username")
	@Override
	public void delete(String username) throws ResourceNotFoundException {
		if (!exists(username))
			throw new ResourceNotFoundException("Person " + username + " not found.");

		repository.delete(username);
		
	}
	
	public boolean exists(String username) {
		try {
			if (getByUsername(username)==null)
				return false;
			else
				return true;
		}catch (ResourceNotFoundException e) {
			return false;
		}
	}

	@CacheEvict(value = "person", key="#id")
	@Override
	public Person updateLogo(String id, String name, InputStream in)
			throws ShoppinoException {
		Person p = repository.findOne(id);
		try{		
			p.setPhoto(imageService.save(name, in));
			p.setDefaultPhoto(false);
		} catch (Exception e) {
			log.error("Error updating person photo " + e);
			throw new ShoppinoException(e);
		}
		return repository.save(p);
	}

	@CacheEvict(value = "person", key="#username")
	@Override
	public String updateSettings(String username, boolean publishOnFb)
			throws ResourceNotFoundException {
		Person p = getByUsername(username);
		
		p.setPublishOnFb(publishOnFb);
		
		return repository.save(p).getUsername();
	}

	@CacheEvict(value = "person", key="#id")
	@Override
	public void removeLogo(String id)
		throws ShoppinoException {
			Person p = repository.findOne(id);
			try{		
				p.setPhoto(defaultPhotoPath);
				p.setDefaultPhoto(true);
			} catch (Exception e) {
				log.error("Error removing  person photo " + e);
				throw new ShoppinoException(e);
			}
			repository.save(p);
		
	}

	
	@CacheEvict(value = "person", key="#personId")
	@Override
	public <T> void addProperty(String personId, String id, String displayName,
			List<T> values) throws ResourceNotFoundException {
	
		Person p = this.getByUsername(personId);
		
		Property prop = new PropertyImpl<T>(id, displayName, values);
		
		p.addProperty(id, prop);
		
		repository.save(p);
	}
	
	@CacheEvict(value = "person", key="#personId")
	@Override
	public <T> void addProperty(String personId, String id, String displayName,
			T value) throws ResourceNotFoundException {
		List values = new ArrayList<T>();
		values.add(value);
		
		this.addProperty(personId, id, displayName, values);
	}
	
	@CacheEvict(value = "person", key="#personId")
	@Override
	public <T> void updateProperty(String personId, String id, String displayName,
			T value) throws ResourceNotFoundException {
		
		Person p = this.getByUsername(personId);
		
		List values = new ArrayList<T>();
		values.add(value);
		
		Property prop = new PropertyImpl<T>(id, displayName, values);
		
		p.removeProperty(id);
		p.addProperty(id, prop);
		
		repository.save(p);
	}
	
	
	@CacheEvict(value = "person", key="#personId")
	@Override
	public void setProperties(String personId, String propertiesAsJson)
			throws ResourceNotFoundException, ShoppinoException {
		
		Person p = this.getByUsername(personId);
		
		Map<String, Property> properties = new HashMap<String, Property>();	
		
		log.debug("propertiesAsJson : " + propertiesAsJson);
		
		if (propertiesAsJson!=null && !propertiesAsJson.equals("")) {
			
			if (propertiesAsJson.startsWith("[") && propertiesAsJson.endsWith("]"))
				propertiesAsJson = propertiesAsJson.substring(1, propertiesAsJson.length()-1);
			
			log.debug("propertiesAsJson after remove [] : " + propertiesAsJson);
			
			//TODO init one time only
			JSONParser parser = new JSONParser();
	
			try{
	//			Object propertiesObj = parser.parse(propertiesAsJson);
				JSONObject propertiesObj = (JSONObject)parser.parse(propertiesAsJson);
				
	//			 Map json = (Map)parser.parse(jsonText, containerFactory);
				    Iterator iter = propertiesObj.entrySet().iterator();
				    while(iter.hasNext()){
				        Map.Entry entry = (Map.Entry)iter.next();
				        log.debug(entry.getKey() + "=>" + entry.getValue());
				        
				        JSONObject propertyObject = (JSONObject) entry.getValue();
						Property prop = new PropertyImpl(propertyObject);
						properties.put(entry.getKey().toString(), prop);
				      }
				  
			  }
			  catch(ParseException pe){
			   log.error("parsing error at position: " + pe.getPosition());
			   log.error("parsing error " + pe);
			   throw new ShoppinoException("Parsing error", pe);
			  }
			
			}
		
		p.setProperties(properties);
				
		
		repository.save(p);
		
	}
	
	
//	@Override
//	public List<Person> findAll() {
//		repository.findAll()
//	}

	@CacheEvict(value = "person", allEntries = true)
	@Override
	public void clearCache() {
		log.info("Cache for person evicted...");
	}

	

	
}

	