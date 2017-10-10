package openrecordz.service;

import java.util.List;

import openrecordz.domain.Person;
import openrecordz.exception.ShoppinoException;

public interface PersonSearchService {

	public boolean isIndexerEnabled();
	
	Person index(Person person);

//	void index(List<Product> products);
	
//	Shop getById(String id) throws ResourceNotFoundException;
//
	void delete(String id);
		
	List<Person> findByQueryPaginated(String query, int page, int size) throws ShoppinoException;

	long countByQuery(String query) throws ShoppinoException; 
}
