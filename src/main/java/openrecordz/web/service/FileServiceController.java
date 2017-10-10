package openrecordz.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.ShoppinoException;
import openrecordz.security.exception.AuthorizationException;
import openrecordz.service.FileService;

@Controller
public class FileServiceController implements BaseServiceController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	FileService fileService; 

	
	
	 @RequestMapping(value = "/files", method = RequestMethod.POST)
	 public @ResponseBody Map handleFormUpload(
	    	@RequestParam(value = "name", required=false) String fixedName,
	    	@RequestParam(value = "path", required=false) String path,	    	
	        @RequestParam("file") MultipartFile file,
	        HttpServletRequest request, HttpServletResponse response) throws IOException, ResourceNotFoundException{

		 
		 logger.debug("name : " + fixedName);
		 logger.debug("path: " + path);
		 
		
		 
	        if (!file.isEmpty()) {
	        	
        		logger.debug("file size in bytes: " + file.getSize());        		
        		
        		String filePath =null;
        		        		
        		if (path!=null && !path.equals(""))
        			filePath = fileService.save(fixedName, file.getInputStream(), path);
        		else
        			filePath = fileService.save(file.getInputStream());
								
				logger.info("File saved into: " + filePath);
	            	
//				return filePath;
				File f = fileService.get(filePath);
			  	
		    	Map<String,Object> ret= new HashMap<String, Object>();
		    	
		    	if (request.getParameter("rfullpath")!=null)
		    		ret.put("path", f.getAbsoluteFile().toString());
		    	
		    	ret.put("name", f.getName());
		    	ret.put("modifiedOn", f.lastModified());
		    	
			  	return ret;
	       } else {
	    	   logger.error("File not present in the request.");
	          throw new RuntimeException("File not present in the request.");
	       }
	    }
	 
	 
	 
    
    
    @RequestMapping(value = "/files", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@RequestParam("path") String path,Model model, HttpServletRequest request) throws AuthorizationException, ShoppinoException {    	
	  
    	fileService.delete(path);
	  	
	  	logger.info("File " + path + "deleted.");
	  	
		return "{\"success\":true}";
    }
    
    
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public @ResponseBody Map get(@RequestParam("path") String path,Model model, HttpServletRequest request) throws AuthorizationException, ShoppinoException {    	
	  
    	logger.info("path: " + path );
    	
    	File f = fileService.get(path);
	  	
    	Map<String,Object> ret= new HashMap<String, Object>();
    	ret.put("name", f.getName());
    	ret.put("modifiedOn", f.lastModified());
	  	return ret;
    }
    
    
    @RequestMapping("/files/download")
    public ResponseEntity<byte[]> download(@RequestParam("path") String path, HttpServletRequest request) throws IOException, ResourceNotFoundException {
    	
    	logger.info("path: " + path );
    	
    	File f = fileService.get(path);
    	InputStream in = new FileInputStream(f);
    	
    	  final HttpHeaders headers = new HttpHeaders();
//          logger.debug("extension : " + FilenameUtils.getExtension(path));
////          logger.debug("mimetypes : " + MimeTypes.lookupMimeType(FilenameUtils.getExtension(path)));
//          MimeTypes.
    	  String contentType=FileTypeMap.getDefaultFileTypeMap().getContentType(f);
    	  logger.debug("getContentType : " +  contentType);
    	  
    	  
          headers.setContentType(MediaType.parseMediaType(contentType));
          headers.setContentDispositionFormData("attachment", path);

          return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
    }
    
    

}