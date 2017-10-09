package urlhandler.url;

public interface UrlDAO {

	public void save(String url);
	
	public String getByUID(String uid);
	
	public String getByUrl(String url);
	
	public void deleteAll();
}
