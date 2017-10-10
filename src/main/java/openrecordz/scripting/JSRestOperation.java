package openrecordz.scripting;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

public class JSRestOperation {

	@Autowired
	RestOperations restOperation;
	
	protected final Log logger = LogFactory.getLog(getClass());

//	public String httpRequest(String url, String method, Map<String,String> parameters, Map<String,String> headers) {
//															is NativeObject but this implememnts Map so it's work
	
	public String httpRequest(String url, String method, Map<Object,Object> parameters, Map<Object,Object> headers) {
		return this.httpRequest(url, method, parameters, headers,null);
	}
	public String httpRequest(String url, String method, Map<Object,Object> parameters, Map<Object,Object> headers,  Map<String,?> urlParameters) {
		logger.debug("url : " + url);
		logger.debug("method : " + method);
		logger.debug("parameters : " + parameters);
		logger.debug("headers : " + headers);
		logger.debug("urlParameters : " + urlParameters);
		
		
		HttpHeaders httpHeaders = null;

		if (headers!=null && headers.size()>0) {
			httpHeaders = new HttpHeaders();
			//		NativeArray arr = (NativeArray) result;
			for (Map.Entry entry : headers.entrySet()) {
				httpHeaders.add(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	    	
		MultiValueMap<String, String> body = null;     

		if (parameters!=null && parameters.size()>0) {
			body = new LinkedMultiValueMap<String, String>();
			for (Map.Entry entry : parameters.entrySet()) {
				String parType= entry.getValue().getClass().getSimpleName();
				
	//			if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
	//				 Log.error("Non supportata");
	//			 }else if (entry.getValue() instanceof sun.org.mozilla.javascript.internal.NativeArray) {
	//				 
	//				 Map<String, String> arrayPars = (Map<String, String>)entry.getValue();     
	//					for (Map.Entry pa : arrayPars.entrySet()) {
	//						 body.add(entry.getKey().toString(), pa.getValue().toString());
	//					}
	//					
	//			 } else {
					 body.add(entry.getKey().toString(), entry.getValue().toString());
	//			 }
				
				
			}
		}
	
//		HttpEntity<?> request = new HttpEntity<Object>(parameters, httpHeaders);
    	HttpEntity<?> request = new HttpEntity<Object>(body, httpHeaders);
//    	HttpEntity<String> request = new HttpEntity<String>(body, httpHeaders);
    	
//    	Map a = new HashMap();
//    	a.put("1", "aaa");
//    	a.put("2", "bbb");
//    	
//    	URI expanded = new UriTemplate(url).expand(a);
    	
    	ResponseEntity<String> response = null;
    	
    	if (urlParameters!=null)
    		response=restOperation.exchange(url, HttpMethod.valueOf(method), request, String.class,(Map)urlParameters);
    	else 
    		response=restOperation.exchange(url, HttpMethod.valueOf(method), request, String.class);
    	
    	logger.debug("response : " + response);
    	
    	return response.getBody();
	}
	
	
	
	
	public String get(String url) {
		logger.debug("url : " + url);
		
		
    	
    	ResponseEntity<String> response = restOperation.getForEntity(url, String.class);
    	
    	logger.debug("response : " + response);
    	
    	return response.getBody();
	}
	
}
