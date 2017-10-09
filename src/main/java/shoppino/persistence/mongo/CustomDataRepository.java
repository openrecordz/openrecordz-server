package shoppino.persistence.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;

import shoppino.domain.customdata.CustomData;


/**
 * Simple repository interface to manage {@link Product} instances.
 * 
 * @author Andrea Leo
 */ 
public interface CustomDataRepository extends PagingAndSortingRepository<CustomData,String>{


}
