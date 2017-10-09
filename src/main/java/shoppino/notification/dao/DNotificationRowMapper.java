//package shoppino.notification.dao;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import org.springframework.jdbc.core.RowMapper;
//
//import shoppino.notification.domain.DeviceNotification;
//
//public final class DNotificationRowMapper implements RowMapper<DeviceNotification> {{
//
//}
//
//	@Override
//	public DeviceNotification mapRow(ResultSet rs, int arg1) throws SQLException {
//		DeviceNotification dn = new DeviceNotification();
//		dn.setId(rs.getLong("id"));
//		dn.setRegId(rs.getString("regid"));
//		dn.setUsername(rs.getString("username"));
//		dn.setType(rs.getString("dtype"));
//	    return dn;
//	}
//}
