//package shoppino.runtime.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import shoppino.exception.ResourceNotFoundException;
//import shoppino.exception.ShoppinoRuntimeException;
//import shoppino.security.service.AuthenticationService;
//import shoppino.service.PersonService;
//import shoppino.service.TenantService;
//
//import com.firebase.security.token.TokenGenerator;
//
//
//
//public class FirebaseServiceImpl  implements FirebaseService {
//
//	protected Log log = LogFactory.getLog(getClass());
//	
//	@Value("$shoppino{runtime.firebase.secret}")
//	private String secret;
////	= "Zc0AjbRERqJTBStM2kqWuGUhPJDt56qgCL44d3Xi";
//	
//	@Autowired
//	private PersonService personService;
//
//	@Autowired
//	private AuthenticationService authenticationService;
//	
//	@Autowired
//	private TenantService tenantService;
//	
//	@Override
//	public String generateToken(Map<String, Object> payload) {
//		TokenGenerator tokenGenerator = new TokenGenerator(secret);
//		Map<String, Object> internalPayload = new HashMap<String, Object>();
//		
//		try {
//			internalPayload.putAll(payload);
//			
//			internalPayload.put("uid", authenticationService.getCurrentLoggedUsername());
//			internalPayload.put("username", authenticationService.getCurrentLoggedUsername());
//		
//			internalPayload.put("email", personService.getByUsername(authenticationService.getCurrentLoggedUsername()).getEmail());
//			
//			internalPayload.put("tenant", tenantService.getCurrentTenantName());
//			
//		
//		
//		log.debug("internalPayload : " + internalPayload);
//		
//		String token = tokenGenerator.createToken(internalPayload);
//		
//		log.debug("Firebase secret : " + token);
//	
//		return token;
//		
//		} catch (ResourceNotFoundException rnfe) {
//			throw new ShoppinoRuntimeException("Error creating payload for firebase service",rnfe);
//		}
//		catch (Exception e) {
//			throw new ShoppinoRuntimeException("Error generating token for firebase service",e);
//		}
//	
//	}
//	
//	
//	
//	
//}
