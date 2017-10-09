//package shoppino.service;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.test.context.ContextConfiguration;
//
//import shoppino.domain.Product;
//import shoppino.exception.EmailAlreadyInUseException;
//import shoppino.exception.ShoppinoException;
//import shoppino.persistence.AbstractIntegrationTest;
//import shoppino.persistence.mongo.ProductRepository;
//import shoppino.search.solr.SolrProductRepository;
//import shoppino.security.exception.UsernameAlreadyInUseException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.security.service.UserService;
//
//
//@ContextConfiguration
//public class LoadDataTest extends AbstractIntegrationTest{
//	
//	@Autowired
//	ProductService productService;
//	
//	@Autowired
//	CategoryService categoryService;
//	
//	@Autowired
//	UserService userService;
//
//	@Autowired
//	PersonService personService;
//	
//	@Autowired
//	ShopService shopService;
//	
//	@Autowired
//	TenantService tenantService;
//
//	@Autowired
//	AuthenticationService authenticationService; 
//	
//	@Autowired
//	ApplicationContext applicationContext;
//	
//	@Autowired
//	ProductRepository repository;
//	
//	@Autowired
//	SolrProductRepository solrRepo;
//  
//	static final String IMG_FILE_TEST = "photo.jpg";
//	static final String IMG_FILE_TEST2 = "photo2.jpg";
//	static final String IMG_FILE_TEST3 = "photo3.jpg";
//	static final String IMG_FILE_TEST4 = "photo4.jpg";
//	static final String IMG_FILE_TEST5 = "photo5.jpg";
//	static final String IMG_FILE_TEST6 = "photo6.jpg";
//	static final String IMG_FILE_TEST7 = "photo7.jpg";
//	
////	@Autowired
////	ProductServiceIntegrationTest productServiceIntegrationTest;
////	public LoadData() {
////		
////	}
//
////	public static void main(String[] args) throws Exception {
////		LoadData ld = new LoadData();
////		ld.productServiceIntegrationTest.updateImage();
////	}
//
//	@Before
//	public void addUser() throws UsernameAlreadyInUseException, EmailAlreadyInUseException, ShoppinoException {
//		//		create user testusername if not exists
//		if (!userService.exists("andrea.leo2"))
//			userService.add("andrea.leo2", "andrea.leo2");		
//		
//		if (!personService.exists("andrea.leo2"))
//			personService.add("andrea.leo2", "Andrea Leo", "andrea.leo@domain.com");
//		
//		
//		if (!userService.exists("mirco.leo"))
//			userService.add("mirco.leo", "mirco.leo");	
//		
//		if (!personService.exists("mirco.leo"))
//			personService.add("mirco.leo", "Mirco Leo", "mirco.leo@domain.com");
//		
//		
//		if (!userService.exists("andrea.sponziello"))
//			userService.add("andrea.sponziello", "andrea.sponziello");	
//		
//		if (!personService.exists("andrea.sponziello"))
//			personService.add("andrea.sponziello", "Andrea Sponziello", "andrea.sponziello@domain.com");
//		
//		
//		
//	}
//	
//	
//	@Before
//	public void purgeRepository() {
//		repository.deleteAll();
//		solrRepo.deleteAll();
//		super.setUp();
//	}
//	
//	@Test
//	public void loaddata() throws Exception {
//
//		//authenticate with andrea.leo
//		authenticationService.authenticate("andrea.leo2");
//				
//		//women		
//		List<String> categoriesWomen=new ArrayList<String>();
//		String catidw = categoryService.add("women", null);
//		categoriesWomen.add(catidw);		
//
//		categoryService.add("clothing-women", catidw);
//		
//		List<String> categoriesShoesWomen=new ArrayList<String>();
//		String catSW=categoryService.add("shoes-women", catidw);
//		categoriesShoesWomen.add(catSW);
//		
//		categoryService.add("bags-women", catidw);
//		categoryService.add("accessories-women", catidw);
//		categoryService.add("jewellery-women", catidw);
//		
//		//men
//		List<String> categoriesMen=new ArrayList<String>();
//		String catidm = categoryService.add("men", null);
//		categoriesMen.add(catidm);
//			
//		categoryService.add("clothing-men", catidm);
//		categoryService.add("denim-men", catidm);
//		categoryService.add("shoes-men", catidm);			
//		categoryService.add("accessories-men", catidm);
//		
////				categoryService.add("underwear-women", catidw);
////				categoryService.add("underwear-men", catidm);
//		
//		///kids
//		String catidkids = categoryService.add("kids", null);
//		
//		
//				
//		
//		double[] l={40.372566,18.213272};
//		String sid=shopService.add("City Moda SRL", "City Moda Description" ,"Lecce", "Italia", "via XX1 Aprile", l);
//		
//		
//		
//		String id= productService.add(product1.getDescription(), product1.getBrand(), categoriesWomen, product1.getPrice(), sid);
//		
//		Resource is = applicationContext.getResource(IMG_FILE_TEST);
//		Product p = productService.updateImage(id,IMG_FILE_TEST, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//
//		
//		double[] l1={40.119655,18.298545};
//		sid=shopService.add("Candido 1859", "Candido 1859" ,"Maglie", "Italia", "Piazza Aldo Moro", l1);
//		
//		id= productService.add(product2.getDescription(), product2.getBrand(), categoriesMen, product2.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST2);
//		p = productService.updateImage(id,IMG_FILE_TEST2, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		double[] l2={40.120759,18.298695};
//		sid=shopService.add("Abbigliamento Giorgio Santese", "Abbigliamento Giorgio Santese" ,"Maglie", "Italia", "Via Trento e Trieste, 5", l2);
//		
//		
//		id= productService.add(product3.getDescription(), product3.getBrand(), categoriesMen, product3.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST3);
//		p = productService.updateImage(id,IMG_FILE_TEST3, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		double[] l3={40.173880,18.166494};
//		sid=shopService.add("De Matteis Stefania", "De Matteis Stefania" ,"Galatina", "Italia", "Corso Principe di Piemonte, 20 ", l3);
//		
//		id= productService.add(product4.getDescription(), product4.getBrand(), categoriesMen, product4.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST4);
//		p = productService.updateImage(id,IMG_FILE_TEST4, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		
//		double[] l4={40.173815,18.166623};
//		sid=shopService.add("Playlife Di Chiriatti Donatella", "Playlife Di Chiriatti Donatella" ,"Galatina", "Italia", "Corso Principe di Piemonte, 45", l4);
//		
//		id= productService.add(product5.getDescription(), product5.getBrand(), categoriesShoesWomen, product5.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST5);
//		p = productService.updateImage(id,IMG_FILE_TEST5, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		
//		
//		//authenticate with mirco.leo
//		authenticationService.authenticate("mirco.leo");
//		
//		double[] l5={40.174770,18.167503};
//		sid=shopService.add("Original Marines Yumarigi Sas Di Putignano Ada & C.", "Original Marines Yumarigi Sas Di Putignano Ada & C." ,"Galatina", "Italia", "Piazza Dante Alighieri, 76", l5);
//				
//		id= productService.add(product6.getDescription(), product6.getBrand(), categoriesWomen, product6.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST6);
//		p = productService.updateImage(id,IMG_FILE_TEST6, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		//authenticate with andrea.sponziello
//		authenticationService.authenticate("andrea.sponziello");
//		
//		double[] l6={40.356347,18.179626};
//		sid=shopService.add("Original Marines", "Original Marines" ,"Lecce", "Italia", "Via Trento, 76", l6);
//		
//		id= productService.add(product7.getDescription(), product7.getBrand(), categoriesWomen, product7.getPrice(), sid);
//		is = applicationContext.getResource(IMG_FILE_TEST7);
//		p = productService.updateImage(id,IMG_FILE_TEST7, is.getInputStream());
//		assertNotNull(p);
//		assertNotNull(p.getImage());
//		
//		
//		
////		double[] l7={40.352946,18.168941};
////		sid=shopService.add("Officina 81", "Officina 81" ,"Lecce", "Italia", "piazza mazzini", l7);
////		
////		id= productService.add(product7.getName(), product7.getDescription(), product7.getBrand(), null, product7.getPrice(), sid);
////		is = applicationContext.getResource(IMG_FILE_TEST7);
////		p = productService.updateImage(id,IMG_FILE_TEST7, is.getInputStream());
////		assertNotNull(p);
////		assertNotNull(p.getImage());
//	}
//
//}
