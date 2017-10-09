package openrecordz.aspect;
//package shoppino.aspect;
//
//import java.io.IOException;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import shoppino.domain.customdata.CustomData;
//import shoppino.exception.ResourceNotFoundException;
//import shoppino.service.CustomDataSearchService;
//import shoppino.service.CustomDataService;
//
//public class SolrCustomDataIndexer {
//	
//	@Autowired
//	CustomDataSearchService customDataSearchService;
//	
//	@Autowired
//	CustomDataService customDataService;
//	
//	private static Log log = LogFactory.getLog(SolrCustomDataIndexer.class);	
//	
//	public void indexById(String id) throws ResourceNotFoundException  {
//	
//		
//		
////		****************ATTENZIONE L'INDICIZZAZIONE SI ABILITA CON SPRING PROFILE PASSANDOLO COME PARAMETRO NEI SETTINGS DI TOMCAT
//		if (customDataSearchService.isIndexerEnabled()) {
//			log.debug("index custom data with id : " + id);
//			
//			CustomData cd = customDataService.getById(id);
//			
//			try {
//				customDataSearchService.index(cd);
//			} catch (SolrServerException e) {
//				log.error("Error indexing customdata with id: " + cd.getId(),e);
//			} catch (IOException e) {
//				log.error("Error indexing customdata with id: " + cd.getId(),e);
//			}
//			
//			log.info("custom data with id " + cd.getId() + " indexed.");
//		}else {
//			log.debug("isIndexerEnabled  : " + customDataSearchService.isIndexerEnabled());
//		}
//	}
//	
//
//	
//}
