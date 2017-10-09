package shoppino.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

public interface ScriptService {

	public boolean isEnabled();
	
	public Map<String, Object> eval(String scriptName, Map parameters) throws FileNotFoundException, ScriptException, IOException ;
	
	public Map<String, Object> call(String functionName, Map parameters) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException;
	
}
