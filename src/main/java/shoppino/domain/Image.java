package shoppino.domain;

public class Image {

	private String url;
	private Integer[] size;
	
	public Image(String url) {
		this.url=url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer[] getSize() {
		return size;
	}

	public void setSize(Integer[] size) {
		this.size = size;
	}
	
	
}
