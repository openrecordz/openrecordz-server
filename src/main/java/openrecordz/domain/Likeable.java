package openrecordz.domain;

import java.util.List;


public interface Likeable {

//	public static String LIKE_SEPARATOR = " ";
	
	public String getId();
	
	
//	public void setLikes(String likes);
	
	public List<String> getLikes();
	
	public void addLike(String username);
	
	public void removeLike(String username);
	
	public int getLikesCount();
	
	public String getCreatedBy();
}
