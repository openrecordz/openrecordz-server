//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Image;
//import shoppino.persistence.AbstractIntegrationTest;
//
///**
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class ImageServiceIntegrationTest extends AbstractIntegrationTest {
//	
//	static final String IMG_FILE_TEST = "photo.jpg";
//	
//	@Autowired
//	ImageService imageService;
//	
//	@Autowired
//	ApplicationContext applicationContext;
//	
//	@Test
//	public void save() throws Exception {
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		String url = imageService.save("test.jpg", is.getInputStream());
//		assertNotNull(url);		
//		assertEquals(true, url.contains("jpg"));
//	}
//	
//	@Test
//	public void saveAsImage() throws Exception {
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		Image image= imageService.saveAsImage("test.jpg", is.getInputStream());
//		assertNotNull(image.getUrl());
//		assertNotNull(image.getSize());		
//		assertEquals(new Integer(640), image.getSize()[0]);
//		assertEquals(new Integer(640), image.getSize()[1]);
//		assertEquals(true, image.getUrl().contains("jpg"));
//	}
//	
//	@Test
//	public void saveWithPath() throws Exception {
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		String url = imageService.save("test.jpg", is.getInputStream(),"/testpath/t2");
//		assertNotNull(url);
//		assertEquals(true, url.contains("jpg"));
//		assertEquals(true, url.contains("/testpath/t2"));
//	}
//		
//	@Test
//	public void getSize() throws Exception {
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		String url = imageService.save("test.jpg", is.getInputStream());
//		assertNotNull(url);
//		assertEquals(true, url.contains("jpg"));
//		Integer[] size = imageService.getImageSize(url);
//		assertEquals(new Integer(640), size[0]);
//		assertEquals(new Integer(640), size[1]);
//		
//	}
//	
//
//}
