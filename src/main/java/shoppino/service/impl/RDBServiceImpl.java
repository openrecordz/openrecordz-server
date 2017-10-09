package shoppino.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import openrecordz.util.DbUtil;
import shoppino.service.RDBService;

public class RDBServiceImpl implements RDBService {
	protected final Log logger = LogFactory.getLog(getClass());

	private Connection connect(String jdbcConnectionString) throws SQLException {
		 Connection connect = null;
		 try {
	 			Class.forName("com.mysql.jdbc.Driver");
	 		} catch (ClassNotFoundException e1) {
	 			// TODO Auto-generated catch block
	 			logger.error(e1);
	 		}
	 	      // Setup the connection with the DB
	 	      try {
	 	    	  
//	 	    	connect = DriverManager
//	 				      .getConnection("jdbc:mysql://localhost/shoppino?"
//	 				          + "user=root&password=shoppino");
	 	    	  
	 	    	 connect = DriverManager
	 			      .getConnection(jdbcConnectionString);
//	 			    		  "jdbc:mysql://localhost/shoppino?"
//	 			          + "user=root&password=root");
	 	    	 
	 	    	 logger.debug("JDBC Connected to : " + jdbcConnectionString);
	 	    	 
	 	    	 return connect;
	 	      }catch (SQLException sqe) {
	 	    	 DbUtil.close(connect);
				logger.error("slq error",sqe);
				throw sqe;
	 	      }
//	 	      finally {
//	 	    	
//	 	      }
	 	    	 
	 	    	
	}
	
	public List<Map<String, String>> select(String jdbcConnectionString, String query) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    	
    	logger.debug("query: " + query);
    	
    	Connection connect = this.connect(jdbcConnectionString);
    	
    	  try {	
 			preparedStatement = connect
 			          .prepareStatement(query);
 			
 			ResultSet rs = preparedStatement.executeQuery();
       
 			        ResultSetMetaData meta = rs.getMetaData();
 			        while (rs.next()) {
 			            Map map = new HashMap();
 			            for (int i = 1; i <= meta.getColumnCount(); i++) {
// 			                String key = meta.getColumnName(i);
 			            	String key = meta.getColumnLabel(i);
 			                String value = rs.getString(key);
 			                map.put(key, value);
 			            }
 			            list.add(map);
 			        }


 			        logger.debug("ResultSet : " + rs);
 	   
 			        return list;
    	  }catch (SQLException sqe) {
				logger.error("slq error",sqe);
				throw sqe;
	 	  }finally {
	 		 DbUtil.close(connect);
	 	   	 DbUtil.close(preparedStatement);
	 	  }
	}
	
	public  Integer  insert(String jdbcConnectionString, String sql, String[] columnNameToReturn) throws SQLException {
		PreparedStatement preparedStatement = null;
		
    	logger.debug("sql: " + sql);
    	
    	Connection connect = this.connect(jdbcConnectionString);
    	
    	try {	
 			
			preparedStatement = connect
			          .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
//			int rowsUpdated = preparedStatement.executeUpdate(sql);
//			String[] ret = new String[1];
//			ret[0]="idtt2";
			preparedStatement.executeUpdate(sql,columnNameToReturn);
	              
			
			ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                return last_inserted_id;
//                return this.select(jdbcConnectionString, "select * from "+rs.getMetaData().getTableName(1)+" where " +rs.getMetaData().getColumnName(1) + "='"+last_inserted_id+"';");
            }
            
//	 	   logger.debug("rowsUpdated : " + rowsUpdated);
	 	   
	       return null;
       
	  }catch (SQLException sqe) {
			logger.error("slq error",sqe);
			throw sqe;
 	  }finally {
 		 DbUtil.close(connect);
 	   	 DbUtil.close(preparedStatement);
 	  }
}

	
public int update(String jdbcConnectionString, String sql) throws SQLException {
		PreparedStatement preparedStatement = null;
		
    	logger.debug("sql: " + sql);
    	
    	Connection connect = this.connect(jdbcConnectionString);
    	
    	try {	
 			
			preparedStatement = connect
			          .prepareStatement(sql);
			
			int rowsUpdated = preparedStatement.executeUpdate(sql);
	              
	 	   logger.debug("rowsUpdated : " + rowsUpdated);
	 	   
	       return rowsUpdated;
       
	  }catch (SQLException sqe) {
			logger.error("slq error",sqe);
			throw sqe;
 	  }finally {
 		 DbUtil.close(connect);
 	   	 DbUtil.close(preparedStatement);
 	  }
	}
}
