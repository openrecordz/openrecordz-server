package openrecordz.persistence.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;

import openrecordz.domain.customdata.CustomData;


/**
 * Simple repository interface to manage {@link Product} instances.
 * 
 * @author Andrea Leo
 */ 
public interface CustomDataRepository extends PagingAndSortingRepository<CustomData,String>{


}
