package shoppino.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import microdev.util.HttpUtil;
import openrecordz.domain.Person;
import openrecordz.exception.ShoppinoException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.MultiValueMap;

import shoppino.service.PersonService;
import shoppino.service.SocialService;


public class SocialServiceImpl implements SocialService{


	@Autowired
	PersonService personService;
	
	//social
	ConnectionFactoryLocator connectionFactoryLocator;
	ConnectionRepository connectionRepository;
		
	@Value("$shoppino{person.avatar.name}")
	String personPhotoName;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Inject
	public SocialServiceImpl(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.connectionRepository = connectionRepository;
//		super(connectionFactoryLocator, connectionRepository);
	}
	
	

	
//	public void updatePhotoFromSocialPrimaryConnection(String username) throws ShoppinoException {
//		
//		Connection connection = connectionRepository.findPrimaryConnection(Facebook.class);
//		if (connection!=null && connection.getImageUrl()!=null && !connection.getImageUrl().equals("")) {
//			logger.debug("Downloading image profile : " + connection.getImageUrl() + "?type=large");
//			InputStream imageIS = HttpUtil.urlGETAsStream(connection.getImageUrl() + "?type=large");
//			Person p = personService.updateLogo(username, personPhotoName, imageIS);
//			logger.debug("Updated profile image for user " + username + " with image : "+ p.getPhoto() );
//		
//		}
//	}
	
	
	public void updatePhotoFromSocial(String username) throws ShoppinoException {
		
//		Find all connections the current user has across all providers. The returned map contains an entry for each provider the user is connected to. The key for each entry is the providerId, and the value is the list of Connections that exist between the user and that provider. For example, if the user is connected once to Facebook and twice to Twitter, the returned map would contain two entries with the following structure:
//
//		 { 
//		     "facebook" -> Connection("Keith Donald") ,
//		     "twitter"  -> Connection("kdonald"), Connection("springsource")
//		 }
//		 
//		The returned map is sorted by providerId and entry values are ordered by rank. Returns an empty map if the user has no connections. 
		
		
		MultiValueMap<String, Connection<?>> connections = connectionRepository.findAllConnections();
		for (String key : connections.keySet()) {
			List<Connection<?>> subConnections=connections.get(key);	
			if (subConnections!=null && subConnections.size()>0 && subConnections.get(0)!=null && subConnections.get(0).getImageUrl()!=null && !subConnections.get(0).getImageUrl().equals("")) {
//				select only first connection for a provider		
				Connection connection = subConnections.get(0);
				logger.debug("Downloading image profile : " + connection.getImageUrl() + "?type=large");
				InputStream imageIS = HttpUtil.urlGETAsStream(connection.getImageUrl() + "?type=large");
				Person p = personService.updateLogo(username, personPhotoName, imageIS);
				logger.debug("Updated profile image for user " + username + " with image : "+ p.getPhoto() );
				//exit loop
				break;
			
			}
		}
		
	}


	public ConnectionFactoryLocator getConnectionFactoryLocator() {
		return connectionFactoryLocator;
	}


	public void setConnectionFactoryLocator(
			ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}


	public ConnectionRepository getConnectionRepository() {
		return connectionRepository;
	}


	public void setConnectionRepository(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}
	
	
	
}
