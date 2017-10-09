//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import junit.framework.Assert;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Shop;
//import shoppino.domain.Statusable;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.PersonRepository;
//import shoppino.search.solr.SolrPersonRepository;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.security.service.UserService;
///**
// * Test case to show the usage of {@link ProductService} and thus the Mongo
// * repository support in general.
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class PersonServiceSolrIntegrationTest extends AbstractIntegrationTest {
//
//
//	@Autowired
//	PersonRepository repository;
//	
//	@Autowired
//	PersonService personService;
//	
//	@Autowired
//	SolrPersonRepository solrPersonRepo;
//	
//	@Autowired
//	CategoryService categoryService;
//	
//	@Autowired
//	AuthenticationService authenticationService; 
//	
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	TenantService tenantService;
//	
//
//	
//	@Autowired
//	ApplicationContext applicationContext;
//	
//	static final String IMG_FILE_TEST = "photo.jpg";
//	static final String CATEGORY = "/accessori";
//	static final String CATEGORY1 = "/uomo";
//	
//	@Before
//	public void addUser() throws UsernameAlreadyInUseException {
//		//		create user testusername if not exists			
//		if (!userService.exists("testusername"))
//			userService.add("testusername", "testusername");	
//			
//	}
//		
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		solrShopRepo.deleteAll();
//		super.setUp();
//	}
//
//	@Test
//	public void add() throws Exception {
//		Assert.assertEquals(0, repository.count());
//		Assert.assertEquals(0, solrShopRepo.count());
//		 
//		//authenticate with andrea.leo
//		authenticationService.authenticate("testusername");
//				
//		double[] l={0,0};
//		String id=shopService.add("City SRL", "City Moda Description" ,"Lecce", "Italia", "via XX1 Aprile", l);
//		Shop s =shopService.getById(id);
//		assertNotNull(s);
//				
//		Shop sSolr = solrShopRepo.findOne(id);
//		
//		assertEquals(s.getId(), sSolr.getId());
//		assertEquals(s.getName(), sSolr.getName());
//		assertEquals(s.getFormattedAddress(), sSolr.getFormattedAddress());
//		assertEquals(s.getTenants(), sSolr.getTenants());				
//	}
//	
//	
//	@Test
//	public void save() throws Exception {
//		Assert.assertEquals(0, repository.count());
//		Assert.assertEquals(0, solrShopRepo.count());
//		 
//		//authenticate with andrea.leo
//		authenticationService.authenticate("testusername");
//				
//		double[] l={0,0};
//		String id=shopService.add("City SRL", "City Moda Description" ,"Lecce", "Italia", "via XX1 Aprile", l);
//		shopService.save(id, "City SRL2", "City Moda Description", null, null, null, "new formatted address", null, null, l, null, null, Statusable.STATUS_ENABLED);
//		Shop s =shopService.getById(id);
//		assertNotNull(s);
//				
//		Shop sSolr = solrShopRepo.findOne(id);
//		
//		assertEquals(s.getId(), sSolr.getId());
//		assertEquals(s.getName(), sSolr.getName());
//		assertEquals(s.getFormattedAddress(), sSolr.getFormattedAddress());
//		assertEquals("new formatted address", sSolr.getFormattedAddress());
//		assertEquals(s.getTenants(), sSolr.getTenants());				
//	}
//	
////	@Test
////	public void update() throws Exception {
////		Assert.assertEquals(0, repository.count());
////		Assert.assertEquals(0, solrShopRepo.count());
////		 
////		//authenticate with andrea.leo
////		authenticationService.authenticate("testusername");
////				
////		double[] l={0,0};
////		
////		List<String> categoriesWomen=new ArrayList<String>();
////		String catidw = categoryService.add("women", null);
////		categoriesWomen.add(catidw);
////		
////		String sid=shopService.add("City SRL", "City Moda Description" ,"Lecce", "Italia", "via XX1 Aprile", l);
////		
////		String id= productService.add(product1.getDescription(), product1.getBrand(), categoriesWomen, product1.getPrice(), sid);
////		
////		productService.update(id, product2.getDescription(), product2.getBrand(), categoriesWomen, product2.getPrice(), sid);
////		
////		Product p = productService.getById(id);
////		assertNotNull(p);
////		assertEquals("Product2 description", p.getDescription());
////		assertEquals("Valentino", p.getBrand());
////		
////		Product pSolr = solrProductRepo.findOne(id);
////		
////		assertEquals(p.getId(), pSolr.getId());
////		assertEquals(p.getDescription(), pSolr.getDescription());
////		assertEquals(p.getShop(), pSolr.getShop());
////		assertEquals(p.getShopName(), pSolr.getShopName());
////		assertEquals(p.getImage(), pSolr.getImage());
////		assertEquals(p.getPrice(), pSolr.getPrice());
////		assertEquals(p.getShopName(), pSolr.getShopName());
////		assertEquals(p.getCreatedBy(), pSolr.getCreatedBy());
////		assertEquals(p.getModifiedBy(), pSolr.getModifiedBy());
////		assertEquals(p.getModifiedOn(), pSolr.getModifiedOn());
////		assertEquals(p.getCategories(), pSolr.getCategories());
////		assertEquals(p.getTenants(), pSolr.getTenants());				
////	}
////	
////	
////	@Test
////	public void delete() throws Exception {
////		Assert.assertEquals(0, repository.count());
////		Assert.assertEquals(0, solrProductRepo.count());
////		 
////		//authenticate with andrea.leo
////		authenticationService.authenticate("testusername");
////				
////		double[] l={0,0};
////		
////		List<String> categoriesWomen=new ArrayList<String>();
////		String catidw = categoryService.add("women", null);
////		categoriesWomen.add(catidw);
////		
////		String sid=shopService.add("City SRL", "City Moda Description" ,"Lecce", "Italia", "via XX1 Aprile", l);
////		
////		String id= productService.add(product1.getDescription(), product1.getBrand(), categoriesWomen, product1.getPrice(), sid);
////		
////				
////		productService.delete(id);
////		
////		Product p = productService.getById(id);
////		assertNull(p);
////		Product pSolr = solrProductRepo.findOne(id);
////		assertNull(pSolr);
////		Assert.assertEquals(0, solrProductRepo.count());
////	}
//	
//	
//
//}
