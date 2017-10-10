package openrecordz.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import openrecordz.domain.Person;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.service.PersonSearchService;
import openrecordz.service.PersonService;

public class SolrPersonIndexer {
	
	@Autowired
	PersonSearchService personSearchService;
		
	@Autowired
	PersonService personService;
	
	
	private static Log log = LogFactory.getLog(SolrPersonIndexer.class);	
	
	public void indexById(String username) throws ResourceNotFoundException  {
		if (personSearchService.isIndexerEnabled()) {
			log.debug("index person with username : " + username);
			
			Person p = personService.getByUsername(username);
			
			Person pIndexed = personSearchService.index(p);
			
			log.info("person with username " + pIndexed.getUsername() + " indexed.");
		
		}else {
			log.debug("isIndexerEnabled  : " + personSearchService.isIndexerEnabled());
		}
	}
	
	public void indexByPerson(Person person) throws ResourceNotFoundException  {
		indexById(person.getUsername());
	}
	
	
	public void removeById(String username)  {
		if (personSearchService.isIndexerEnabled()) {
				
			log.debug("removing index for person with id : " + username);
					
			personSearchService.delete(username);
			
			log.info("person with username " + username + " removed from index.");
		
		}else {
			log.debug("isIndexerEnabled  : " + personSearchService.isIndexerEnabled());
		}
	}
	
}
