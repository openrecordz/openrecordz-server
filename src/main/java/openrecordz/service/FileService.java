package openrecordz.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import openrecordz.exception.ResourceNotFoundException;




public interface FileService {		
	
	public String save(String filename, InputStream in, String path) throws IOException;
	
	public String save(InputStream in) throws IOException;
	
	public String save(String filename,InputStream in) throws IOException;
	
	public File get(String path) throws ResourceNotFoundException;
	
	
	public void delete(String path);
}
