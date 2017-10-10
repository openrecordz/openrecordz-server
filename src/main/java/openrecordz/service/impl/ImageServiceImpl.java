package openrecordz.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import microdev.util.HttpUtil;
import openrecordz.domain.Image;
import openrecordz.service.ImageService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class ImageServiceImpl implements ImageService {

	protected Log log = LogFactory.getLog(getClass());
	
	private String imagerepoUrl;
	private String addUrl;
	private String getSizeUrl;
	private String searchUrl;
	private String deleteUrl;
	
	public String save(String name, InputStream in) throws ParseException, ClientProtocolException, IOException {
		return save(name, in, null);
	}
	
	@Override
	public Image saveAsImage(String name, InputStream in)
			throws ParseException, ClientProtocolException, IOException {
		return saveAsImage(name, in, null);
	}
	
	@Override
	public Image saveAsImage(String name, InputStream in, String path)
			throws ParseException, ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
//				HttpVersion.HTTP_1_1.toString());

		log.debug("add image url : " + imagerepoUrl + addUrl);
		HttpPost        post   = new HttpPost( imagerepoUrl + addUrl );
//		HttpPost        post   = new HttpPost( "http://localhost:8080/imagerepo/service/images/add.json" );
		MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

		//http://stackoverflow.com/questions/7650690/transfer-a-file-between-android-devices
		InputStreamBody isb = new InputStreamBody(in, "image/jpeg", name);

		// add key value pair. The key "imageFile" is arbitrary
		entity.addPart("file", isb);
		entity.addPart("name", 	new StringBody(name, "text/plain", Charset.forName( "UTF-8" )));
		if (path!=null)
			entity.addPart("path", 	new StringBody(path, "text/plain", Charset.forName( "UTF-8" )));
		
//		// For File parameters
//		entity.addPart( "file", new FileBody((( File ) paramValue ), "application/zip" ));
//		 
//		// For usual String parameters
//		entity.addPart( paramName, new StringBody( paramValue.toString(), "text/plain",
//		                                           Charset.forName( "UTF-8" )));
		 
		post.setEntity( entity );
		 try{
		// Here we go!
		String response = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );
		log.debug("response : "+ response);
		JSONObject json=(JSONObject)JSONValue.parse(response);
		
		String url = json.get("url").toString();
		JSONArray sizeJson = (JSONArray)json.get("size");
		Integer[] size = new Integer[2];
		size[0] = Integer.parseInt(sizeJson.get(0).toString());
		size[1] = Integer.parseInt(sizeJson.get(1).toString());
		
		Image image = new Image(url);
		image.setSize(size);
		
		return image;
//			return response;
		 }catch (Exception e) {
			// TODO: handle exception
			 log.error("error saving image with name : " + name);
			 return null;
		} finally {
			client.getConnectionManager().shutdown();	
		}
	}
	
	public String save(String name, InputStream in, String path) throws ParseException, ClientProtocolException, IOException {
		return saveAsImage(name, in, path).getUrl();						
	}
	

	@Override
	public Integer[] getImageSize(String url) throws Exception {
		log.debug("get size image url : " + imagerepoUrl + getSizeUrl + url);
		
		String response = HttpUtil.urlGETAsString(imagerepoUrl + getSizeUrl + url);
		log.debug("response : "+ response);
		JSONArray json=(JSONArray)JSONValue.parse(response);
		Integer[] size = new Integer[2];
		size[0] = Integer.parseInt(json.get(0).toString());
		size[1] = Integer.parseInt(json.get(1).toString());
		return size;
	}




	@Override
	public String save(String name, String url) throws ParseException,
			ClientProtocolException, IOException {
		// TODO Auto-generated method stub
//		return null;
//		fai post semplice senza multipart
//		HttpUtil
		UrlResource urlResource = new UrlResource(url);
		return save(name, urlResource.getInputStream());
	}
	
	
	@Override
	public Image saveAsImage(String name, String url) throws ParseException,
			ClientProtocolException, IOException {
		UrlResource urlResource = new UrlResource(url);
		return saveAsImage(name, urlResource.getInputStream());
	}

	@Override
	public Resource getImageAsResource(String imageUrl) throws MalformedURLException {
		UrlResource urlResource = new UrlResource(this.imagerepoUrl+this.searchUrl+imageUrl);
		return urlResource;
	}

	@Override
	public void delete(String url) {
		log.debug("deleting image with url : " + imagerepoUrl + deleteUrl + url);
		
		String response = HttpUtil.urlGETAsString(imagerepoUrl + deleteUrl + url);
		
	}



	
	public void setImagerepoUrl(String imagerepoUrl) {
		this.imagerepoUrl = imagerepoUrl;
	}


	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}

	public void setGetSizeUrl(String getSizeUrl) {
		this.getSizeUrl = getSizeUrl;
	}
	
	

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	
	

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	


}
