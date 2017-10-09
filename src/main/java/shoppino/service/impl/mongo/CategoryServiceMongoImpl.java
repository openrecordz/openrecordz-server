package shoppino.service.impl.mongo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import shoppino.domain.Category;
import shoppino.exception.ResourceNotFoundException;
import shoppino.exception.ShoppinoRuntimeException;
import shoppino.persistence.mongo.CategoryRepository;
import shoppino.service.CategoryService;
import shoppino.service.TenantService;
import shoppino.web.interceptor.CategoryInterceptor;

import com.github.slugify.Slugify;

public class CategoryServiceMongoImpl implements CategoryService
//	, MessageSourceAware 
	{
	
	protected Log log = LogFactory.getLog(getClass());
	
	
	@Autowired
	CategoryRepository repository;
	
	@Autowired
	MongoOperations operations;
	
	@Autowired
	TenantService tenantService;
	

//	@Autowired
////	@Qualifier(value="categoryMessageSource")
//	private MessageSource messageSource;
	
//	private static String CATEGORY_MESSAGE_SUFFIX = "category.";
	 
//	public void setMessageSource(MessageSource messageSource) {
//		this.messageSource = messageSource;
//	}
	
//	@Deprecated
//	public String add(String name, String parent) throws ResourceNotFoundException {
//		String newPath = "";
//		
//		if (parent!=null) {
//			Category parentCat = this.getById(parent);
//			log.debug("parent category path : " + parentCat.getPath());
//			newPath = parentCat.getPath();
//		}
//		
//		newPath = newPath + CATEGORY_SEPARATOR + name;
//		
//		log.debug("new category path : " + newPath);
//		
//		Category c = new Category(name,parent, newPath);
//		
//		c.addTenant(tenantService.getCurrentTenantName());
//		
//	
//		Category newC=repository.save(c); 
//		return newC.getId();
//	}
	
	
	//also remove end /
	public static String encodePath(String pathUnencoded) {
		Log log = LogFactory.getLog(CategoryServiceMongoImpl.class);
		
		log.debug("pathUnencoded : " + pathUnencoded);
		
		//converto - in space cosi slugify riporta a -
		pathUnencoded = pathUnencoded.replace("-", " ");
		
		//encode path
//		Slugify slg = new Slugify();
		String[] pathAsArray=pathUnencoded.split("/");
		String encodedPathElement;
		String encodedPath ="";
		for (String pathElement : pathAsArray) {
			
			encodedPathElement=Slugify.slugify(pathElement);
			encodedPath=encodedPath+"/"+encodedPathElement;
		}
		encodedPath=encodedPath.substring(1, encodedPath.length());
		log.debug("encoded path with tenant: " + encodedPath);
		
		return encodedPath;
	}
	
//	@Deprecated
//	public String add(String path, String otype, Integer order)  {
//		this.add(path, otype, order, null);
//	}
	
	
	@Deprecated
	public String add(String path, String otype, Integer order, Map<String, String> labels, Integer visibility)  {
		log.debug("path : " + path);
		log.debug("otype : " + otype);
		log.debug("order : " + order);
		log.debug("labels : " + labels);
		log.debug("visibility : " + visibility);
		
		
		if (!path.startsWith("/"))
			throw new ShoppinoRuntimeException("Category path must start with / character.");
		
		path = "/"+tenantService.getCurrentTenantName()+path;
		log.debug("path with tenant: " + path);
		
		String encodedPath = encodePath(path);
		
		Category c = new Category(encodedPath);
		c.setOtype(otype);
		c.setOrder(order);
		c.setLabels(labels);
//		c.setAllowUserContentCreation(allowUserContentCreation);
		c.setVisibility(visibility);
		c.addTenant(tenantService.getCurrentTenantName());
		
	
		Category newC=repository.save(c); 
		
		log.info("Category : " + encodedPath + " , otype "+ otype + " , order " + order + " labels " + labels +" is created");
		
//		this.refreshCache();
		return newC.getId();
	}
	
	
	
	@Deprecated
	public String add(String path, String otype, Integer order, Map<String, String> labels, Boolean allowUserContentCreation)  {
		log.debug("path : " + path);
		log.debug("otype : " + otype);
		log.debug("order : " + order);
		log.debug("labels : " + labels);
		log.debug("allowUserContentCreation : " + allowUserContentCreation);
		
		
		if (!path.startsWith("/"))
			throw new ShoppinoRuntimeException("Category path must start with / character.");
		
		path = "/"+tenantService.getCurrentTenantName()+path;
		log.debug("path with tenant: " + path);
		
		String encodedPath = encodePath(path);
		
		Category c = new Category(encodedPath);
		c.setOtype(otype);
		c.setOrder(order);
		c.setLabels(labels);
		c.setAllowUserContentCreation(allowUserContentCreation);
		c.addTenant(tenantService.getCurrentTenantName());
		
	
		Category newC=repository.save(c); 
		
		log.info("Category : " + encodedPath + " , otype "+ otype + " , order " + order + " labels " + labels +" is created");
		
//		this.refreshCache();
		return newC.getId();
	}
	
	
	public void deleteAll() {
	
		Query query = Query.query(Criteria.where("tenants").is(tenantService.getCurrentTenantName()));		
		log.debug("query : " + query);
		operations.remove(query,"category");
		
		 log.info("all categories by tenant " + tenantService.getCurrentTenantName() + " are deleted");
	}
	
	public void delete(String path) throws ResourceNotFoundException {
		log.debug("path : " + path);
		path=encodePath(path);
		
		if (!path.startsWith("/"))
			throw new ShoppinoRuntimeException("Category path must start with / character.");
		
	
		path = "/"+tenantService.getCurrentTenantName()+path;
		log.debug("path with tenant: " + path);
		
		//check if category exists
		Category c = this.getById(path);
		
		if (!c.getTenants().contains(tenantService.getCurrentTenantName()))
			throw new ShoppinoRuntimeException("You can't delete category from other tenant");
		
		repository.delete(path);
		
		 log.info("category : " + path + " is deleted");
	}
	
	
	@Override
	public Category getById(String id) throws ResourceNotFoundException {
		id=encodePath(id);
		Category c= repository.findOne(id);
		if (c==null)
			throw new ResourceNotFoundException("Category not found with id : " + id);
		return c;
	}
	
	@Override
	//@Cacheable(value="category", key="#id")
	public Category getById(String id, String locale) throws ResourceNotFoundException {
		
		Category c= this.getById(id);
//		c.setLabel(messageSource.getMessage(CATEGORY_MESSAGE_SUFFIX + c.getId(), null, c.getName(), new Locale(locale)));
//		c.setLabel(c.getLabels().get("en_US"));
		return c;
	}


	@Override
	public Category getByName(String name, String locale) throws ResourceNotFoundException {
		log.debug("getByName " + name);
		
		List<Category> categories = repository.findByName(name);
		
		if (categories==null || categories.size()==0)
			throw new ResourceNotFoundException("Category not found with name : " + name);
		
		Category c = categories.get(0);
//		c.setLabel(messageSource.getMessage(CATEGORY_MESSAGE_SUFFIX + c.getId(), null, c.getName(), new Locale(locale)));
//		c.setLabel(c.getLabels().get("en_US"));
		return c;
	}
	
	@Override
//	@Cacheable(value="category", key="#id")
	public List<Category> findAll() {
		List<Category> returns= new ArrayList<Category>();
		Iterable<Category> cIt=repository.findAllByTenants(tenantService.getCurrentTenantName(), new Sort(Sort.Direction.ASC, "order"));
		for (Category c : cIt) {
			returns.add(c);			
		}
		return returns;
	}
	
	
	@Override
//	@Cacheable(value="category")
	public List<Category> findAll(String locale) {
		List<Category> returns= new ArrayList<Category>();
		Iterable<Category> cIt=repository.findAllByTenants(tenantService.getCurrentTenantName(), new Sort(Sort.Direction.ASC, "order"));
		for (Category c : cIt) {
//			c.setLabel(messageSource.getMessage(CATEGORY_MESSAGE_SUFFIX + c.getId(), null, c.getName(), new Locale(locale)));
//			c.setLabel(c.getLabels().get("en_US"));
			returns.add(c);			
		}
		return returns;
	}

	
	
	
	
	public List<Category> findAll(int page, int size) {
		List<Category> returns= new ArrayList<Category>();
		Iterable<Category> cIt=repository.findAllByTenants(tenantService.getCurrentTenantName(), new PageRequest(page, size, new Sort(Sort.Direction.ASC, "order")));
		for (Category c : cIt) {
			returns.add(c);			
		}
		return returns;
	}

	@Deprecated
	public List<Category> findAll(int page, int size, String locale){
		List<Category> returns= new ArrayList<Category>();
		for (Category c : this.findAll(page, size)) {
//			c.setLabel(messageSource.getMessage(CATEGORY_MESSAGE_SUFFIX + c.getId(), null, c.getName(), new Locale(locale)));
			returns.add(c);			
		}
		return returns;
	}
	
//	@Cacheable(value="category", key="#tenant")
//	private List<Category> findAllInternal(String locale, String tenant) {
//		
//	}
	
	public List<Category> getChildren(String parentCategoryId) throws ResourceNotFoundException {
//		List<Category> returns= new ArrayList<Category>();
//		Iterable<Category> cIt=repository.findByParentAndTenants(parentCategoryId, tenantService.getCurrentTenantName(), new Sort(Sort.Direction.ASC, "order"));
//		for (Category c : cIt) {
//			returns.add(c);			
//		}
//		return returns;
		
		
		
		Query query = new Query(Criteria.where("parent").is(parentCategoryId).and("tenants").is(tenantService.getCurrentTenantName()));
		query.sort().on("order", Order.ASCENDING);
	
//		return repository.findByCreatedByAndTenants(username, tenantService.getCurrentTenantName(), page, size);
		return operations.find(query, Category.class);
	}

	public List<Category> getChildren(String parentCategoryId, String locale) throws ResourceNotFoundException {
		List<Category> returns= new ArrayList<Category>();
		for (Category c : this.getChildren(parentCategoryId)) {
//			c.setLabel(messageSource.getMessage(CATEGORY_MESSAGE_SUFFIX + c.getId(), null, c.getName(), new Locale(locale)));
			returns.add(c);
		}
		return returns;
	}

	@Override
	public void clearCache() {
		CategoryInterceptor.allCategories = new HashMap<String, List<Category>>();		
		
		
	}
	
	public void refreshCache() {
		CategoryInterceptor.allCategories.put(tenantService.getCurrentTenantName(), this.findAll(0,200));
	}
	

}

	