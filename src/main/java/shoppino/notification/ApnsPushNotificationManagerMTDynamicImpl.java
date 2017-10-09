//package shoppino.notification;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javapns.Push;
//import javapns.communication.exceptions.KeystoreException;
//import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
//import javapns.notification.PushNotificationPayload;
//import javapns.notification.transmission.PushQueue;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//
//import shoppino.domain.Notification;
//import shoppino.exception.ShoppinoRuntimeException;
//import shoppino.notification.dao.DeviceNotificationDAO;
//import shoppino.notification.domain.DeviceNotification;
//import shoppino.service.NotificationService;
//import shoppino.service.TenantService;
//
//public class ApnsPushNotificationManagerMTDynamicImpl implements PushNotificationManager {
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
//	public static String DEFAULT_CERTIFICATE_EXTENSION = ".p12";
//	
//	public static String APNS_APPPLE_SANDBOX_PREFIX = "sandbox";
//	public static String APNS_APPPLE_PRODUCTION_PREFIX = "production";
//	
////	private boolean production;
////	private Map<String, String> certificatePaths;
////	private Map<String, Resource> certificatePathsRes;
////	private Map<String, String> keyStorePwds; 
//	
//	private Map<String,PushQueue> queues; //<tenantName, PushQueue>
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
//	@Value("$shoppino-notification{notification.ios.apns.certificate.filesystem.path}")
//	public String fileSystemApnsCertificatePath;
//	
//	@Value("$shoppino-notification{notification.ios.apns.certificate.filesystem.keystorepwd}")
//	public String fileSystemApnsCertificateKeyStorePassword;
//			
//	
//	
////	@Autowired
////	EnvironmentService environmentService;
//	
////	@Deprecated
////	public ApnsPushNotificationManagerMTDynamicImpl(Map<String, String> certificatePaths, Map<String, String> keyStorePwds, boolean production) throws KeystoreException, IOException {
////		this.queues = new HashMap<String, PushQueue>();
////		this.production = production;
//////		this.certificatePaths = certificatePaths;
////		this.certificatePathsRes = new HashMap<String, Resource>();
//////		this.keyStorePwds = keyStorePwds;
////		
////		PushQueue queue;
////		
////		int i = 0;
////		for (Map.Entry<String, String> certificatePath : certificatePaths.entrySet()) {
////			Resource r = new ClassPathResource(fileSystemApnsCertificatePath);
////			logger.debug("Certificate "+ certificatePath.getValue() + " exists " +r.getFile().exists());
////			certificatePathsRes.put(certificatePath.getKey(), r);
////			/* Create the queue */ 
////	        queue = Push.queue(r.getFile(), keyStorePwds.get(certificatePath.getKey()), production, threads);
////	        queues.put(certificatePath.getKey(), queue);
////	        i++;
////		}
////		
////
////        /* Start the queue (all threads and connections and initiated) */ 
//////        queue.start();
////
////	}
//	
//	
//	public ApnsPushNotificationManagerMTDynamicImpl() {
//		this.queues = new HashMap<String, PushQueue>();
//	}
//	
//	public void createQueue() {
//		String tenant = tenantService.getCurrentTenantName();
//		if (queues.containsKey(tenant)) {
//			logger.debug("APNS Push Notification Queue already created for tenant :" + tenant);
//		}else {
//			try {
//				String pathFileSystemCertificates = fileSystemApnsCertificatePath + File.separator + tenantService.getCurrentTenantName();
//				logger.debug("pathFileSystemCertificates : "  + pathFileSystemCertificates);
//				
//				String pathFileSystemSandoBoxCertificate = pathFileSystemCertificates + "-" +  APNS_APPPLE_SANDBOX_PREFIX + DEFAULT_CERTIFICATE_EXTENSION;
//				logger.debug("pathFileSystemSandoBoxCertificate : "  + pathFileSystemSandoBoxCertificate);
//				
//				String pathFileSystemProductionCertificate = pathFileSystemCertificates + "-" +  APNS_APPPLE_PRODUCTION_PREFIX + DEFAULT_CERTIFICATE_EXTENSION;
//				logger.debug("pathFileSystemProductionCertificate : "  + pathFileSystemProductionCertificate);
//				
//				Resource fileSystemSandBoxCertificate = new FileSystemResource(pathFileSystemSandoBoxCertificate);
//				Resource fileSystemProductionCertificate = new FileSystemResource(pathFileSystemProductionCertificate);
//				Resource fileSystemCertificate = null;
//				
//				boolean apnsProduction = true;
//				if (fileSystemSandBoxCertificate.exists()) {
//					apnsProduction = false;
//					fileSystemCertificate = fileSystemSandBoxCertificate;
//					logger.debug("fileSystemSandBoxCertificate found here: "  + fileSystemSandBoxCertificate.getFile().getAbsolutePath());
//				}
//				
//				if (fileSystemProductionCertificate.exists()) {
//					apnsProduction = true;
//					fileSystemCertificate = fileSystemProductionCertificate;
//					logger.debug("fileSystemProductionCertificate found here: "  + fileSystemProductionCertificate.getFile().getAbsolutePath());
//				}
//				
//				if (fileSystemCertificate==null) {
//					logger.error("APNS Certificate with names : " + pathFileSystemSandoBoxCertificate +" , " + pathFileSystemProductionCertificate +" not found");
//					throw new ShoppinoRuntimeException("APNS Certificate with names : " + pathFileSystemSandoBoxCertificate +" , " + pathFileSystemProductionCertificate +" not found");
//				}
//					
//				
//					logger.debug("Creating APNS queue for : " + tenant +" and apns production : " + apnsProduction);
//					PushQueue queue = Push.queue(fileSystemCertificate.getFile(), fileSystemApnsCertificateKeyStorePassword, apnsProduction, threads);
//					queues.put(tenant, queue);
//					
//////					used by Push.payload... deprecating 
////					certificatePathsRes.put(tenant, fileSystemCertificate);
////					keyStorePwds.put(tenant, fileSystemApnsCertificateKeyStorePassword);
//					
//					logger.info("Apns  queue created for : " + tenant + " and apns production : " + apnsProduction);
//				
//				
//			} catch (KeystoreException ke) {
//				logger.error("APNS Certificate KeystoreException",ke);
//				throw new ShoppinoRuntimeException("APNS Certificate KeystoreException",ke);
//			} catch (IOException ioe) {
//				logger.error("APNS Certificate IOException",ioe);
//				throw new ShoppinoRuntimeException("APNS Certificate IOException",ioe);
//			}
//		}
//	}
//	
//	@Override
////	@Async
//	@Deprecated
//	public void send(String from, String to, String message, String refId,String senderId)
//		 {
//		
//		createQueue();
//		
//		
//		List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send to " + to);
//		}
//		
////		deprecated beacause doesn't use a queue and so isn't dynamic
//		
////		else if (deviceNotifications.size()==1) {
////			DeviceNotification deviceNotification = deviceNotifications.get(0);
////			List locArgs = new ArrayList();
////			locArgs.add(new String(from));
////			
////	
////			PushNotificationPayload payload = PushNotificationPayload.complex(); 
////			/* Customize the payload */ 
////			// payload.addAlert(message); // messaggio da tradurre server-side
////			try {
////				payload.addSound("default");
////			
////				payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
////				payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
////				payload.addCustomDictionary(productIDKey, refId);
////				payload.addCustomDictionary("t", Notification.LIKE_MESSAGE);
////				payload.addBadge(notificationService.hasNew(to));
////				
////				logger.debug("Sending message to one device. RegId :" + deviceNotification.getRegId());
////
////				List<PushedNotification> notifications = Push.payload(payload, certificatePathsRes.get(senderId).getFile(), keyStorePwds.get(senderId), production, deviceNotification.getRegId());				
////				for(PushedNotification pn : notifications) {
////					logger.debug("Received ApnsPush Notification: " + pn);
////				}
////			
////			} catch (JSONException e) {
////				 logger.error("Error posting messages", e);
////			} catch (CommunicationException e) {
////				logger.error("Error posting messages", e);
////			} catch (KeystoreException e) {
////				logger.error("Error posting messages", e);
////			} catch (IOException e) {
////				logger.error("Error posting messages", e);
////			}
//			
////		}
//	else {
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
//			       PushQueue pq = queues.get(senderId).add(payload, deviceNotification.getRegId());
//			       
//			       /**
//			         * Get a list of critical exceptions that underlying threads experienced.
//			         * Critical exceptions include CommunicationException and KeystoreException.
//			         * Exceptions related to tokens, payloads and such are *not* included here,
//			         * as they are noted in individual PushedNotification objects.
//			         * If critical exceptions are present, the underlying thread(s) is most
//			         * likely not working at all and you should solve the problem before
//			         * trying to go any further.
//			         *
//			         * @return a list of critical exceptions
//			         */
//
//			       List<Exception> pqe=pq.getCriticalExceptions();
//			       for (Exception exception : pqe) {
//					logger.error("PushQueue critical exception", exception);
//			       }
////				} catch (JSONException e) {
////					 logger.error("Error posting messages", e);
//				} catch (InvalidDeviceTokenFormatException e) {
//					 logger.error("Error posting messages", e);
//				} catch (javapns.json.JSONException e) {
//					 logger.error("Error posting messages", e);
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
//		
//		try {
//		createQueue();
//
//		
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send.. ");
//		}
//		
////		deprecated beacause doesn't use a queue and so isn't dynamic
//		
////		else if (deviceNotifications.size()==1) {
////			DeviceNotification deviceNotification = deviceNotifications.get(0);
////			List locArgs = new ArrayList();
////			locArgs.add(new String(from));
////			
////	
////			PushNotificationPayload payload = PushNotificationPayload.complex(); 
////			/* Customize the payload */ 
////			// payload.addAlert(message); // messaggio da tradurre server-side
////			try {
////				payload.addSound("default");
////			
////				
//////				payload.addCustomAlertLocKey(type); // messaggio tradotto client-side
////////				payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
//////				payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
////				payload.addCustomDictionary(C_URI_KEY, refId);
////				payload.addCustomDictionary(NOTIFICATION_TYPE_KEY, type);
////				payload.addAlert(message);
////				
//////				passare type con "t"-> nuovo "uri" questi hanno campo cURI, msg invece che messaggio
////				
//////				payload.addBadge(notificationService.hasNew(to));
////				
////				logger.debug("Sending message to one device. RegId :" + deviceNotification.getRegId());
////
////				List<PushedNotification> notifications = Push.payload(payload, certificatePathsRes.get(senderId).getFile(), keyStorePwds.get(senderId), productiondsdsds, deviceNotification.getRegId());				
////				for(PushedNotification pn : notifications) {
////					logger.debug("Received ApnsPush Notification: " + pn);
////				}
////			
////			} catch (JSONException e) {
////				 logger.error("Error posting messages", e);
////			} catch (CommunicationException e) {
////				logger.error("Error posting messages", e);
////			} catch (KeystoreException e) {
////				logger.error("Error posting messages", e);
////			} catch (IOException e) {
////				logger.error("Error posting messages", e);
////			}
////			
////		} 
//		else {
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
//			        PushQueue pq = queues.get(senderId).add(payload, deviceNotification.getRegId());
//			        
//			        /**
//			         * Get a list of critical exceptions that underlying threads experienced.
//			         * Critical exceptions include CommunicationException and KeystoreException.
//			         * Exceptions related to tokens, payloads and such are *not* included here,
//			         * as they are noted in individual PushedNotification objects.
//			         * If critical exceptions are present, the underlying thread(s) is most
//			         * likely not working at all and you should solve the problem before
//			         * trying to go any further.
//			         *
//			         * @return a list of critical exceptions
//			         */
//
//			        List<Exception> pqe=pq.getCriticalExceptions();
//				       for (Exception exception : pqe) {
//						logger.error("PushQueue critical exception", exception);
//				       }
//			       
////				} catch (JSONException e) {
////					 logger.error("Error posting messages", e);
//				} catch (InvalidDeviceTokenFormatException e) {
//					 logger.error("Error posting messages", e);
//				}
//			}
//		}
//	
//		}catch (Exception e) {
//				logger.error("Error sending apns push notification ", e);
//		}
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	//nuovi con json
//	
////	 public void sendToAll(String from, String message, String type, String refId, String senderId) {
////		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByTypeAndTenant(DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
////		 send(from, deviceNotifications, message, type, refId, senderId);
////	 }
//	 public void sendAsJson(String from, String to, String message,Map<String,String> data,  Integer appleBadge, String type,String senderId) {
//		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.IOS_TYPE, tenantService.getCurrentTenantName());
//		 sendAsJson(from, deviceNotifications, message, data, appleBadge,type, senderId);
//	 }
//	
//	
////	@Async
//	public void sendAsJson(String from, List<DeviceNotification> deviceNotifications, String message, Map<String,String> data,  Integer appleBadge, String type,String senderId)
//		 {
//		
//		try {
//		createQueue();
//
//		
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send.. ");
//		}
//		
////		deprecated beacause doesn't use a queue and so isn't dynamic
//		
////		else if (deviceNotifications.size()==1) {
////			DeviceNotification deviceNotification = deviceNotifications.get(0);
////			List locArgs = new ArrayList();
////			locArgs.add(new String(from));
////			
////	
////			PushNotificationPayload payload = PushNotificationPayload.complex(); 
////			/* Customize the payload */ 
////			// payload.addAlert(message); // messaggio da tradurre server-side
////			try {
////				payload.addSound("default");
////			
////				
//////				payload.addCustomAlertLocKey(type); // messaggio tradotto client-side
////////				payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
//////				payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
////				payload.addCustomDictionary(C_URI_KEY, refId);
////				payload.addCustomDictionary(NOTIFICATION_TYPE_KEY, type);
////				payload.addAlert(message);
////				
//////				passare type con "t"-> nuovo "uri" questi hanno campo cURI, msg invece che messaggio
////				
//////				payload.addBadge(notificationService.hasNew(to));
////				
////				logger.debug("Sending message to one device. RegId :" + deviceNotification.getRegId());
////
////				List<PushedNotification> notifications = Push.payload(payload, certificatePathsRes.get(senderId).getFile(), keyStorePwds.get(senderId), productiondsdsds, deviceNotification.getRegId());				
////				for(PushedNotification pn : notifications) {
////					logger.debug("Received ApnsPush Notification: " + pn);
////				}
////			
////			} catch (JSONException e) {
////				 logger.error("Error posting messages", e);
////			} catch (CommunicationException e) {
////				logger.error("Error posting messages", e);
////			} catch (KeystoreException e) {
////				logger.error("Error posting messages", e);
////			} catch (IOException e) {
////				logger.error("Error posting messages", e);
////			}
////			
////		} 
//		else {
//			List locArgs = new ArrayList();
//			locArgs.add(new String(from));
//			
//			for (DeviceNotification deviceNotification : deviceNotifications) {
//				  logger.debug("Sending message to device: " + deviceNotification.getRegId());
//				PushNotificationPayload payload = PushNotificationPayload.complex(); 	
//				try {
//					
//					payload.addSound("default");
//					
//					if (data!=null && data.get("_smart21_sound")!=null)
//						payload.addSound(data.get("_smart21_sound"));
//					
//				
////					payload.addCustomAlertLocKey(type); // messaggio tradotto client-side
//////					payload.addCustomAlertLocKey("push-liked"); // messaggio tradotto client-side
////					
////					payload.addCustomAlertLocArgs(locArgs); // argomenti del messaggio client-side
////					payload.addCustomDictionary(C_URI_KEY, refId);
////					payload.addCustomDictionary(NOTIFICATION_TYPE_KEY, type);
//					
//					for (Map.Entry<String, String> entry : data.entrySet()) {
//					    String key = entry.getKey();
//					    String value = entry.getValue();
//					    payload.addCustomDictionary(key, value);
//					}
//					
//					payload.addAlert(message);
//					
//					if (appleBadge!=null)
//						payload.addBadge(appleBadge);
//					
////					payload.addBadge(notificationService.hasNew(to));
//					
//					  /* Add a notification for the queue to push */ 
////					logger.debug("queues.get(senderId) : " + queues.get(senderId)); 
//			        PushQueue pq = queues.get(senderId).add(payload, deviceNotification.getRegId());
//			        
//			        /**
//			         * Get a list of critical exceptions that underlying threads experienced.
//			         * Critical exceptions include CommunicationException and KeystoreException.
//			         * Exceptions related to tokens, payloads and such are *not* included here,
//			         * as they are noted in individual PushedNotification objects.
//			         * If critical exceptions are present, the underlying thread(s) is most
//			         * likely not working at all and you should solve the problem before
//			         * trying to go any further.
//			         *
//			         * @return a list of critical exceptions
//			         */
//
//			        List<Exception> pqe=pq.getCriticalExceptions();
//				       for (Exception exception : pqe) {
//						logger.error("PushQueue critical exception", exception);
//				       }
////				} catch (JSONException e) {
////					 logger.error("Error posting messages", e);
//				} catch (InvalidDeviceTokenFormatException e) {
//					 logger.error("Error posting messages", e);
//				}
//			}
//		}
//	
//		}catch (Exception e) {
//				logger.error("Error sending apns push notification ", e);
//		}
//	}
//	
//	@Override
//	public void clean() {
//		this.queues = new HashMap<String, PushQueue>();
//		logger.info("APNS Queues cleaned");
//	}
//
//
//	@Override
//	public void send(String from, String to, String message, String refId
//			){
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//	
//	
//
//
//
//}
