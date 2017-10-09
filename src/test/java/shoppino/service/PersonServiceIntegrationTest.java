//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.io.File;
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Person;
//import shoppino.exception.EmailAlreadyInUseException;
//import shoppino.exception.ShoppinoException;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.BrandRepository;
//import shoppino.persistence.mongo.PersonRepository;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//
///**
// * Test case to show the usage of {@link BrandRepository} and thus the Mongo
// * repository support in general.
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class PersonServiceIntegrationTest extends AbstractIntegrationTest {
//
//	@Value("$shoppino{default.avatar.path}")
//	String defaultPhotoPath;
//	
//	@Autowired
//	PersonRepository repository;
//	
//	@Autowired
//	PersonService personService;
//	
//	@Autowired
//	ApplicationContext applicationContext;
//	
//	static final String IMG_FILE_TEST = "photo.jpg";
//	
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		super.setUp();
//	}
//
//	@Test
//	public void add() throws Exception {
//		String username = personService.add("person3", "person3 fn", "person3@test.com");
//		Person p = personService.getByUsername(username);
//		assertEquals("person3", p.getUsername());
//		assertEquals(true, p.getPhoto().contains(defaultPhotoPath));
//		
//		try {
//			personService.add("person4", "person4 fn", "person3@test.com");
//			assertEquals(true, false);
//		} catch (EmailAlreadyInUseException e) {
//			assertEquals(true, true);
//		}
//		
//		
//		try {
//			personService.add("person3", "person5 fn", "person5@test.com");
//			assertEquals(true, false);
//		}
//		catch (ShoppinoException e) {//person already exists
//			assertEquals(true, true);
//		}
//	}
//	
//	@Test
//	public void addWithLogo() throws Exception {
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		String username = personService.add("person3", "person3 fn", "person3@test.com");
//		personService.updateLogo(username, "test.jpg", is.getInputStream());
//		Person p = personService.getByUsername(username);
//		assertEquals("person3", p.getUsername());
//		assertNotNull(p.getPhoto());
////		String partialPathImage = String.format("%1$tY"+File.separator+
////				"%1$tm"+File.separator+
////				"%1$td"+File.separator+
////				"%1$tH"+File.separator, new Date());
////		assertEquals(true, p.getPhoto().contains(partialPathImage));
//	}
//	
//	@Test
//	public void update() throws Exception {
//		String username = personService.add("person4", "person4 fn", "person4@test.com");
//		String usernameUp = personService.update("person4", "person41 fn", "person41@test.com", null);
//		Person p = personService.getByUsername(usernameUp);
//		assertEquals("person4", p.getUsername());
//		assertEquals("person41 fn", p.getFullName());
//		assertEquals("person41@test.com", p.getEmail());
//		assertEquals(true, p.getPhoto().contains(defaultPhotoPath));
//	}
//	
//	@Test
//	public void getByUsername()  throws Exception {
//		repository.save(people);
//		Person p = personService.getByUsername("person1@test.com");
//		assertEquals("person1@test.com", p.getUsername());
//		
//	}
//	
//	
//	@Test
//	public void getByEmail()  throws Exception {
//		String username = personService.add("person3", "person3 fn", "person3@test.com");
//		Person p = personService.getByEmail("person3@test.com");
//		assertEquals("person3", p.getUsername());
//		
//	}
//	
//	@Test
//	public void exists()  throws Exception {
//		repository.save(people);
//		assertEquals(true,personService.exists("person1@test.com"));
//		assertEquals(false,personService.exists("personNotExists@test.com"));
//	}
//	
//
//}
