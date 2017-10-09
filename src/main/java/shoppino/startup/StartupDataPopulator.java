package shoppino.startup;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import shoppino.service.ImageService;

public class StartupDataPopulator {

	@Autowired
	ImageService imageService;
	
	@Value("$shoppino{default.avatar.path}")
	String defaultPhotoPath;
	@Value("$shoppino{startup.startupdatapopulator.enabled}")
	boolean enabled;
	
	@Autowired
	ApplicationContext applicationContext;
	
	protected final Log logger = LogFactory.getLog(getClass());
	static final String DEFAUTL_AVATAR= "classpath:/default/avatar/avatar.png";
	
	public void init() {
		if (enabled) {
			logger.info("Loading Startup Data Populator");
			try {
				loadDefaultPersonAvatar();
			}catch (Exception e) {
				logger.error("Failed loading Startup Data Populator.", e);
			}
		}else
			logger.info("Startup Data Populator disabled.");
	}
	
	public void loadDefaultPersonAvatar() throws ParseException, ClientProtocolException, IOException {
		Resource is = applicationContext.getResource(DEFAUTL_AVATAR);
		imageService.save("avatar.png", is.getInputStream(), defaultPhotoPath);
	}
}
