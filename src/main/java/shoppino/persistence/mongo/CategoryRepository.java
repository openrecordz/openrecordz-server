package shoppino.persistence.mongo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import openrecordz.domain.Category;


/**
 * Simple repository interface to manage {@link Category} instances.
 * 
 * @author Andrea Leo
 */ 
public interface CategoryRepository extends CrudRepository<Category,String>{

	List<Category> findByName(String name);
	
	List<Category> findAllByTenants(String tenant, Sort sort);
	
	List<Category> findAllByTenants(String tenant, Pageable pageable);
	
	List<Category> findByParentAndTenants(String parent, String tenant, Sort sort);
}
