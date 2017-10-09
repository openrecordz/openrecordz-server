package microdev.util;

import java.util.Vector;

public class ListScrollElement {
	private Vector urls;
	private String currentPage;
	private String nextUrl;
	private String previousUrl;
	
	/**
	 * @param urls
	 * @param currentPage
	 */
	public ListScrollElement(Vector urls, String currentPage) {
		super();
		this.urls = urls;
		this.currentPage = currentPage;
	}

	public ListScrollElement() {
		urls = null;
		currentPage = null;
		nextUrl = null;
		previousUrl = null;
	}

	public String getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	
	public Vector getUrls() {
		return urls;
	}
	
	public void setUrls(Vector urls) {
		this.urls = urls;
	}
	
	
	public String getNextUrl() {
		return nextUrl;
	}
	
	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}
	
	public String getPreviousUrl() {
		return previousUrl;
	}
	
	public void setPreviousUrl(String previousUrl) {
		this.previousUrl = previousUrl;
	}
}