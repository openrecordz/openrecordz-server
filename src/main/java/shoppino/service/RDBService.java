package shoppino.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RDBService {

	public List<Map<String, String>> select(String jdbcConnectionString, String query) throws SQLException;
	
	public int update(String jdbcConnectionString, String sql) throws SQLException;
	
	public  Integer  insert(String jdbcConnectionString, String sql, String[] columnNameToReturn) throws SQLException;
}
