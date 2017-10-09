//package shoppino.security;
//
//import static org.junit.Assert.assertEquals;
//
//import javax.sql.DataSource;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import shoppino.security.domain.User;
//import shoppino.security.exception.AuthenticationException;
//import shoppino.security.exception.UserNotExistsException;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.security.service.UserService;
//
///**
//
// * @author Andrea Leo
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//public class UserServiceIntegrationTest {
//	
////	@Autowired
////	UserDetailsManager userDetailsManager;
//	
////	@Autowired
//	JdbcTemplate template;
//	
//	@Autowired
//	DataSource hsqlDataSource;
//	
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	AuthenticationService authenticationService;
//	
//	@Before
//	public void setUp() {
//		template = new JdbcTemplate(hsqlDataSource);
//		
//		template.execute("DROP TABLE IF EXISTS authorities");
//        template.execute("DROP TABLE IF EXISTS users");
//         
//		template.execute(
//	            "create table users(username VARCHAR(50) NOT NULL PRIMARY KEY,password VARCHAR(50) NOT NULL,enabled BOOLEAN NOT NULL);");
//        template.execute(
//            "create table authorities(username VARCHAR(50) NOT NULL,authority VARCHAR(50) NOT NULL,CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(username) REFERENCES users(username));");
//        template.execute("create unique index ix_auth_username ON authorities(username,authority);");
//        
//        template.execute("insert into users values('admin','admin', 1);");
//        template.execute("INSERT INTO authorities VALUES('admin','ROLE_ADMIN');");
//		}
//
//
////	@Test
////	public void testUserCreation() {
////		GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_USER");
////		Collection<GrantedAuthority> gaColl = new ArrayList<GrantedAuthority>();
////		gaColl.add(ga);
////		User user = new User("testUsername", "password", true, true, true, true, gaColl);
////		userDetailsManager.createUser(user);
////		
////		UserDetails ud= userDetailsManager.loadUserByUsername("testUsername");
////		assertEquals("testUsername", ud.getUsername());
////		assertEquals("password", ud.getPassword());
////		
////		Authentication user1auth = new TestingAuthenticationToken(ud.getUsername(), ud.getPassword());
////		user1auth.setAuthenticated(true);
////	}
//	
//	@Test
//	public void testUserCreation() throws UsernameAlreadyInUseException, UserNotExistsException {
//		userService.add("testUsername", "password");
//		User u = userService.getByUsername("testUsername");
//		assertEquals("testUsername", u.getUsername());
//		assertEquals("password", u.getPassword());
//		
//		try {
//			userService.add("testUsername", "password");
//			assertEquals(true, false);
//		} catch (UsernameAlreadyInUseException e) {
//			assertEquals(true, true);
//		} 
//		
//		
//	}
//	
//	@Test
//	public void delete() throws UsernameAlreadyInUseException, UserNotExistsException {
//		userService.add("testUsername", "password");
//		User u = userService.getByUsername("testUsername");
//		assertEquals("testUsername", u.getUsername());
//		assertEquals("password", u.getPassword());
//		
//		userService.delete("testUsername");
//		assertEquals(false, userService.exists("testUsername"));
//		
//	}
//	
//	@Test
//	public void addRole() throws UserNotExistsException, UsernameAlreadyInUseException, AuthenticationException {
//		authenticationService.authenticate("admin");
//		userService.add("testUsername", "password");
//		userService.addRole("testUsername", "ROLE_TEST");
//	}
//
//	
//	@Test
//	public void runAsAddRole() throws AuthenticationException, UsernameAlreadyInUseException {
//		userService.add("testusername", "password");
//		authenticationService.authenticate("testusername");
//		
//		Boolean result = authenticationService.runAs(new AuthenticationService.RunAsWork<Boolean>() {
//			@Override
//	        public Boolean doWork() throws Exception {
//				userService.addRole("testusername", "ROLE_TEST");
//	            return true;
//	        }
//		}, "admin","admin");
//		
//		assertEquals(true, result);
//		assertEquals("testusername", authenticationService.getCurrentLoggedUsername());
//	}
//	
////	@Test
////	public void testLoadUser() {
////		UserDetails ud = userDetailsManager.loadUserByUsername("rod");
////		assertEquals("rod", ud.getUsername());
////		assertEquals("koala", ud.getPassword());
////	}
//
//}
