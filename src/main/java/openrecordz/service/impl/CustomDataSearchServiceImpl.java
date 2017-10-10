package openrecordz.service.impl;
//package shoppino.service.impl;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrInputDocument;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import shoppino.domain.Searchable;
//import shoppino.domain.Statusable;
//import shoppino.domain.customdata.CustomData;
//import shoppino.exception.ShoppinoException;
//import shoppino.search.solr.SolrCustomDataRepository;
//import shoppino.service.CustomDataSearchService;
//import shoppino.service.CustomDataService;
//import shoppino.service.TenantService;
//public class CustomDataSearchServiceImpl implements CustomDataSearchService {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//
//	@Autowired
//	SolrCustomDataRepository customDataSearchRepository;
//	
//	
//	
//	@Autowired
//	TenantService tenantService;
//	
//	@Autowired
////	@Qualifier("personA")
//	CustomDataService customDataService;
//	
//	
//	private static String SFIELD="location";
//	private static String SORT_FIELD="score desc,geodist() asc,modifiedon desc";
//	
//	public static String CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY = "indexerEnabled";
//	
//	public boolean isIndexerEnabled() {
//		return Boolean.parseBoolean(ConfigThreadLocal.get().get(CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY).toString());
//	}
//	
//	
//	@Override
//	public void index(CustomData customData) throws SolrServerException, IOException {
//		
////		CustomData.setLocationAsString(CustomData.getLatitude() + ", " + CustomData.getLongitude());
////		try {
////			Shop shop=shopService.getById(CustomData.getShop());
////			CustomData.setShopFormattedAddress(shop.getFormattedAddress());
////		} catch (ResourceNotFoundException e) {
////			logger.error("Shop not found while idexing CustomData", e);
////		}
//		Map<String,Object>  cd = customData.toMap();
////		return customDataSearchRepository.save(cd);
//		
//		Map<String,Object>  newcd = new LinkedHashMap<String, Object>();
//		
//		SolrInputDocument doc = new SolrInputDocument();
//		
//		doc.addField("id", customData.getId());
//		doc.addField("type", "CustomData");
////		doc.addField("_type", value)
//		doc.addField("_createdby_s", customData.getCreatedBy());
//		doc.addField("_modifiedby_s", customData.getModifiedBy());
//		doc.addField("_createdon_dt", customData.getCreatedOn());
//		doc.addField("_modifiedon_dt", customData.getModifiedOn());
//		doc.addField("_status_i", customData.getStatus());
//		doc.addField("_type_s", customData.getType());
//		doc.addField("tenants", tenantService.getCurrentTenantName());
//
//		if (cd.containsKey("_location"))
//        	doc.addField("_location_p", cd.get("_location"));
//			
//		
//		StringBuilder sbfulltext = new StringBuilder(); 
//		
//		Iterator it = cd.entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pair = (Map.Entry)it.next();
//	        
//		        if (!pair.getKey().equals("id") && !pair.getKey().toString().startsWith("_")) {
//		        	sbfulltext.append(pair.getValue() + " ");
//			        if(pair.getValue() instanceof String) {
//			        	doc.addField(pair.getKey().toString()+"_s", pair.getValue());
//			        } else if(pair.getValue() instanceof Integer) {
//			        	doc.addField(pair.getKey().toString()+"_i", pair.getValue());
//			        }
//		        }
////	        newcd.put(pair.getKey()+"", new SolrInputField(pair.getValue()));
////	        System.out.println(pair.getKey() + " = " + pair.getValue());
//	    }
//	    
//	    
//		 
//	    doc.addField("text", sbfulltext.toString());
//	      
//	      customDataSearchRepository.getSolrOperations().getSolrServer().add(doc);
//	     
//	      customDataSearchRepository.getSolrOperations().getSolrServer().commit(); 
//		
//	}
//	
//
//	@Override
//	public List<CustomData> findByQueryLocationPaginated(String fquery,
//			int page, int size, Integer status) throws ShoppinoException {
////																								  q=*&sfield=location&pt=40.191511,18.210879&sort=score desc,geodist() asc,modifiedon desc
////		http://www.dressique.localhost.com:8880/shoppino-ultima/service/v1/search/CustomDatas?fquery=q%3D*%26sfield%3Dlocation%26pt%3D40.191511%2C18.210879%26sort%3Dscore%20desc%2Cgeodist()%20asc%2Cmodifiedon%20desc&lat=40.222&lon=34.2222		
//	
//		SolrQuery solrQuery = new SolrQuery();
//    	 solrQuery.setStart((page)*size);
//    	 solrQuery.setRows(size);
//    	 
//    	 logger.debug("fquery : " + fquery);
//    	 
//    	 //TODO do it better :)
//    	 List<NameValuePair> params = URLEncodedUtils.parse(fquery, Charset.forName("UTF-8"));
//
// 		for (NameValuePair param : params) {
// 			 solrQuery.add(param.getName(), param.getValue());    	
// 		  logger.debug(param.getName() + " : " + param.getValue());
// 		}
// 		
////    	 solrQuery.add("q", QueryUtil.cleanAndProcessQuery(query));    	
////    	 solrQuery.add("sfield", SFIELD);
////    	 solrQuery.add("pt", location[0]+","+location[1]);    	 
////    	 solrQuery.add("sort", SORT_FIELD);	    	
//    	 
// 		//TODO removing special filter query and other parameters
//// 		 solrQuery.removeFilterQuery(Searchable.TYPE_FIELD);
//// 		 solrQuery.removeFilterQuery(Searchable.TENANTS_FIELD);
// 		
//    	 solrQuery.add("fq", Searchable.TYPE_FIELD+":"+ "CustomData");
//    	 solrQuery.add("fq", Searchable.TENANTS_FIELD+":"+tenantService.getCurrentTenantName());
//
//// non controllo le date di validita per fullquery..    	 
////    	 SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
////    	 dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
////    	 String rangeEnd = "endDate:[" + dateFormatUTC.format(DateUtil.getToday()) + " TO * ]";
////    	 solrQuery.add("fq", rangeEnd);
//    	 if (status!=null)
//    		 solrQuery.add("fq", Statusable.STATUS_FIELD+":["+status+" TO *]");
//    	 
//    	 logger.debug("solrQuery: " + solrQuery.toString());
//    	 
//		
//    		QueryResponse queryResponse=null;
//			try {
//				queryResponse = customDataSearchRepository.getSolrOperations().getSolrServer().query(solrQuery);
//		} catch (SolrServerException e) {
//			logger.error("Error executing solr query: " + solrQuery.toString());
//			throw new ShoppinoException("Error executing solr query: " + solrQuery.toString());
//		}
//    	List<SolrDocument> CustomDatas = queryResponse.getResults();
//    	
//    	    	    	
//    	//get from db
//    	List<CustomData> returnCustomDatas= new ArrayList<CustomData>();
//    	for (SolrDocument solrDocument : CustomDatas) {		
//    		returnCustomDatas.add(customDataService.getById(solrDocument.get("id").toString()));
//    	}
//    	
//    	
//    	
//    	//calculate distance  
////    	try{
////    		if (location!=null && LocationUtils.isValidLocation(location[0], location[1])) {
////	    	Double userLatitude = location[0];
////	    	
////	    	Double userLongitude =  location[1];
////	    	    	   
////		    	if (userLatitude != null && userLongitude != null ) {   
////		    		LatLng userLocation = new LatLng(userLatitude, userLongitude);
////		    		
////		    		for (CustomData CustomData : returnCustomDatas) {			   
////				    	LatLng destinatioLocation= new LatLng(CustomData.getLatitude(), CustomData.getLongitude());
////				    	double distance = LatLngTool.distance(userLocation, destinatioLocation, LengthUnit.KILOMETER);
////				    	CustomData.setDistanceAsKm(distance);
////		    		}
////			    	
////		    	}
////    		}
////    	}catch (Exception e) {
////    		logger.error("Error while calculating distance. ",e);
////		}
//    	    	    	   
//    	
//    	
//    	return returnCustomDatas;
//    	
//	}
//	
//	
//	
//	
//
//	@Override
//	public long countByFullQuery(String fquery,  Integer status) throws ShoppinoException {
//	
//		SolrQuery solrQuery = new SolrQuery();
//    	 
//    	 logger.debug("fquery : " + fquery);
//    	 
//    	 //TODO do it better :)
//    	 List<NameValuePair> params = URLEncodedUtils.parse(fquery, Charset.forName("UTF-8"));
//
// 		for (NameValuePair param : params) {
// 			 solrQuery.add(param.getName(), param.getValue());    	
// 		  logger.debug(param.getName() + " : " + param.getValue());
// 		}
// 		
////    	 solrQuery.add("q", QueryUtil.cleanAndProcessQuery(query));    	
////    	 solrQuery.add("sfield", SFIELD);
////    	 solrQuery.add("pt", location[0]+","+location[1]);    	 
////    	 solrQuery.add("sort", SORT_FIELD);	    	
//    	 
// 		//TODO removing special filter query and other parameters
//// 		 solrQuery.removeFilterQuery(Searchable.TYPE_FIELD);
//// 		 solrQuery.removeFilterQuery(Searchable.TENANTS_FIELD);
// 		
//    	 solrQuery.add("fq", Searchable.TYPE_FIELD+":"+ "CustomData");
//    	 solrQuery.add("fq", Searchable.TENANTS_FIELD+":"+tenantService.getCurrentTenantName());
//
//// non controllo le date di validita per fullquery..    	 
////    	 SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
////    	 dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
////    	 String rangeEnd = "endDate:[" + dateFormatUTC.format(DateUtil.getToday()) + " TO * ]";
////    	 solrQuery.add("fq", rangeEnd);
//    	 
//    	 solrQuery.add("fq", Statusable.STATUS_FIELD+":["+status+" TO *]");
//    	 
//    	 logger.debug("solrQuery: " + solrQuery.toString());
//    	 
//		
//    		QueryResponse queryResponse=null;
//			try {
//				queryResponse = customDataSearchRepository.getSolrOperations().getSolrServer().query(solrQuery);
//		} catch (SolrServerException e) {
//			logger.error("Error executing solr query: " + solrQuery.toString());
//			throw new ShoppinoException("Error executing solr query: " + solrQuery.toString());
//		}
//
//    	
//		return queryResponse.getResults().getNumFound();
//    	    	    	
//    	
//	}
//	
//
//
//	
//
//}
