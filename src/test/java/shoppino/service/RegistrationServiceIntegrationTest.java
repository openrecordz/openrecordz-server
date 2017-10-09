//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Person;
//import shoppino.exception.EmailAlreadyInUseException;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.PersonRepository;
//import shoppino.security.domain.User;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.UserService;
//
///**
// * Test case to show the usage of {@link } and thus the Mongo
// * repository support in general.
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class RegistrationServiceIntegrationTest extends AbstractIntegrationTest {
//
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	PersonService personService;
//	
//	@Autowired
//	RegistrationService registrationService;
//	
//	@Autowired
//	ApplicationContext applicationContext;
//
//	@Autowired
//	PersonRepository repository;
//
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		super.setUp();
//	}
//
//
//	@Test
//	public void register() throws Exception {				
//		
//		registrationService.register("testusername-register", "fullname", "emailtest", "password");
//		Person p = personService.getByUsername("testusername-register");
//		assertEquals("testusername-register", p.getUsername());
//		User u = userService.getByUsername("testusername-register");
//		assertEquals("testusername-register", u.getUsername());
//		assertEquals("password", u.getPassword());
//		userService.delete("testusername-register");
//		personService.delete("testusername-register");
//		
//		userService.add("testusername-register-error", "emailtest-error");
//		
//		try {
//			registrationService.register("testusername-register-error", "fullname", "emailtest-error", "password");
//			assertEquals(true, false);
//		}catch (UsernameAlreadyInUseException e) {
//			assertEquals(true, true);
//		}
//		
//		userService.delete("testusername-register-error");
//		assertEquals(false,personService.exists("testusername-register-error"));
//		
//		personService.add("testusername-register-NOTerror", "fullname", "emailtest-error");
//		try {
//			registrationService.register("testusername-register-error-dup-email", "fullname", "emailtest-error", "password");
//			assertEquals(true, false);
//		}catch (EmailAlreadyInUseException e) {
//			assertEquals(true, true);
//		}
//		assertEquals(false,personService.exists("testusername-register-error-dup-email"));
//		assertEquals(false,userService.exists("testusername-register-error-dup-email"));
//	}
//
//
//}
