package it.f21.accessLog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.sql.DataSource;

import microdev.db.DBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;


/**
 * @author Andrea
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccessLogManager {
//	private Connection conn = null;
	
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	private SimpleJdbcInsert insertActor;
	
	
	private Log log = LogFactory.getLog(AccessLogManager.class);
	
	public AccessLogManager() {
	}
	
	public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.insertActor = 
                new SimpleJdbcInsert(dataSource).withTableName("request_logs");
	}
	
	public void serialize(AccessLog al) throws Exception {
		try {
			insert(al);
		} catch(Exception e) {
			log.error("",e);
		}
	}

	public List<AccessLog> last() {
		
		String sql = "select id, remote_host, remote_addr, request_uri, query_string, request_url, username, user_agent, user_lang, created_on " +
				"from request_logs" + 
	            " order by id desc limit 500;";

		return this.simpleJdbcTemplate.query(sql, new AccessLogRowMapper());
	}
	
	private void insert(AccessLog al) throws Exception {
		Long id = null;
//		ResultSet rs = null;
//		Statement stmt = null;
		try {
//			conn = ConnectionUtil.currentConnection();

			// formatto la data UTC
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String timestamp = sdf.format(al.getDate());
			
			String query = "insert into request_logs " +
					"(remote_host, " +
					"remote_addr, " +
					"request_uri, " +
					"query_string, " +
					"request_url, " +
					"username, " +
//					"user_id, " +
//					"organization_id, " +
					"user_agent, " +
					"user_lang," +
					"tenant)" +
					" values ('" + DBUtil.addslashes(al.getRemote_host()) + "', " +
					"'" + DBUtil.addslashes(al.getRemote_addr()) + "', " +
					"'" + DBUtil.addslashes(al.getRequest_uri()) + "', " +
					"'" + DBUtil.addslashes(al.getQuery_string()) + "', " +
					"'" + DBUtil.addslashes(al.getRequest_url()) + "', " +
					"'" + DBUtil.addslashes(al.getUsername()) + "', " +
//					al.getUser_id() + ", " +
//					al.getOrganization_id() + ", " +
					"'" + DBUtil.addslashes(al.getUser_agent()) + "', " +
					"'" + DBUtil.addslashes(al.getUser_lang()) + "'," +
					"'" + al.getTenant()+ "')";
			
			this.simpleJdbcTemplate.getJdbcOperations().execute(query);
			//log.debug("query = " + query);
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//													ResultSet.CONCUR_READ_ONLY);
//			stmt.execute(query);
		} catch (Exception e) {
			log.error("",e);
		} 
//	} catch (SQLException e) {
//		log.error("",e);
//	}
	}
}