//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import shoppino.url.UrlDAO;
//
///**
// * Test case to show the usage of {@link } and thus the Mongo
// * repository support in general.
// * 
// * @author Andrea Leo
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//public class UrlServiceIntegrationTest 
//// extends AbstractIntegrationTest 
// {
//
//	@Autowired
//	UrlService urlService;
//	
//	@Autowired
//	UrlDAO urlDAO;
//	
//	@Before
//	public void purgeRepository() {
//		urlDAO.deleteAll();
//	}
//	
//	@Test
//	public void save(){
//		String uid = urlService.save("/test/url");
//		String url=urlService.getByUID(uid);
//		assertEquals("/test/url", url);
//	}
//
//	
//	
//	
//}
