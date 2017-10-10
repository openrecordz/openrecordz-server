package openrecordz.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import openrecordz.exception.ResourceNotFoundException;
import openrecordz.service.FileService;
import openrecordz.service.TenantService;

public class FileServiceImpl implements FileService {

	protected Log log = LogFactory.getLog(getClass());
	
	@Value("$platform{file.filesystem.path}")
	String baseFileDir;

	@Autowired
	TenantService tenantService;
	
	@Override
	public String save(String filename, InputStream in, String path)
			throws  IOException {
			log.debug("pathContentDir : " + path);
		   
			String baseDirWithTenant = baseFileDir+tenantService.getCurrentTenantName();
			log.debug("baseDirWithTenant : " + baseDirWithTenant);
			
		   File dir = new File(baseDirWithTenant, path);
		   boolean created = dir.mkdirs();
		   log.debug("folders : " + dir.getAbsolutePath() + " created : " + created);
		  
		   File file = new File(dir, filename);
		   
		   OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
			log.info("File has been created. path: " + file.getAbsolutePath());
			return file.getAbsolutePath().replace(baseDirWithTenant, "");
	}
	
	@Override
	public String save(InputStream in) throws IOException {
		   
			String baseDirWithTenant = baseFileDir+tenantService.getCurrentTenantName();
			log.debug("baseDirWithTenant : " + baseDirWithTenant);
			 
			File dir = new File(baseDirWithTenant);
			
			boolean created = dir.mkdirs();
			log.debug("folders : " + dir.getAbsolutePath() + " created : " + created);
			   
			 File ftmp=File.createTempFile("s21-tmp", ".csv", dir);			 			 
			 log.info("ftmp : " + ftmp);
			 
		  		   
		   
		   OutputStream out = new FileOutputStream(ftmp);
			byte buf[] = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
			log.info("File has been created. path: " + ftmp.getAbsolutePath());
			return ftmp.getAbsolutePath().replace(baseDirWithTenant, "");
	}
	
	
	
	@Override
	//must be resource spring
	public File get(String path) throws ResourceNotFoundException {
		String baseDirWithTenant = baseFileDir+tenantService.getCurrentTenantName();
		log.debug("baseDirWithTenant : " + baseDirWithTenant);
		
		 File file = new File(baseDirWithTenant, path);
		 if (file.exists()==false)
			 throw new ResourceNotFoundException("File doesn't exists for path : " + path);
		 
		 return file;
		
	}
	
	@Override
	public void delete(String path) {
		String baseDirWithTenant = baseFileDir+tenantService.getCurrentTenantName();
		log.debug("baseDirWithTenant : " + baseDirWithTenant);
		
		 File file = new File(baseDirWithTenant, path);
		 file.delete();
		
	}

	

	
	

	




}
