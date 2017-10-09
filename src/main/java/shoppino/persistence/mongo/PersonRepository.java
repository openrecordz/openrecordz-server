package shoppino.persistence.mongo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import shoppino.domain.Person;


/**
 * Simple repository interface to manage {@link Person} instances.
 * 
 * @author Andrea Leo
 */ 
public interface PersonRepository extends CrudRepository<Person,String>{

	List<Person> findByEmail(String email);

}
