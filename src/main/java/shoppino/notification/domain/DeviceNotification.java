package shoppino.notification.domain;

public class DeviceNotification {

	public static final String ANDROID_TYPE = "android";
	public static final String IOS_TYPE = "ios";
	
	private long id;
	private String regId;
	private String username;
	private String type;
	private String tenant;
	
	public DeviceNotification() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	
}
