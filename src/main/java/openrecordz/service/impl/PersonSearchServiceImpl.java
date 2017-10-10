package openrecordz.service.impl;
//package shoppino.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import shoppino.domain.Person;
//import shoppino.domain.Searchable;
//import shoppino.domain.SearchablePerson;
//import shoppino.exception.ShoppinoException;
//import shoppino.search.solr.SolrPersonRepository;
//import shoppino.search.solr.util.QueryUtil;
//import shoppino.service.PersonSearchService;
//import shoppino.service.PersonService;
//import shoppino.service.TenantService;
//import at.pagu.soldockr.core.query.Criteria;
//import at.pagu.soldockr.core.query.Query;
//import at.pagu.soldockr.core.query.SimpleQuery;
//import at.pagu.soldockr.core.query.SimpleStringCriteria;
//
//public class PersonSearchServiceImpl implements PersonSearchService {
//
//	protected final Log log = LogFactory.getLog(getClass());
//
//	@Autowired
//	SolrPersonRepository solrPersonRepository;
//	
//	@Autowired
//	TenantService tenantService;
//	
//	@Autowired
//	private TenantSettingsSetter tenantSettingsSetter;
//	
//	@Autowired
//	PersonService personService;
//	
//	public boolean isIndexerEnabled() {
//		return Boolean.parseBoolean(ConfigThreadLocal.get().get(CustomDataSearchServiceImpl.CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY).toString());
//	}
//	
//	
//	@Override
//	public Person index(Person person) {
//		return solrPersonRepository.save(person);
//		
//	}
//	
////	@Override
////	public void index(List<Product> products) {
////		productSearchRepository.save(products);
////		
////	}
//	
////	public Product getById(String id) throws ResourceNotFoundException {		
////		Product p = productSearchRepository.findOne(id);
////		if (p==null)
////			throw new ResourceNotFoundException("Product not found by search engine with id : " + id);
////		
////		return p;
////	}
////	
//	@Override
//	public void delete(String id) {
//		solrPersonRepository.delete(id);
//		
//	}
//
//
//
//	@Override
//	public List<Person> findByQueryPaginated(String query, int page, int size)
//			throws ShoppinoException {
//	  	
//		Pageable pageable = new PageRequest(page, size);
// 
//    	Query squery = new SimpleQuery(new SimpleStringCriteria(QueryUtil.cleanAndProcessQuery(query)));
//    	squery.addFilterQuery(new SimpleQuery(new Criteria(Searchable.TYPE_FIELD).is(Person.TYPE_PERSON)));
//    	
//    	if (!tenantSettingsSetter.isCrossUsersEnabled(tenantService.getCurrentTenantName())) {
//    		log.debug("CrossUsersEnabled : false");
//    		squery.addFilterQuery(new SimpleQuery(new Criteria(SearchablePerson.TENANT_FIELD).is(tenantService.getCurrentTenantName()).or(SearchablePerson.TENANTS_FIELD).is(tenantService.getCurrentTenantName())));
////    		squery.addFilterQuery(new SimpleQuery(new Criteria(SearchablePerson.TENANT_FIELD).is(tenantService.getCurrentTenantName())));
//    	}   	
////			.addFilterQuery(new SimpleQuery(new Criteria(Searchable.TENANTS_FIELD).is(tenantService.getCurrentTenantName())));	    		    		    
//					   
//
//    	List<Person> returnVal= new ArrayList<Person>();
//    	
//    	Page<Person> people= solrPersonRepository.getSolrOperations().executeListQuery(squery.setPageRequest(pageable), Person.class);
//    	
//    	for (Person p: people) {
//    		returnVal.add(personService.getByUsername(p.getUsername()));
//        }
//    	
//    	
//    	return returnVal;
//        
//	}
//	
//	
//	@Override
//	public long countByQuery(String query)
//			throws ShoppinoException {
//	  	
//	
//    	Query squery = new SimpleQuery(new SimpleStringCriteria(QueryUtil.cleanAndProcessQuery(query)));
//    	squery.addFilterQuery(new SimpleQuery(new Criteria(Searchable.TYPE_FIELD).is(Person.TYPE_PERSON)));
//    	
//    	if (!tenantSettingsSetter.isCrossUsersEnabled(tenantService.getCurrentTenantName())) {
//    		log.debug("CrossUsersEnabled : false");
//    		squery.addFilterQuery(new SimpleQuery(new Criteria(SearchablePerson.TENANT_FIELD).is(tenantService.getCurrentTenantName()).or(SearchablePerson.TENANTS_FIELD).is(tenantService.getCurrentTenantName())));
////    		squery.addFilterQuery(new SimpleQuery(new Criteria(SearchablePerson.TENANT_FIELD).is(tenantService.getCurrentTenantName())));
//    	}   	
//    	
////			.addFilterQuery(new SimpleQuery(new Criteria(Searchable.TENANTS_FIELD).is(tenantService.getCurrentTenantName())));	    		    		    
//			    			   
//
//    
//    	
//    	long count = solrPersonRepository.getSolrOperations().executeCount(squery);
//    	
//    	return count;
//        
//	}
//
//}
