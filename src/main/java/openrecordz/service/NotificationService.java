package openrecordz.service;

import java.util.List;
import java.util.Map;

import openrecordz.domain.Notification;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.ShoppinoException;
import openrecordz.exception.TooManyNotificationException;

public interface NotificationService {

	public static String ALL_DEVICES = "all";
	
	public void register(String registrationId, String type);
	
	//remove the association with username
	public void unregister(String registrationId, String type);
	
	public void unregisterDevice(String registrationId, String type);
	
	public void updateRegistration(String oldRegId, String newRegId, String type);

	public List<Notification> findAllPaginated(int page, int size);
	
	@Deprecated
	public String sendLikeNotification(String message, String to, String refId);
	
	
	/**
	 * message : testo
	 * to: username o "all" per tutti
	 * type:
	 * “like_msg”: Notifica di un Like su un determinato prodotto con apertura del dettaglio del prodotto.
	 * 	alert”: Notifica ALERT con apertura di una popup.
	 * “uri”: Notifica di tipo URI (apertura di una webview al link indicato oppure apertura del dettaglio del prodotto indicato
	 * 
        	String strUri = extras.getString(FUtils.NOTIFICATION_URI);
        	if(strUri.startsWith("http://")){
        		//Apertura di una webview all'url indicato.
        		showUrlNotification(strUri);
        	}else if(strUri.startsWith("deal://")){
        		//Apertura del dettaglio del prodotto.
        		//Recupero Id dell'oggetto della notifica.
            	String itemId = strUri.replace("deal://", "");
            	viewItemDetails(itemId);
        	}
    	}
	}
	 * @throws TooManyNotificationException 
	 */
	@Deprecated
	public String send(String message, String type, String to, String cURI, Boolean forceSend) throws TooManyNotificationException;
	
//	@Deprecated // use:  send(String message, String type, String to, String refId);
//	public String send(String from, String message, String type, String to, String refId);
	
//	@Deprecated
//	public String send(String from, String message, String type, String refId);
//	


	
	//per chat type=chat
	public String send(String to, String message, Map<String, String> data, Integer appleBadge, String type, Boolean historyVisible);
	
	
	public Notification view(String id) throws ResourceNotFoundException;
	
	public int hasNew(String username);
	
	public List<Notification> findByTo(String to);
	
	public List<Notification> findByType(String type,int page, int size);
	
	long countByType(String type);
	
//	public List<Notification> findByFromAndToAndRefId(String from, String to, String refId);
	
	public void delete(String id) throws ShoppinoException;


}
