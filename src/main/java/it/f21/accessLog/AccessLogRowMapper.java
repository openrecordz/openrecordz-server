package it.f21.accessLog;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public final class AccessLogRowMapper implements RowMapper<AccessLog> {{

}

	@Override
	public AccessLog mapRow(ResultSet rs, int arg1) throws SQLException {
		AccessLog al = new AccessLog();
		al.setRemote_host(rs.getString("remote_host"));
		al.setRemote_addr(rs.getString("remote_addr"));		
		al.setRequest_uri(rs.getString("request_uri"));
		al.setQuery_string(rs.getString("query_string"));
		al.setRequest_url(rs.getString("request_url"));
		al.setUser_lang(rs.getString("user_lang"));
		al.setUser_agent(rs.getString("user_agent"));
		al.setUsername(rs.getString("username"));
		al.setDate(rs.getTimestamp("created_on"));
			
	    return al;
	}
}
