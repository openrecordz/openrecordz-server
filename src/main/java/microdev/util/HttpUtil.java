package microdev.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;


public class HttpUtil {

	private static Log log = LogFactory.getLog(HttpUtil.class);
	
	@Value("$platform{http_client.retry_attempts}")
	private static int max_attempts;
	
	@Value("$platform{http_client.timeout}")
	private static int http_clientTimeout;

//	public static String urlGETAsString(String url) {
//		return HttpUtil.urlGETAsString(url, 3);
//	}
	
	/**
	 * TODO passare un eventuale oggetto ProxyPrams per impostare un proxy
	 * utilizzando il codice commentato.
	 * TODO implementare il numero di tentativi, -1 = fino a risultato ottenuto
	 * @param url
	 * @return
	 */
	public static String urlGETAsString(String url) {

//		log.debug("HttpUtil Downloading: " + url);
		String responseBody = null;
		
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		
		// proxy
		//		client.getHostConfiguration().setProxy("fw-02.links.it", 8080);
		//		client.getState().setProxyCredentials(
		//				new AuthScope("fw-02.links.it", 8080), 
		//				new NTCredentials("sponzielloa", "Links01", "fw-02.links.it", "links.it"));
		// fine proxy

		// Create a method instance.
		GetMethod method = new GetMethod(url);
		
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
//				new DefaultHttpMethodRetryHandler(ApplicationSettings.getInstance().getPropertyAsInt("http_client.retry_attempts"), false));
//		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return null;
			}
			
			// Read the response body.
			responseBody = method.getResponseBodyAsString();
//			HttpMethodParams params = method.getParams();
			// as byte
			// responseBody = method.getResponseBody();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (Exception e) {
			log.error("===================================");
			log.error("", e); //.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return responseBody;
	}

	public static InputStream urlGETAsStream(URL url) {
		return urlGETAsStream(url.toString());
	}

	public static InputStream urlGETAsStream(String url) {

		InputStream responseBody = null;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// proxy
		//		client.getHostConfiguration().setProxy("fw-02.links.it", 8080);
		//		client.getState().setProxyCredentials(
		//				new AuthScope("fw-02.links.it", 8080), 
		//				new NTCredentials("sponzielloa", "Links01", "fw-02.links.it", "links.it"));
		// fine proxy

		// Create a method instance.
		GetMethod method = new GetMethod(url);
		
		// Provide custom retry handler is necessary
		// tries 3 times
//		ApplicationSettings settings = ApplicationSettings.getInstance();
//		int max_attempts = settings.getPropertyAsInt("http_client.retry_attempts");
		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(max_attempts, false));
//		ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));
		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, http_clientTimeout);
		
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return null;
			}
			
			// Read the response body.
			// responseBody = method.getResponseBodyAsString();
			// as stream
			responseBody = method.getResponseBodyAsStream();
			// as byte
			// responseBody = method.getResponseBody();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} finally {
			// Release the connection.
			//			method.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * TODO passare un eventuale oggetto ProxyPrams per impostare un proxy
	 * utilizzando il codice commentato.
	 * @param url
	 * @return
	 */
	public static InputStream urlPost(String url, Map<String, String> params) {

		InputStream responseBody = null;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// proxy
		//		client.getHostConfiguration().setProxy("fw-02.links.it", 8080);
		//		client.getState().setProxyCredentials(
		//				new AuthScope("fw-02.links.it", 8080), 
		//				new NTCredentials("sponzielloa", "Links01", "fw-02.links.it", "links.it"));
		// fine proxy

		// Create a method instance.
		PostMethod method = new PostMethod(url);

		// Provide custom retry handler is necessary
		// tries 3 times
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(ApplicationSettings.getInstance().getPropertyAsInt("http_client.retry_attempts"), false));
		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));

		if (params != null) {
			for( String key : params.keySet()) {
				method.addParameter(key, params.get(key));
			}
		}

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return null;
			}

			// Read the response body.
			// responseBody = method.getResponseBodyAsString();
			// as byte
			// responseBody = method.getResponseBody();
			// as stream
			responseBody = method.getResponseBodyAsStream();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} finally {
			// Release the connection.
			//			method.releaseConnection();
		}
		//		GlobbiUtil.printStream(responseBody);
		return responseBody;
	}

	/**
	 * TODO passare un eventuale oggetto ProxyPrams per impostare un proxy
	 * utilizzando il codice commentato.
	 * @param url
	 * @return
	 */
	public static String urlPOSTAsString(String url, Map<String, String> params) {

		String responseBody = null;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// proxy
		//		client.getHostConfiguration().setProxy("fw-02.links.it", 8080);
		//		client.getState().setProxyCredentials(
		//				new AuthScope("fw-02.links.it", 8080), 
		//				new NTCredentials("sponzielloa", "Links01", "fw-02.links.it", "links.it"));
		// fine proxy

		// Create a method instance.
		PostMethod method = new PostMethod(url);

		// Provide custom retry handler is necessary
		// tries 3 times
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(ApplicationSettings.getInstance().getPropertyAsInt("http_client.retry_attempts"), false));
		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));

		if (params != null) {
			for( String key : params.keySet()) {
				method.addParameter(key, params.get(key));
			}
		}

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return null;
			}

			// Read the response body.
			responseBody = method.getResponseBodyAsString();
			// as byte
			// responseBody = method.getResponseBody();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return responseBody;
	}

	public static String urlPOSTAsString(String url) {
		return urlPOSTAsString(url, null);
	}

	public static boolean urlFound(String url) {

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// Create a method instance.
		HeadMethod method = new HeadMethod(url);
		
		method.setFollowRedirects(false);
		// Provide custom retry handler is necessary
		// tries 3 times
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(ApplicationSettings.getInstance().getPropertyAsInt("http_client.retry_attempts"), false));
		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return false;
			}

			method.getResponseBodyAsString();
			System.out.println("status line: " + method.getStatusLine());
			System.out.println("status code: " + method.getStatusCode());
			// as byte
			// responseBody = method.getResponseBody();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error("", e); //.printStackTrace();
		} finally {
			// Release the connection.
			//			method.releaseConnection();
		}
		return true;
	}

	/**
	 * Ex.
	 * 
	 * ByteArrayPartSource mem_file = new ByteArrayPartSource("content.txt", "### some text as binary###".getBytes());
		Part[] parts = {
				new StringPart("nome", "Andrea"),
				new StringPart("cognome", "Sponziello"),
				new FilePart("content.txt", mem_file)	
		};
		HttpUtil_old.postMultipart("http://localhost:8080/testapp/postdata", parts);
	 * @param post_url
	 * @param parts
	 */
	public static void postMultipart(String post_url, Part[] parts) {

		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "QBR Feeder");

		PostMethod method = new PostMethod(post_url);

		//		ByteArrayPartSource mem_file = new ByteArrayPartSource("content.txt", "parecchia robba di bytes...".getBytes());
		//		Part[] parts = {
		//				new StringPart("nome", "Andrea"),
		//				new StringPart("cognome", "Sponziello"),
		//				new FilePart(mem_file.getFileName(), mem_file)
		//		};
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

		BufferedReader br = null;
		try {
			int returnCode = client.executeMethod(method);

			if(returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err.println("The Post method is not implemented by this URI");
				// still consume the response body
				method.getResponseBodyAsString();
			} else {
				br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
				String readLine;
				while(((readLine = br.readLine()) != null)) {
					System.err.println(readLine);
				}
			}
		} catch (Exception e) {
			log.error("", e); //.printStackTrace();
		} finally {
			method.releaseConnection();
			if(br != null) try { br.close(); } catch (Exception fe) {}
		}
	}
	
	/**
	 * TODO passare un eventuale oggetto ProxyPrams per impostare un proxy
	 * utilizzando il codice commentato.
	 * TODO implementare il numero di tentativi, -1 = fino a risultato ottenuto
	 * @param url
	 * @return
	 */
	public static String urlGETAsString(String url, Map<String, String> params) {

//		log.debug("HttpUtil Downloading: " + url);
		String responseBody = null;
		
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		
		// proxy
		//		client.getHostConfiguration().setProxy("fw-02.links.it", 8080);
		//		client.getState().setProxyCredentials(
		//				new AuthScope("fw-02.links.it", 8080), 
		//				new NTCredentials("sponzielloa", "Links01", "fw-02.links.it", "links.it"));
		// fine proxy
		
		// Create a method instance.
		GetMethod method = new GetMethod(url);
		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(ApplicationSettings.getInstance().getPropertyAsInt("http_client.retry_attempts"), false));
		method.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, ApplicationSettings.getInstance().getPropertyAsInt("http_client.timeout"));
		
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> lparams = new ArrayList<NameValuePair>();
			for( String key : params.keySet()) {
				lparams.add(new NameValuePair(key, params.get(key)));
			}
			NameValuePair[] pairs = lparams.toArray(new NameValuePair[lparams.size()]);
			method.setQueryString(pairs);
		}
		
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				return null;
			}
			
			Header[] headers = method.getResponseHeaders();
			String charset = getCharset(headers);
//			System.out.println("charset: " + charset);
			
			// Read the response body.
			responseBody = method.getResponseBodyAsString();
//			System.out.println("body: " + responseBody);
//			HttpMethodParams params = method.getParams();
			// as byte
			// responseBody = method.getResponseBody();
			// vedi qui per gli altri metodi:
			// classe base HttpMethodBase
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HttpMethodBase.html
			// estesa GetMethod
			// http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/methods/GetMethod.html

		} catch (Exception e) {
			log.error("===================================");
			log.error("", e); //.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return responseBody;
	}
	
	private static String getCharset(Header[] headers) {
		String charset = null;
		for (Header h : headers) {
			String name = h.getName();
			String value = h.getValue();
//			System.out.println("name: " + name + ", value: " + value);
			if(name.toLowerCase().equals("content-type")) {
//				System.out.println("FOUND: " + value);
				String[] parts = value.split(";");
				// test String[] parts = {"Charset =utf-8 ", "charse", "text/html"};
				for(String p : parts) {
					if (p.trim().toLowerCase().startsWith("charset")) {
						String[] charset_parts = p.split("=");
						charset = charset_parts[charset_parts.length - 1].trim().toUpperCase();
						break;
					}
				}
				break;
			}
		}
		return charset;
	}
}
