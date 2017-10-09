package urlhandler.url;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUrlDAOImpl implements UrlDAO {
	
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	private SimpleJdbcInsert insertActor;
	
	public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.insertActor = 
                new SimpleJdbcInsert(dataSource).withTableName("url");
	}

	 
	public void save(String url) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("uid", UUID.randomUUID());
		parameters.put("url", url);
        insertActor.execute(parameters);
        
	}

	
	
	public String getByUID(String uid) {
		String sql = "select url from url" + 
	            " where uid = ?";

		return this.simpleJdbcTemplate.queryForObject(sql, String.class, uid);
	}
	
	public String getByUrl(String url) {
		String sql = "select uid from url" + 
	            " where url = ? limit 1";
		//TODO
//		String sql = "select distinct uid from url" + 
//	            " where url = ? order by created On etc...";
		
		//old
//		String sql = "select uid from url" + 
//	            " where url = ?";

		return this.simpleJdbcTemplate.queryForObject(sql, String.class, url);
	}


	@Override
	public void deleteAll() {
		this.simpleJdbcTemplate.update(
		        "delete from url");
	}
}
