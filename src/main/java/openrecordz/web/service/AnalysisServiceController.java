package openrecordz.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.exception.OpenRecordzException;
import openrecordz.security.service.AuthenticationService;


@Controller
public class AnalysisServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	 public static Connection connect = null;

    
    @Autowired
    AuthenticationService authenticationService;
    
  

    
    
    @RequestMapping(value = "/analytics", method = RequestMethod.GET)  
	 public @ResponseBody List<Map<String, String>> analytics(Model model, HttpServletRequest request) throws OpenRecordzException {
   	
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    	
    	String query=request.getParameter("q");    		     
      	
    	
    	logger.debug("query: " + query);
    	
    	
    	 try {
 			Class.forName("com.mysql.jdbc.Driver");
 		} catch (ClassNotFoundException e1) {
 			// TODO Auto-generated catch block
 			logger.error(e1);
 		}
 	      // Setup the connection with the DB
 	      try {
 	    	  
// 	    	connect = DriverManager
// 				      .getConnection("jdbc:mysql://localhost/shoppino?"
// 				          + "user=root&password=shoppino");
 	    	  
 			connect = DriverManager
 			      .getConnection("jdbc:mysql://localhost/shoppino?"
 			          + "user=root&password=root");
 		
 			
 			
 			
 			PreparedStatement preparedStatement = connect
 			          .prepareStatement(query);
 			
 			ResultSet rs = preparedStatement.executeQuery();
       
 		
 				

 			 
 			        ResultSetMetaData meta = rs.getMetaData();
 			        while (rs.next()) {
 			            Map map = new HashMap();
 			            for (int i = 1; i <= meta.getColumnCount(); i++) {
 			                String key = meta.getColumnName(i);
 			                String value = rs.getString(key);
 			                map.put(key, value);
 			            }
 			            list.add(map);
 			        }


 	     } catch (Exception e1) {
  			// TODO Auto-generated catch block
  			logger.error(e1);
  		}
       
       return list;
   }
    
    

    
}