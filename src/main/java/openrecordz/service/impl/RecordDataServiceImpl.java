package openrecordz.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

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

import openrecordz.domain.Statusable;
import openrecordz.domain.customdata.CustomData;
import openrecordz.domain.customdata.CustomDataImpl;
import openrecordz.domain.customdata.CustomDatable;
import openrecordz.domain.customdata.Record;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.persistence.mongo.CustomDataRepository;
import openrecordz.security.exception.AuthorizationRuntimeException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.security.service.AuthorizationService;
import openrecordz.service.RecordDataService;
import openrecordz.service.TenantService;

public class RecordDataServiceImpl implements RecordDataService{
	
	protected Log log = LogFactory.getLog(getClass());
	
	

	@Autowired
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
	
	public RecordDataServiceImpl() {
		checkReservedCharsEnabled = true;
	}
	
	@Override
	public String add(String dsId, String className, String json) {
//		http://www.mkyong.com/mongodb/java-mongodb-insert-a-document/
//		String json = "{'database' : 'mkyongDB','table' : 'hosting'," +
//				  "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";
//			 
//				DBObject dbObject = (DBObject)JSON.parse(json);
//			 
//				collection.insert(dbObject);
		
		log.debug("dsId : "+ dsId);
		log.debug("className : "+ className);
		log.debug("json : "+ json);
		
//		if (checkReservedCharsEnabled && className.startsWith("_") && authenticationService.isAdministrator())
		if (checkReservedCharsEnabled && className.startsWith("_") && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		Record record = new Record(className, json);
		record.setDatasetRefId(dsId);
		record.addTenant(tenantService.getCurrentTenantName());
		record.setCreatedBy(authenticationService.getCurrentLoggedUsername());
		record.setModifiedBy(authenticationService.getCurrentLoggedUsername());
		record.setModifiedOn(new Date());
		
		addLocation(record);
		
		CustomData returncdata = customDataRepository.save(record);
		
		return returncdata.getId();
	}


//	@Override
//	public <U extends CustomDatable> String add(U object) {
//		return this.add(object.getType(), object.toJSON());
//	}
	
	
	public CustomData getByIdInternal(String id) throws ResourceNotFoundException {
		return this.getById(id);
	}
	
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
	public String update(String id, String className, String json) throws ResourceNotFoundException {
		return this.update(id, className, json, false);
	}
	
	@Override
	@CacheEvict(value = "customData", key="#id")
	public String update(String id, String className, String json, Boolean versioningEnabled) throws ResourceNotFoundException {
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
		
		CustomData returncdata = customDataRepository.save(cdata);
		
		//versioning
		if (versioningEnabled) {
			cdataOld.put("_status", Statusable.STATUS_VERSIONED);
			cdataOld.put("_versioned_from_id", returncdata.getId());
			customDataRepository.save(cdataOld);
		}
		
		
		
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
	public void removeAll(String dsId) {
		Query query = Query.query(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		query.addCriteria(Criteria.where("_dataset_ref_id").is(dsId));
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
	public List<CustomData> findByQuery(String queryStr, String dataset_ref_id, String type) {
		return this.findByQuery(queryStr, dataset_ref_id,type, null, null, null, null, null);
	}
	
	@Override
	public List<CustomData> findByQueryInternal(String queryStr, String dataset_ref_id, String type) {
		return this.findByQuery(queryStr, dataset_ref_id, type, null, null, null, null, null);
	}
	
	@Override
	public List<CustomData> findByQuery(String queryStr, String dataset_ref_id, String type, Integer page, Integer size, String direction, String sortField) {
		return this.findByQuery(queryStr,dataset_ref_id, type, page, size, direction, sortField, null);
	}
	
	@Override
	public List<CustomData> findByQueryInternal(String queryStr, String dataset_ref_id, String type, Integer page, Integer size, String direction, String sortField, Integer status) {
		return this.findByQuery(queryStr, dataset_ref_id, type, page, size, direction, sortField, status);
	}
	
	@Override
//	@Cacheable(value="customData")
	public List<CustomData> findByQuery(String queryStr, String dataset_ref_id, String type, Integer page, Integer size, String direction, String sortField, Integer status) {
		
		Integer skip = null;
		
		if (page!=null && size !=null) {
			skip = page*size;
			log.debug("skip row : " +skip);
		}

		
		BasicQuery query = new BasicQuery(queryStr);
		log.debug("_dataset_ref_id : " + dataset_ref_id);
		log.debug("type : " + type);
		log.debug("direction : " + direction);
		log.debug("sortField : " + sortField);
		log.debug("status : " + status);
		
		
//		if (checkReservedCharsEnabled && type.startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
//			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
//		
		
		
		if (query.getQueryObject().containsField(CustomDataImpl.TENANTS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TENANTS_PROP_KEY);
		
		query.addCriteria(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		
		if (query.getQueryObject().containsField(CustomDataImpl.TYPE_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TYPE_PROP_KEY);
				
		
		query.addCriteria(Criteria.where("_dataset_ref_id").is(dataset_ref_id));
		
		if (type!=null)
			query.addCriteria(Criteria.where(CustomDataImpl.TYPE_PROP_KEY).is(type));
		
		if (query.getQueryObject().containsField(CustomDataImpl.STATUS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.STATUS_PROP_KEY);
		
		Integer statusQuery=Statusable.STATUS_VISIBLE;
		
		if (status!=null){
			statusQuery=status;
		}
		
		query.addCriteria(Criteria.where(CustomDataImpl.STATUS_PROP_KEY).gte(statusQuery));
		
				
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
	public long countByQueryInternal(String queryStr,String dataset_ref_id,
			String type, Integer status) {
		
		
		
		BasicQuery query = new BasicQuery(queryStr);
		log.debug("type : " + type);
		log.debug("queryStr : " + queryStr);
		
		
//		if (checkReservedCharsEnabled && className.startsWith("_")  && !authorizationService.isAdministrator(authenticationService.getCurrentLoggedUsername()))
//			throw new AuthorizationRuntimeException("ClassName can't start with _. This is a reserved char...");
		
		
		
		if (query.getQueryObject().containsField(CustomDataImpl.TENANTS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TENANTS_PROP_KEY);
		
		query.addCriteria(Criteria.where(CustomDataImpl.TENANTS_PROP_KEY).is(tenantService.getCurrentTenantName()));
		
		if (query.getQueryObject().containsField(CustomDataImpl.TYPE_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.TYPE_PROP_KEY);
		
		if (type!=null)
			query.addCriteria(Criteria.where(CustomDataImpl.TYPE_PROP_KEY).is(type));
		
		query.addCriteria(Criteria.where("_dataset_ref_id").is(dataset_ref_id));
		
		if (query.getQueryObject().containsField(CustomDataImpl.STATUS_PROP_KEY))
			query.getQueryObject().removeField(CustomDataImpl.STATUS_PROP_KEY);
		
		Integer statusQuery=Statusable.STATUS_VISIBLE;
		
		if (status!=null){
			statusQuery=status;
		}
		
		query.addCriteria(Criteria.where(CustomDataImpl.STATUS_PROP_KEY).gte(statusQuery));
		
		
		log.debug("query : " + query);
		
		return operations.count(query, CustomData.class);
		
	}

	
	
	@Override
	@Deprecated
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

	
	

	

	
	
}

	