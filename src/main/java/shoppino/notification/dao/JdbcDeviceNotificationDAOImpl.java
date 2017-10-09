package shoppino.notification.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import shoppino.notification.domain.DeviceNotification;

@Repository
public class JdbcDeviceNotificationDAOImpl implements DeviceNotificationDAO {
	
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	private SimpleJdbcInsert insertActor;
	
	public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.insertActor = 
                new SimpleJdbcInsert(dataSource).withTableName("dnotification");
	}

	 
	public void insert(DeviceNotification deviceNotification) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("regid", deviceNotification.getRegId());
        parameters.put("username", deviceNotification.getUsername());
        parameters.put("dtype", deviceNotification.getType());
        parameters.put("tenant", deviceNotification.getTenant());
        insertActor.execute(parameters);
        
	}
	
	public List<DeviceNotification> findByRegIdAndUsernameAndTypeAndTenant(String regId, String username, String type, String tenant) {
		String sql = "select id, regid, username, dtype from dnotification" + 
	            " where regid = ? and username = ? and dtype = ? and tenant = ?";
//	    RowMapper<DeviceNotification> mapper = new RowMapper<DeviceNotification>() {  
//	        public DeviceNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
//	        	DeviceNotification dn = new DeviceNotification();
//	        	dn.setId(rs.getLong("id"));
//	        	dn.setRegId(rs.getString("regid"));
//	        	dn.setUsername(rs.getString("username"));
//	        	dn.setType(rs.getString("dtype"));
//	            return dn;
//	        }
//	    };

	    return this.simpleJdbcTemplate.query(sql, new DNotificationRowMapper(), regId, username, type, tenant);
//	    return this.simpleJdbcTemplate.query(sql, mapper, regId, username, type);
	}
	
	
	public List<DeviceNotification> findByRegIdAndTypeAndTenant(String regId, String type, String tenant) {
		String sql = "select id, regid, username, dtype, tenant from dnotification" + 
	            " where regid = ? and dtype = ? and tenant = ?";

		return this.simpleJdbcTemplate.query(sql, new DNotificationRowMapper(), regId, type, tenant);
	}

	public List<DeviceNotification> findByUsernameAndTenant(String username, String tenant) {
		String sql = "select id, regid, username, dtype,tenant from dnotification" + 
	            " where username = ? and tenant = ?";

		return this.simpleJdbcTemplate.query(sql, new DNotificationRowMapper(), username, tenant);
	}

	public List<DeviceNotification> findByUsernameAndTypeAndTenant(String username, String type, String tenant) {
		String sql = "select id, regid, username, dtype, tenant from dnotification" + 
	            " where username = ? and dtype = ? and tenant = ?";

		return this.simpleJdbcTemplate.query(sql, new DNotificationRowMapper(), username, type, tenant);
	}
	
	public List<DeviceNotification> findByTypeAndTenant(String type, String tenant) {
		String sql = "select id, regid, username, dtype, tenant from dnotification" + 
	            " where dtype = ? and tenant = ?";

		return this.simpleJdbcTemplate.query(sql, new DNotificationRowMapper(), type, tenant);
	}
	

	@Override
	public void deleteAll() {
		this.simpleJdbcTemplate.update(
		        "delete from dnotification");
		
	}


	@Override
	public void delete(String regId, String type) {
		this.simpleJdbcTemplate.update(
		        "delete from dnotification where regid = ? and dtype = ?", regId, type);
		
	}


	@Override
	public void update(String oldRegId, String newRegId, String type, String tenant) {
		DeviceNotification dn = this.findByRegIdAndTypeAndTenant(oldRegId, type, tenant).get(0);
		dn.setRegId(newRegId);
		update(dn);
	}


	@Override
	public void update(DeviceNotification deviceNotification) {
		this.simpleJdbcTemplate.update(
		        "update dnotification set regid=?, dtype=?, username=?  where id=?", deviceNotification.getRegId(), 
		        deviceNotification.getType(), deviceNotification.getUsername(), deviceNotification.getId());
	}
	
}
