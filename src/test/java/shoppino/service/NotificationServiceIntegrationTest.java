//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Notification;
//import shoppino.notification.dao.DeviceNotificationDAO;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.NotificationRepository;
//import shoppino.security.exception.AuthenticationException;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.security.service.UserService;
//
///**
// * Test case to show the usage of {@link } and thus the Mongo
// * repository support in general.
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class NotificationServiceIntegrationTest extends AbstractIntegrationTest {
//
//
//	@Autowired
//	NotificationRepository repository;
//	
//	@Autowired
//	NotificationService notificationService;
//	
//	@Autowired
//	ProductService productService;
//	
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	AuthenticationService authenticationService; 
//	
//	@Autowired(required=false)
//	DeviceNotificationDAO deviceNotificationDAO;
//	
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		deviceNotificationDAO.deleteAll();
//		super.setUp();
//	}
//	
//	@Before
//	public void addUser() throws UsernameAlreadyInUseException, AuthenticationException {
//		//		create user testusername if not exists			
//		if (!userService.exists("testusername"))
//			userService.add("testusername", "testusername");	
//		
//		authenticationService.authenticate("testusername");
//			
//	}
//
//	@Test
//	public void register() throws Exception {
//		notificationService.register("testregId", "Android");
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndUsernameAndTypeAndTenant("testregId", "testusername", "Android", "default").size());
//		
//		notificationService.register("testregId" , "Android");
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndUsernameAndTypeAndTenant("testregId", "testusername", "Android", "default").size());
//		
//		authenticationService.logout();
//		assertEquals(false, authenticationService.isAuthenticated());
//		
//		notificationService.register("testregId2", "Android");
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndTypeAndTenant("testregId2", "Android", "default").size());
//		assertEquals(null, deviceNotificationDAO.findByRegIdAndTypeAndTenant("testregId2", "Android", "default").get(0).getUsername());
//		
//		authenticationService.authenticate("testusername2");
//		notificationService.register("testregId2", "Android");
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndTypeAndTenant("testregId2", "Android","default").size());
//		assertEquals("testusername2", deviceNotificationDAO.findByRegIdAndTypeAndTenant("testregId2", "Android","default").get(0).getUsername());
//	}
//	
//	@Test
//	public void unregister() throws Exception {
//		notificationService.register("testregId", "Android");
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndUsernameAndTypeAndTenant("testregId", "testusername", "Android","default").size());
//		
//		notificationService.unregister("testregId", "Android");
//		
//		assertEquals(0, deviceNotificationDAO.findByRegIdAndUsernameAndTypeAndTenant("testregId", "testusername", "Android","default").size());
//		assertEquals(1, deviceNotificationDAO.findByRegIdAndTypeAndTenant("testregId", "Android","default").size());
//	}
//	
//	@Test
//	public void sendAndView() throws Exception {
//		String nId = notificationService.sendLikeNotification(Notification.LIKE_MESSAGE, "testusernameTo", "1");
//		assertEquals(1, notificationService.hasNew("testusernameTo"));
//		
//		Notification n = notificationService.view(nId);
//		assertNotNull(n);
//		assertEquals("testusername", n.getFrom());
//		assertEquals("testusernameTo", n.getTo());
//		assertEquals(Notification.LIKE_MESSAGE, n.getMessage());
//		assertEquals(false, n.isRead());
//		assertEquals("1", n.getRefId());
//		assertEquals(true, notificationService.view(nId).isRead());
//		
//		String nId2 = notificationService.sendLikeNotification(Notification.LIKE_MESSAGE, "testusernameTo", "1");
//		assertNull(nId2);
////		notificationService.delete(nId);
//	}
//	
//	@Test
//	public void sendAndFind() throws Exception {
//		String nId = notificationService.sendLikeNotification(Notification.LIKE_MESSAGE, "testusernameTo", "1");
//		System.out.println(nId);
//		Thread.sleep(1000);
//		String nId2 = notificationService.sendLikeNotification(Notification.LIKE_MESSAGE, "testusernameTo", "2");
//		System.out.println(nId2);
//		Thread.sleep(1000);
//		String nId3 = notificationService.sendLikeNotification(Notification.LIKE_MESSAGE, "testusernameTo", "3");
//		System.out.println(nId3);
//		Thread.sleep(1000);
//		
//		assertEquals(3, notificationService.hasNew("testusernameTo"));
//		
//		List<Notification> notifications = notificationService.findByTo("testusernameTo");
//		assertEquals(nId3, notifications.get(0).getId());
//		assertEquals(false, notifications.get(0).isRead());
//		assertEquals(nId2, notifications.get(1).getId());
//		assertEquals(false, notifications.get(1).isRead());
//		assertEquals(nId, notifications.get(2).getId());
//		assertEquals(false, notifications.get(2).isRead());
//		
//		List<Notification> notifications2 = notificationService.findByTo("testusernameTo");
//		assertEquals(3, notifications2.size());
//		assertEquals(true, notifications2.get(0).isRead());
//		assertEquals(true, notifications2.get(1).isRead());
//		assertEquals(true, notifications2.get(2).isRead());
//	}
//	
//	
//}
