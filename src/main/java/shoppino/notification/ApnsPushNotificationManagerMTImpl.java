//package shoppino.notification;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javapns.Push;
//import javapns.communication.exceptions.CommunicationException;
//import javapns.communication.exceptions.KeystoreException;
//import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
//import javapns.notification.PushNotificationPayload;
//import javapns.notification.PushedNotification;
//import javapns.notification.transmission.PushQueue;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import shoppino.domain.Notification;
//import shoppino.notification.dao.DeviceNotificationDAO;
//import shoppino.notification.domain.DeviceNotification;
//import shoppino.service.NotificationService;
//import shoppino.service.TenantService;
//
//public class ApnsPushNotificationManagerMTImpl implements PushNotificationManager {
//
//	@Deprecated
//	private static String productIDKey = "productID";
//	
//	private static String C_URI_KEY= "cURI";
//	private static String NOTIFICATION_TYPE_KEY = "t";
//	
//	
//	/* Decide how many threads you want your queue to use */ 
//    private static int threads = 30;
//
//    
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	private boolean production;
//	private Map<String, String> certificatePaths;
//	private Map<String, Resource> certificatePathsRes;
//	private Map<String, String> keyStorePwds; 
//	
//	private Map<String,PushQueue> queues;
//	
//	@Autowired
//	DeviceNotificationDAO deviceNotificationDAO;
//	
//	@Autowired
//	NotificationService notificationService;
//	
//	@Autowired
//	TenantService tenantService;
//	
//	@Deprecated
//	public ApnsPushNotificationManagerMTImpl(Map<String, String> certificatePaths, Map<String, String> keyStorePwds, boolean production) throws KeystoreException, IOException {
//		this.queues = new HashMap<String, PushQueue>();
//		this.production = production;
//		this.certificatePaths = certificatePaths;
//		this.certificatePathsRes = new HashMap<String, Resource>();
//		this.keyStorePwds = keyStorePwds;
//		
//		PushQueue queue;
//		
//		int i = 0;
//		for (Map.Entry<String, String> certificatePath : certificatePaths.entrySet()) {
//			Resource r = new ClassPathResource(certificatePath.getValue());
//			logger.debug("Certificate "+ certificatePath.getValue() + " exists " +r.getFile().exists());
//			certificatePathsRes.put(certificatePath.getKey(), r);
//			/* Create the queue */ 
//	        queue = Push.queue(r.getFile(), keyStorePwds.get(certificatePath.getKey()), production, threads);
//	        queues.put(certificatePath.getKey(), queue);
//	        i++;
//		}
//		
//
//        /* Start the queue (all threads and connections and initiated) */ 
////        queue.start();
//
//	}
//	
//	@Override
////	@Async
//	public void send(String from, String to, String message, String refId,String senderId)
//		 {
//		List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send to " + to);
//		}else if (deviceNotifications.size()==1) {
//			DeviceNotification deviceNotification = deviceNotifications.get(0);
//			List locArgs = new ArrayList();
//			locArgs.add(new String(from));
//			
//	
//			PushNotificationPayload payload = PushNotificationPayload.complex(); 
//			/* Customize the payload */ 
//			// payload.addAlert(message); // messaggio da tradurre server-side
//			try {
//				payload.addSound("default");
//			
//				payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
//				payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
//				payload.addCustomDictionary(productIDKey, refId);
//				payload.addCustomDictionary("t", Notification.LIKE_MESSAGE);
//				payload.addBadge(notificationService.hasNew(to));
//				
//				logger.debug("Sending message to one device. RegId :" + deviceNotification.getRegId());
//
//				List<PushedNotification> notifications = Push.payload(payload, certificatePathsRes.get(senderId).getFile(), keyStorePwds.get(senderId), production, deviceNotification.getRegId());				
//				for(PushedNotification pn : notifications) {
//					logger.debug("Received ApnsPush Notification: " + pn);
//				}
//			
////			} catch (JSONException e) {
////				 logger.error("Error posting messages", e);
//			} catch (CommunicationException e) {
//				logger.error("Error posting messages", e);
//			} catch (KeystoreException e) {
//				logger.error("Error posting messages", e);
//			} catch (IOException e) {
//				logger.error("Error posting messages", e);
//			} catch (javapns.json.JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} else {
//			List locArgs = new ArrayList();
//			locArgs.add(new String(from));
//			
//			for (DeviceNotification deviceNotification : deviceNotifications) {
//				  logger.debug("Sending message to device: " + deviceNotification.getRegId());
//				PushNotificationPayload payload = PushNotificationPayload.complex(); 	
//				try {
//					payload.addSound("default");
//				
//					payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
//					payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
//					payload.addCustomDictionary(productIDKey, refId);
//					payload.addCustomDictionary("t", Notification.LIKE_MESSAGE);
//					payload.addBadge(notificationService.hasNew(to));
//					
//					  /* Add a notification for the queue to push */ 
////					logger.debug("queues.get(senderId) : " + queues.get(senderId)); 
//			        queues.get(senderId).add(payload, deviceNotification.getRegId());
////				} catch (JSONException e) {
////					 logger.error("Error posting messages", e);
//				} catch (InvalidDeviceTokenFormatException e) {
//					 logger.error("Error posting messages", e);
//				} catch (javapns.json.JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	
//		
//	}
//	
//	
//	
//	
//	
//	
//	 public void sendToAll(String from, String message, String type, String refId, String senderId) {
//		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByTypeAndTenant(DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		 send(from, deviceNotifications, message, type, refId, senderId);
//	 }
//	 public void send(String from, String to, String message, String type, String refId, String senderId) {
//		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		 send(from, deviceNotifications, message, type, refId, senderId);
//	 }
//	
//	
////	@Async
//	public void send(String from, List<DeviceNotification> deviceNotifications, String message, String type, String refId,String senderId)
//		 {
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send.. ");
//		}else if (deviceNotifications.size()==1) {
//			DeviceNotification deviceNotification = deviceNotifications.get(0);
//			List locArgs = new ArrayList();
//			locArgs.add(new String(from));
//			
//	
//			PushNotificationPayload payload = PushNotificationPayload.complex(); 
//			/* Customize the payload */ 
//			// payload.addAlert(message); // messaggio da tradurre server-side
//			try {
//				payload.addSound("default");
//			
//				
////				payload.addCustomAlertLocKey(type); // messaggio tradotto client-side
//////				payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
////				payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
//				payload.addCustomDictionary(C_URI_KEY, refId);
//				payload.addCustomDictionary(NOTIFICATION_TYPE_KEY, type);
//				payload.addAlert(message);
//				
////				passare type con "t"-> nuovo "uri" questi hanno campo cURI, msg invece che messaggio
//				
////				payload.addBadge(notificationService.hasNew(to));
//				
//				logger.debug("Sending message to one device. RegId :" + deviceNotification.getRegId());
//
//				List<PushedNotification> notifications = Push.payload(payload, certificatePathsRes.get(senderId).getFile(), keyStorePwds.get(senderId), production, deviceNotification.getRegId());				
//				for(PushedNotification pn : notifications) {
//					logger.debug("Received ApnsPush Notification: " + pn);
//				}
//			
////			} catch (JSONException e) {
////				 logger.error("Error posting messages", e);
//			} catch (CommunicationException e) {
//				logger.error("Error posting messages", e);
//			} catch (KeystoreException e) {
//				logger.error("Error posting messages", e);
//			} catch (IOException e) {
//				logger.error("Error posting messages", e);
//			} catch (javapns.json.JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} else {
//			List locArgs = new ArrayList();
//			locArgs.add(new String(from));
//			
//			for (DeviceNotification deviceNotification : deviceNotifications) {
//				  logger.debug("Sending message to device: " + deviceNotification.getRegId());
//				PushNotificationPayload payload = PushNotificationPayload.complex(); 	
//				try {
//					payload.addSound("default");
//				
////					payload.addCustomAlertLocKey(type); // messaggio tradotto client-side
//////					payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
////					
////					payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
//					payload.addCustomDictionary(C_URI_KEY, refId);
//					payload.addCustomDictionary(NOTIFICATION_TYPE_KEY, type);
//					payload.addAlert(message);
////					payload.addBadge(notificationService.hasNew(to));
//					
//					  /* Add a notification for the queue to push */ 
////					logger.debug("queues.get(senderId) : " + queues.get(senderId)); 
//			        queues.get(senderId).add(payload, deviceNotification.getRegId());
////				} catch (JSONException e) {
////					 logger.error("Error posting messages", e);
//				} catch (InvalidDeviceTokenFormatException e) {
//					 logger.error("Error posting messages", e);
//				} catch (javapns.json.JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	
//		
//	}
//
//	@Override
//	public void send(String from, String to, String message, String refId
//			){
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//	public boolean isProduction() {
//		return production;
//	}
//
//	@Override
//	public void clean() {
//		throw new UnsupportedOperationException("Method not implemented");	
//	}
//
//	@Override
//	public void sendAsJson(String from, String to, String message,
//			Map<String, String> data, Integer appleBadge, String type,
//			String senderId) {
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//
//
//
//}
