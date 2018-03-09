package openrecordz.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import openrecordz.domain.customdata.CustomData;
import openrecordz.domain.customdata.CustomDataImpl;
import openrecordz.domain.customdata.CustomDatable;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.persistence.mongo.CustomDataRepository;
import openrecordz.security.exception.AuthorizationRuntimeException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.security.service.AuthorizationService;
import openrecordz.service.CustomDataService;
import openrecordz.service.TenantService;

public class CustomDataServiceImpl implements CustomDataService{
	
	protected Log log = LogFactory.getLog(getClass());
	
	

	@Autowired
//	@Qualifier("mongoTemplateAuth")
	MongoOperations operations;
	
	@Autowired
	CustomDataRepository customDataRepository;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	AuthorizationService authorizationService;

	public boolean checkReservedCharsEnabled;
	
	public CustomDataServiceImpl() {
		checkReservedCharsEnabled = true;
	}
	
	@Override
	public String add(String className, String json) {
//		http://www.mkyong.com/mongodb/java-mongodb-insert-a-document/
//		String json = "{'database' : 'mkyongDB','table' : 'hosting'," +
//				  "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";
//			 
//				DBObject dbObject = (DBObject)JSON.parse(json);
//			 
//				collection.insert(dbObject);
		
		log.debug("className : "+ className);
		log.debug("json : "+ json);
		
//		if (checkReservedCharsEnabled && className.startsWith("_") && authenticationService.isAdministrator())
		if (checkReservedCharsEnabled && className.startsWith("_") && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		CustomData cdata = new CustomDataImpl(className, json);
		cdata.addTenant(tenantService.getCurrentTenantName());
		cdata.setCreatedBy(authenticationService.getCurrentLoggedUsername());
		cdata.setModifiedBy(authenticationService.getCurrentLoggedUsername());
		cdata.setModifiedOn(new Date());
		
		addLocation(cdata);
		addDateField(cdata);
		
		CustomData returncdata = customDataRepository.save(cdata);
		
		return returncdata.getId();
	}


	@Override
	public <U extends CustomDatable> String add(U object) {
		return this.add(object.getType(), object.toJSON());
	}
	
	
	public CustomData getByIdInternal(String id) throws ResourceNotFoundException {
		CustomDataImpl customData = (CustomDataImpl)customDataRepository.findOne(id);
		
		if (customData==null)
			throw new ResourceNotFoundException("Resource not found with id : " + id);
		//not check tenant ownership
//		if (!customData.getTenants().contains(tenantService.getCurrentTenantName())) {
//			throw new ResourceNotFoundException("Resource not found into this tenant with id : " + id);
//		}
		return customData;	}
	
	@Override
	//@Cacheable(value="customData")
	@Cacheable(value="customData", key="#id")
	public CustomData getById(String id) throws ResourceNotFoundException {
		CustomDataImpl customData = (CustomDataImpl)customDataRepository.findOne(id);
		
		if (customData==null)
			throw new ResourceNotFoundException("Resource not found with id : " + id);
		
		if (!customData.getTenants().contains(tenantService.getCurrentTenantName())) {
			throw new ResourceNotFoundException("Resource not found into this tenant with id : " + id);
		}
		
		return customData;
	}

	@Override
	@CacheEvict(value = "customData", key="#id")
	public String update(String id, String className, String json) throws ResourceNotFoundException {
		log.debug("id : "+ id);
		log.debug("className : "+ className);
		log.debug("json : "+ json);
		
		if (checkReservedCharsEnabled && className.startsWith("_") && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		CustomData cdataOld = getById(id);
//
//		cdata.set
//		cdata.setModifiedBy(authenticationService.getCurrentLoggedUsername());
//		cdata.setModifiedOn(new Date());
//		//TODO SETTARE JSON
//		
//		CustomData returncdata = customDataRepository.save(cdata);
////		customDataRepository.save(cdata);
//		return returncdata.getId();
////		return id;
		
		CustomData cdata = new CustomDataImpl(className, json);
		cdata.setId(id);
		cdata.addAllTenants(cdataOld.getTenants());
		cdata.setCreatedBy(cdataOld.getCreatedBy());
		cdata.setCreatedOn(cdataOld.getCreatedOn());
		cdata.setModifiedBy(authenticationService.getCurrentLoggedUsername());
		cdata.setModifiedOn(new Date());
		
		addLocation(cdata);
		addDateField(cdata);

		
		CustomData returncdata = customDataRepository.save(cdata);
		
		return returncdata.getId();
	}
	
	
	@Override
	@CacheEvict(value = "customData", key="#id")
	public String patch(String id, String className, String json) throws ResourceNotFoundException {
		log.debug("id : "+ id);
		log.debug("className : "+ className);
		log.debug("json : "+ json);
		
		if (checkReservedCharsEnabled && className.startsWith("_") && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		CustomData cdataOld = getById(id);

//		CustomData cdata = new CustomDataImpl(className, json);
		
		Map newJsonMap=(Map)JSON.parse(json);
//		if (newJsonMap.("_"))
//		controllare che newjsonmap non abbia campi che iniziano con _
			
		cdataOld.patch(newJsonMap);
		
		

		cdataOld.setModifiedBy(authenticationService.getCurrentLoggedUsername());
		cdataOld.setModifiedOn(new Date());
		
		addLocation(cdataOld);
		addDateField(cdataOld);

		CustomData returncdata = customDataRepository.save(cdataOld);
		
		return returncdata.getId();
	}
	
	@Override
	public <U extends CustomDatable> String update(String id, U object) throws ResourceNotFoundException {
		return this.update(id, object.getType(), object.toJSON());
	}
	
	@Override
	@CacheEvict(value = "customData", key="#id")
	public void remove(String id) throws ResourceNotFoundException {
		CustomData customData = this.getById(id);
		
		if (checkReservedCharsEnabled && customData.getType().startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		operations.remove(customData, "customData");
//		operations.remove(customData);
//		customDataRepository.delete(customData);
	}
	
	@Override
	//TODO evict only classname entries.
	@CacheEvict(value = "customData", allEntries = true)
	public void removeAll(String className) {
		Query query = Query.query(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		query.addCriteria(Criteria.where(CustomDataImpl.TYPE_PROP_KEY).is(className));
		log.debug("query : " + query);
		operations.remove(query, "customData");
	}
	
	@Override
	@CacheEvict(value = "customData", allEntries = true)
	public void removeAll() {
		Query query = Query.query(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		log.debug("query : " + query);
		operations.remove(query, "customData");
	}
	
	@Override
	public List<CustomData> findByQuery(String queryStr, String type) {
		return this.findByQuery(queryStr, type, null, null, null, null, null);
	}
	
	@Override
	public List<CustomData> findByQueryInternal(String queryStr, String type) {
		return this.findByQuery(queryStr, type, null, null, null, null, null);
	}
	
	@Override
	public List<CustomData> findByQuery(String queryStr, String type, Integer page, Integer size, String direction, String sortField) {
		return this.findByQuery(queryStr, type, page, size, direction, sortField, null);
	}
	
	@Override
	public List<CustomData> findByQueryInternal(String queryStr, String type, Integer page, Integer size, String direction, String sortField, Integer status) {
		return this.findByQuery(queryStr, type, page, size, direction, sortField, status);
	}
	
	
	@Override
	public List<CustomData> findByQuery(String queryStr, String className, Integer page, Integer size, String direction,
			String sortField, Integer status) {
		return this.findByQuery(queryStr, className, page, size, direction, sortField, status, false);
	}

	@Override
	public List<CustomData> findByQueryInternal(String queryStr, String className, Integer page, Integer size,
			String direction, String sortField, Integer status, boolean crossDomainSearch) {
		return this.findByQuery(queryStr, className, page, size, direction, sortField, status, crossDomainSearch);
	}
	
	@Override
//	@Cacheable(value="customData")
	public List<CustomData> findByQuery(String queryStr, String type, Integer page, Integer size, String direction, String sortField, Integer status, boolean crossDomainSearch) {
		
		Integer skip = null;
		
		if (page!=null && size !=null) {
			skip = page*size;
			log.debug("skip row : " +skip);
		}

		
		BasicQuery query = new BasicQuery(queryStr);
		log.debug("type : " + type);
		log.debug("direction : " + direction);
		log.debug("sortField : " + sortField);
		log.debug("status : " + status);
		
		
		if (checkReservedCharsEnabled && type!=null && type.startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		
		if (crossDomainSearch==false) {
			if (query.getQueryObject().containsField(CustomDataImpl.TENANTS_PROP_KEY))
				query.getQueryObject().removeField(CustomDataImpl.TENANTS_PROP_KEY);
			
			query.addCriteria(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		}
		
		
		if (query.getQueryObject().containsField(CustomDataImpl.TYPE_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TYPE_PROP_KEY);
		
		if (type!=null) {
			query.addCriteria(Criteria.where(CustomDataImpl.TYPE_PROP_KEY).is(type));
		}
		
		if (query.getQueryObject().containsField(CustomDataImpl.STATUS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.STATUS_PROP_KEY);
		
		if (status!=null)
			query.addCriteria(Criteria.where(CustomDataImpl.STATUS_PROP_KEY).gte(status));
		
				
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
		
		return operations.find(query, CustomData.class);
	}

	@Override
	public long countByQueryInternal(String queryStr,
			String className, Integer status) {
		
		
		
		BasicQuery query = new BasicQuery(queryStr);
		log.debug("className : " + className);
		log.debug("queryStr : " + queryStr);
		
		
		if (checkReservedCharsEnabled && className.startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		
		if (query.getQueryObject().containsField(CustomDataImpl.TENANTS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TENANTS_PROP_KEY);
		
		query.addCriteria(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		
		if (query.getQueryObject().containsField(CustomDataImpl.TYPE_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TYPE_PROP_KEY);
		
		if (className!=null) {
			query.addCriteria(Criteria.where(CustomDataImpl.TYPE_PROP_KEY).is(className));
		}		
		
		if (query.getQueryObject().containsField(CustomDataImpl.STATUS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.STATUS_PROP_KEY);
		
		if (status!=null)
			query.addCriteria(Criteria.where(CustomDataImpl.STATUS_PROP_KEY).gte(status));
		
		
		log.debug("query : " + query);
		
		return operations.count(query, CustomData.class);
		
	}

	
	
	@Override
	public List findClasses() {
//		http://stackoverflow.com/questions/26629542/mongodb-distinct-with-condition-in-java
//		BasicDBObject match = new BasicDBObject();
//		match.put("$query", new BasicDBObject("price", new BasicDBObject("$gt", 10)));
		return operations.getCollection("customData").distinct("_type",new BasicDBObject("_tenants", tenantService.getCurrentTenantName()));
	}
	
	public boolean isCheckReservedCharsEnabled() {
		return checkReservedCharsEnabled;
	}

	public void setCheckReservedCharsEnabled(boolean checkReservedCharsEnabled) {
		this.checkReservedCharsEnabled = checkReservedCharsEnabled;
	}

	private void addLocation(CustomData customData) {
		try  {
	//		BasicQuery query = new BasicQuery("{}");
	//		query.addCriteria(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is("comunedisoleto"));
			
	//		List<CustomData> allData=operations.findAll(CustomData.class);
	//		for (CustomData customData : allData) {
				if (!customData.containsKey("_location") ) {
					if (customData.containsKey("_latitude") && customData.containsKey("_longitude")){
						double[] pLocation = new double[2];
						pLocation[0]=Double.parseDouble(customData.get("_latitude").toString());
						pLocation[1]=Double.parseDouble(customData.get("_longitude").toString());
						customData.put("_location", pLocation);
//						customDataRepository.save(customData);
	//					break;
	//				}
	//				
				}
			}
		}catch (Exception e) {
				log.error("Error adding _location property", e);
		}
		
	}

	
	private void addDateField(CustomData customData) {
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		log.debug("addDateField");

		try  {

			for(String key: customData.keySet()){
				log.debug("addDateField key:" + key);

				if (key.endsWith("_d")) {
					log.debug("addDateField key end with _d:" + key);

						customData.put(key, format.parse(customData.toMap().get(key).toString()));
				}
			}
		}catch (Exception e) {
				log.error("Error adding date field", e);
		}
		
	}

	

	
	

	

	
	
}

	