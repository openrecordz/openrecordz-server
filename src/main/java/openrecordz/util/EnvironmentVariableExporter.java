package openrecordz.util;

public class EnvironmentVariableExporter {

//	@Autowired Environment env;
	
	public String myEnv;
	
//	public EnvironmentVariableExporter() {
//		myEnv = env.getProperty("my.env");
//	}

	public String getMyEnv() {
		return myEnv;
	}

	public void setMyEnv(String myEnv) {
		this.myEnv = myEnv;
	}
	
	
}
