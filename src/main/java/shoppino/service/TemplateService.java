package shoppino.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TemplateService {

	public static String DEFAULT_TEMPLATE_VELOCITY_EXTENSION = ".vm";
	
	public String process(String templateName, Map model) throws FileNotFoundException, IOException ;
	
	public String process(String templateName, Map model, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException ;
	
}
