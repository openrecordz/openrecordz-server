//package shoppino.security;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import shoppino.security.domain.User;
//import shoppino.security.exception.AuthenticationException;
//import shoppino.security.exception.UserNotExistsException;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.security.service.UserService;
//import shoppino.security.service.impl.AuthenticationServiceImpl;
//
///**
//
// * @author Andrea Leo
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//public class AuthenticationServiceIntegrationTest {	
//	
//	@Autowired
//	AuthenticationService authenticationService;
//	
//	@Autowired
//	UserService userService;
//	
//	@Before
//	public void setUp() throws UsernameAlreadyInUseException {
//		//		create user testusername if not exists			
//		if (!userService.exists("testusername"))
//			userService.add("testusername", "testusername");
//		}
//
//	@Test
//	public void testAuthenticate() throws AuthenticationException, UserNotExistsException {
//		authenticationService.authenticate("testusername");
//		User u = userService.getByUsername("testUsername");
//		assertNotNull(u);
//		assertEquals("testusername", authenticationService.getCurrentLoggedUsername());
//	}
//	
//	@Test
//	public void runAs() throws AuthenticationException {
//		authenticationService.logout();
//		String result = authenticationService.runAs(new AuthenticationService.RunAsWork<String>() {
//			@Override
//	        public String doWork() throws Exception {
//	            return "Hello";
//	        }
//		}, "admin","admin");
//		
//		assertEquals("Hello", result);
//	}
//
//
//}
