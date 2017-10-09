package microdev.util;

public class ListPageLinkElement {
	private String url;
	private String page;
	
	
	/**
	 * @param url
	 * @param page
	 */
	public ListPageLinkElement(String url, String page) {
		super();
		this.url = url;
		this.page = page;
	}
	
	public String getPage() {
		return page;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}