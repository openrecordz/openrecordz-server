package shoppino.domain;



public interface Statusable {

	
	public Integer getStatus();
	
	
	
	public static String STATUS_FIELD = "status";
	
	public static Integer STATUS_DELETED = -100;
	public static Integer STATUS_REJECTED = -10;
	public static Integer STATUS_DISABLED = -1;
	public static Integer STATUS_ENABLED = 1;
	public static Integer STATUS_VERSIONED = 5;
	public static Integer STATUS_VISIBLE = 10;
	public static Integer STATUS_VERIFIED = 20;
}
