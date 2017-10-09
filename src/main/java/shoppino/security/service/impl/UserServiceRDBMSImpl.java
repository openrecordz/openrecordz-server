package shoppino.security.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;

import shoppino.security.domain.User;
import shoppino.security.exception.UserNotExistsException;
import shoppino.security.exception.UsernameAlreadyInUseException;
import shoppino.security.service.UserService;

public class UserServiceRDBMSImpl implements UserService {

	protected final Log logger = LogFactory.getLog(getClass());
	
//	@Autowired
	JdbcTemplate template;
	
//	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	UserDetailsManager userDetailsService;
	
//	public UserServiceRDBMSImpl(DataSource dataSource) {
//		this.dataSource = dataSource;
//		template = new JdbcTemplate(dataSource);
//	}
		
//	@Override
//	public User getByUsername(String username) {
//		// TODO Auto-generated method stub
//		UserDetails ud= userDetailsService.loadUserByUsername(username);
//		User u = new User();
//		u.setUsername(ud.getUsername());
//		u.setPassword(ud.getPassword());
//		u.setEnabled(ud.isEnabled());
//		return u;
//	}

	
	public User getByUsername(String username) throws UserNotExistsException {
		try {
			User u = template.queryForObject("select username, password from users where username = ?",
					new RowMapper<User>() {
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							User u = new User();
							u.setUsername(rs.getString("username"));
							u.setPassword(rs.getString("password"));
							u.setEnabled(true);
	//						return new User(rs.getString("username"), null, rs.getString("firstName"), rs
	//								.getString("lastName"));
							return u;
						}
					}, username);
			
			if (u==null)
				throw new UserNotExistsException(username);
			
			return u;
		}catch (DataAccessException daex) {
			throw new UserNotExistsException(username, daex);
		}
	}
	


	public void add(String username, String password) throws UsernameAlreadyInUseException {
//		PasswordEncoder encoder = new Md5PasswordEncoder();
//	    String hashedPass = encoder.encodePassword(password, null);
//	    logger.debug("md5 password : "+ hashedPass);
		String hashedPass = password;
	    
	    try {
			template.execute("INSERT INTO users VALUES('"+username+"','"+hashedPass+"',TRUE);");
			template.execute("INSERT INTO authorities VALUES('"+username+"','ROLE_USER');");
			template.execute("INSERT INTO authorities VALUES('"+username+"','ROLE_RESTUSER');");
			
		} catch (DuplicateKeyException e) {
			throw new UsernameAlreadyInUseException(username);
		}
		
	    logger.info("user created with username : " + username);
		
		//user creation
//				GrantedAuthority ga = new GrantedAuthorityImpl("ROLE_USER");
//				Collection<GrantedAuthority> gaColl = new ArrayList<GrantedAuthority>();
//				gaColl.add(ga);
//				org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
//						username, password, true, true, true, true, gaColl);
//				userDetailsManager.createUser(user);
	}
	
	
	public void update(String username, String newPassword) throws UserNotExistsException {
//		PasswordEncoder encoder = new Md5PasswordEncoder();
//	    String hashedPass = encoder.encodePassword(password, null);
//	    logger.debug("md5 password : "+ hashedPass);
		String hashedPass = newPassword;
	    
		if (exists(username)==false)
			throw new UserNotExistsException(username);
		
		template.execute("UPDATE users SET password='"+hashedPass+"' WHERE username='"+username+"';");			
			
		
	    logger.info("user updated with username : " + username);
	}
	
	public void delete(String username) throws UserNotExistsException {	    
		if (exists(username)==false)
			throw new UserNotExistsException(username);
		
		template.execute("DELETE FROM authorities WHERE username='"+username+"';");
		template.execute("DELETE FROM users WHERE username='"+username+"';");			
			
		
	    logger.info("user deleted with username : " + username);
	}
	
	
	@Override
	public boolean exists(String username) {
		try {
			User u = getByUsername(username);
			if (u!=null)
				return true;
			else
				return false;
		}catch (UserNotExistsException e) {
			return false;
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void addRole(String username, String role) throws UserNotExistsException {
		
		if (!exists(username)) 
			throw new UserNotExistsException(username);
		
	 template.execute("INSERT INTO authorities VALUES('"+username+"','"+role+"');");
	 logger.info("authority "+ role +" created for username : " + username);
	}
	
	
	public void deleteRoles(String username) throws UserNotExistsException {	    
		if (exists(username)==false)
			throw new UserNotExistsException(username);
		
		template.execute("DELETE FROM authorities WHERE username='"+username+"';");
				
			
		
	    logger.info("deleted all authorities for username : " + username);
	}
	
	
//	public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
//		this.userDetailsManager = userDetailsManager;
//	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.template = new JdbcTemplate(dataSource);
	}



	
}
