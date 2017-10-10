package openrecordz.service;

import java.util.List;

import openrecordz.domain.Person;
import openrecordz.exception.OpenRecordzException;

public interface PersonSearchService {

	public boolean isIndexerEnabled();
	
	Person index(Person person);

//	void index(List<Product> products);
	
//	Shop getById(String id) throws ResourceNotFoundException;
//
	void delete(String id);
		
	List<Person> findByQueryPaginated(String query, int page, int size) throws OpenRecordzException;

	long countByQuery(String query) throws OpenRecordzException; 
}
