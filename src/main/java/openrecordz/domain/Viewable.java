package openrecordz.domain;



public interface Viewable {

	
	public Integer getVisibility();
	
	
	
	
	public static Integer VISIBILITY_DELETED = -100;
//	public static Integer STATUS_REJECTED = -10;
	public static Integer VISIBILITY_DISABLED = -1;
	public static Integer VISIBILITY_SEARCH_ONLY = 10;
	public static Integer VISIBILITY_INSERT_ONLY = 20;
	public static Integer VISIBILITY_ALL = 30;
}
