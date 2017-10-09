package shoppino.security.web.authentication;

public class UsernameUtils {

	public static String getRealUsername(String username) {
		//to lowercase
		if (username !=null)
			return username.toLowerCase();
		else 
			return null;
	}
}
