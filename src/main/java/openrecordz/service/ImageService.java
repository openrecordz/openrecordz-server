package openrecordz.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.core.io.Resource;

import openrecordz.domain.Image;




public interface ImageService {		
	
	public String save(String name, InputStream in) throws ParseException, ClientProtocolException, IOException;
	
	public Image saveAsImage(String name, InputStream in) throws ParseException, ClientProtocolException, IOException;
	
	
	
	public String save(String name, InputStream in, String path) throws ParseException, ClientProtocolException, IOException;
	
	public Image saveAsImage(String name, InputStream in, String path) throws ParseException, ClientProtocolException, IOException;
	

	
	public String save(String name, String url) throws ParseException, ClientProtocolException, IOException;

	public Image saveAsImage(String name, String url) throws ParseException, ClientProtocolException, IOException;
	
	
	
	public Resource getImageAsResource(String imageUrl)throws MalformedURLException;
	
	public Integer[] getImageSize(String url) throws Exception;
	
	public void delete(String url);
}
