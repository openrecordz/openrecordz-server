//package shoppino.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.List;
//import java.util.Locale;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Category;
//import shoppino.exception.ResourceNotFoundException;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.CategoryRepository;
//
///**
// * 
// * @author Andrea Leo
// */
//@ContextConfiguration
//public class CategoryServiceIntegrationTest extends AbstractIntegrationTest {
//
//
//	@Autowired
//	CategoryRepository repository;
//	
//	@Autowired
//	CategoryService categoryService;
//	
//	
//	@Autowired
//	TenantService tenantService;
//	
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		super.setUp();
//	}
//
//	
//	@Test
//	public void add() throws Exception {
////		repository.save(shops);
//		String id = categoryService.add("accessori",  null);
//		assertNotNull(id);
//		assertEquals("/accessori", id);
//		
//		id = categoryService.add("accessori donna","/accessori");
//		assertNotNull(id);
//		assertEquals("/accessori/accessori donna", id);
//		
//	
//	}
//	
//	@Test
//	public void getById() throws ResourceNotFoundException {
//		String id = categoryService.add("accessori",  null);
//		assertNotNull(id);
//		
//		Category c = categoryService.getById("/accessori");
//		assertNotNull(id);
//		assertEquals("accessori", c.getName());
//		assertEquals(null, c.getParent());
//		assertEquals("/accessori", c.getId());
//		assertEquals("/accessori", c.getPath());
//		
//		id = categoryService.add("accessori donna",  "/accessori");
//		assertNotNull(id);
//		assertEquals("/accessori/accessori donna", id);
//		
//		
//		c = categoryService.getById("/accessori/accessori donna");
//		assertNotNull(id);
//		assertEquals("accessori donna", c.getName());
//		assertEquals("/accessori", c.getParent());
//		assertEquals("/accessori/accessori donna", c.getId());
//		assertEquals("/accessori/accessori donna", c.getPath());
//	}
//	
//	@Test
//	public void getByIdLocalized() throws ResourceNotFoundException {
//		String id = categoryService.add("men",  null);
//		Category c = categoryService.getById(id,Locale.getDefault().toString());
//		assertNotNull(id);
//		assertEquals("Men", c.getLabel());
//		
//		c = categoryService.getById(id,Locale.ITALY.toString());
//		assertNotNull(id);
//		assertEquals("Uomo", c.getLabel());
//		
//		id = categoryService.add("men-shoes",  id);
//		c = categoryService.getById(id,Locale.getDefault().toString());
//		assertNotNull(id);
//		assertEquals("Men Shoes", c.getLabel());
//		
//		c = categoryService.getById(id,Locale.ITALY.toString());
//		assertNotNull(id);
//		assertEquals("Scarpe Uomo", c.getLabel());
//	}
//	
//	
//	@Test
//	public void findAll() throws Exception {
//		add();
//		add();
//		List<Category> cs =categoryService.findAll();
//		assertEquals(2, cs.size());
//	}
//	
//	@Test
//	public void findAllLocalized() throws Exception {
//		String id = categoryService.add("men",  null);
//		id = categoryService.add("women",  null);
//		List<Category> cs =categoryService.findAll(Locale.getDefault().toString());
//		assertEquals(2, cs.size());
//		assertEquals("Men", cs.get(1).getLabel());
//		assertEquals("Women", cs.get(0).getLabel());
//		
//		cs =categoryService.findAll(Locale.ITALY.toString());
//		assertEquals(2, cs.size());
//		assertEquals("Uomo", cs.get(1).getLabel());
//		assertEquals("Donna", cs.get(0).getLabel());
//	}
//	
//
//}
