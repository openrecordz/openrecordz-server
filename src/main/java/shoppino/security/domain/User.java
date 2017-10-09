package shoppino.security.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

	
public class User {

	@NotNull
    @Max(64)
	private String username;
	
	@NotEmpty(message = "Password must not be blank.")
    @Size(min = 1, max = 10, message = "Password must between 1 to 10 Characters.")
	private String password;
	private boolean enabled;
	
	public User() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
