package shoppino.scheduler.script;

import org.springframework.beans.factory.annotation.Autowired;

import shoppino.service.ScriptService;

public class RunMeTask {
	@Autowired
	ScriptService scriptService;
	
	private static String BEFORE_PRODUCT_UPDATE_FUNCTION_NAME = "onBeforeProductUpdate";
	
	public void run() {
		System.out.println("Spring 3 + Quartz 1.8.6 ~");
//		scriptService.call(functionName, parameters);
	}
}