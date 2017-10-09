//package shoppino.service;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.solr.client.solrj.SolrServerException;
//
//import shoppino.domain.customdata.CustomData;
//import shoppino.exception.ShoppinoException;
//
//public interface CustomDataSearchService {
//
//	public boolean isIndexerEnabled();
//	
//	void index(CustomData CustomData) throws SolrServerException, IOException;
//
////	List<CustomData> findByQueryLocationPaginated(String fquery,
////			double[] location, int page, int size, Integer status)
////			throws ShoppinoException;
//
//	long countByFullQuery(String fquery, Integer status)
//			throws ShoppinoException;
//
//	List<CustomData> findByQueryLocationPaginated(String fquery, int page,
//			int size, Integer status) throws ShoppinoException;
//
////	void index(List<CustomData> CustomDatas);
//	
////	CustomData getById(String id) throws ResourceNotFoundException;
////
////	void delete(String id);
////	
////	List<CustomData> findByLocationNearPaginated(String query, double[] location,int page, int size, Integer status) throws ShoppinoException ;
////	
////	List<CustomData> findByCategoryAndLocationNearPaginated(String category, double[] location, int page, int size, Integer status) throws ShoppinoException;
////	
////	@Deprecated
////	List<CustomData> findByQueryPaginated(String query, int page, int size, Integer status) throws ShoppinoException;
////
////	long countByQuery(String query, Integer status) throws ShoppinoException;
////
//////	List<CustomData> findAllGroupedPaginatedSortByScore(int page, int size);		
////	List<GroupedCustomData> findByLocationNearPaginatedGrouped(String query, double[] location, int page, int size, int groupLimit) throws ShoppinoException;
////
////	List<CustomData> findByQueryLocationPaginated(String fullQuery, double[] location,
////			int page, int size, Integer status) throws ShoppinoException;
////	
////	long countByFullQuery(String fullQuery, Integer status) throws ShoppinoException;
//	
//
//}
