//package shoppino.notification;
//
//import java.io.IOException;
//import java.util.ArrayList;
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
//import shoppino.notification.dao.DeviceNotificationDAO;
//import shoppino.notification.domain.DeviceNotification;
//import shoppino.service.NotificationService;
//import shoppino.service.TenantService;
//
//public class ApnsPushNotificationManagerImpl implements PushNotificationManager {
//
//	private static String productIDKey = "productID";
//	
//	/* Decide how many threads you want your queue to use */ 
//    private static int threads = 30;
//
//    
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	private boolean production;
//	private String certificatePath;
//	private Resource certificatePathRes;
//	private String keyStorePwd; 
//	
//	private PushQueue queue;
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
//	public ApnsPushNotificationManagerImpl(String certificatePath, String keyStorePwd, boolean production) throws KeystoreException, IOException {
//		this.certificatePath = certificatePath;
//		certificatePathRes = new ClassPathResource(certificatePath);
////		Resource certificatePathAsResource = applicationContext.getResource(certificatePath);
//		this.keyStorePwd = keyStorePwd;
//		this.production = production;
//		
//		/* Create the queue */ 
//        queue = Push.queue(certificatePathRes.getFile(), keyStorePwd, production, threads);
//
//        /* Start the queue (all threads and connections and initiated) */ 
////        queue.start();
//
//	}
//	
//	@Override
//	@Deprecated
////	@Async
//	public void send(String from, String to, String message, String refId)
//		 {
////		System.out.println("send");
//		List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send");
//		}else if (deviceNotifications.size()==1) {
////			String token = "523831adb064c13a79cbc7ebf11e68db1138902713df336d30b43cff163a4255";
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
//				payload.addBadge(notificationService.hasNew(to));
//				
//				
//				List<PushedNotification> notifications = Push.payload(payload, certificatePathRes.getFile(), keyStorePwd, production, deviceNotification.getRegId());
//		
//				for(PushedNotification pn : notifications) {
//					logger.debug("Received Push Notification: " + pn);
//				}
//			
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
//				PushNotificationPayload payload = PushNotificationPayload.complex(); 	
//				try {
//					payload.addSound("default");
//				
//					payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
//					payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
//					payload.addCustomDictionary(productIDKey, refId);
//					payload.addBadge(notificationService.hasNew(to));
//					
//					  /* Add a notification for the queue to push */ 
//			        queue.add(payload, deviceNotification.getRegId());
////			        System.out.println("send ended");
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
//	@Deprecated
//	public void send(String from, String to, String message, String refId,
//			String senderId){
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//	public boolean isProduction() {
//		return production;
//	}
//
//
//	public String getCertificatePath() {
//		return certificatePath;
//	}
//
//
//	public String getKeyStorePwd() {
//		return keyStorePwd;
//	}
//
//	@Override
//	public void send(String from, String to, String message, String type,
//			String refId, String senderId) {
//		throw new UnsupportedOperationException("Method not implemented");
//	}
//
//	@Override
//	public void sendToAll(String from, String message, String type,
//			String refId, String senderId) {
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//	@Override
//	public void clean() {
//		throw new UnsupportedOperationException("Method not implemented");
//		
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
//
//}
