//package shoppino.notification;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.MessageSource;
//import org.springframework.context.NoSuchMessageException;
//
//import shoppino.notification.dao.DeviceNotificationDAO;
//import shoppino.notification.domain.DeviceNotification;
//import shoppino.service.EnvironmentService;
//import shoppino.service.NotificationService;
//import shoppino.service.TenantService;
//
//import com.google.android.gcm.server.Constants;
//import com.google.android.gcm.server.Message;
//import com.google.android.gcm.server.MulticastResult;
//import com.google.android.gcm.server.Result;
//import com.google.android.gcm.server.Sender;
//
//import openrecordz.domain.Tenant;
//import openrecordz.exception.ShoppinoRuntimeException;
//
//public class GcmPushNotificationManagerMTDynamicImpl implements PushNotificationManager {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//	private static final int MULTICAST_SIZE = 1000;
//	private static final Executor threadPool = Executors.newFixedThreadPool(5);
//	
//	public static String TENANT_NOTIFICATION_CURRENT_ANDROID_GCM_APIKEY_KEY = "tenants.notification.%s.android.gcm.%s.apikey";
//	private Map<String, Sender> senders;
////	private List<String> apiKeys;
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
//	@Autowired
//	@Qualifier(value="notificationMessageSource")
//	MessageSource messageSource;
//	
//	@Autowired
//	EnvironmentService environmentService;
//	
//	public GcmPushNotificationManagerMTDynamicImpl() {
//		this.senders = new HashMap<String, Sender>();
//	}
//
////	public GcmPushNotificationManagerMTDynamicImpl(Map<String, String> apiKeys) {
//////		this.apiKeys = apiKeys;
////		this.senders = new HashMap<String, Sender>();
////		Sender sender;
////		for (Map.Entry<String, String> apiKey : apiKeys.entrySet()) {
////			 sender = new Sender(apiKey.getValue());
////			 logger.debug("Gcm sender : " + apiKey.getKey() + " " + apiKey.getValue());
////			 senders.put(apiKey.getKey(), sender);
////		}
////		
////	}
//	
//	public void createSender() {
//		String tenant = tenantService.getCurrentTenantName();
//		if (senders.containsKey(tenant)) {
//			logger.debug("GCM Push Notification Sender already created for tenant . " + tenant);
//		}else {
//			try {
//				String apiKey = messageSource.getMessage(String.format(TENANT_NOTIFICATION_CURRENT_ANDROID_GCM_APIKEY_KEY,tenant, environmentService.getShortEnvironmentName()), null, Locale.getDefault());
//				logger.debug("Creating Gcm sender for : " + tenant + ", apikey " + apiKey + " and short env " + environmentService.getShortEnvironmentName());
//				Sender sender = new Sender(apiKey);
//				senders.put(tenant, sender);
//				logger.info("Gcm sender created for : " + tenant + ", apikey " + apiKey + " and short env " + environmentService.getShortEnvironmentName() );
//			}catch (NoSuchMessageException nsme) {
//				logger.error("Android GCM apiKey not found for tenant : " + tenant + " and short env : " + environmentService.getShortEnvironmentName(), nsme);
//				throw new ShoppinoRuntimeException("Android GCM apiKey not found for tenant : " + tenant + " and short env : " + environmentService.getShortEnvironmentName(), nsme);
//			}
//		}
//	}
//	 
//	@Deprecated
//	public void send(String from, String to, String message, String refId, String senderId) {
//		createSender();
//		
//		
//		List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
//		if (deviceNotifications.size()==0) {
//			logger.debug("Nothing to send to " + to);
//		}else if (deviceNotifications.size()==1) {
//			DeviceNotification deviceNotification = deviceNotifications.get(0);
//			
//			 Message gcmMessage = new Message.Builder()
////				.collapseKey("Likes")
////	        	.collapseKey(timestamp)
//				/**
//				 * The Time to Live (TTL) feature lets the sender specify the maximum 
//				 * lifespan of a message using the time_to_live parameter in the send 
//				 * request. The value of this parameter must be a duration from 0 to 
//				 * 2,419,200 seconds, and it corresponds to the maximum period of time 
//				 * for which GCM will store and try to deliver the message.
//				 */
//				.timeToLive(3)
////				.delayWhileIdle(true)
//				.delayWhileIdle(false)
////				.addData("Type", "Like")
////				.addData("Message", "Like dall'utente XXX")
//				.addData("Type", message)	
//				.addData("From", from)
//				.addData("RefId", refId)
//	        	.build();
//			 
//			 	/**
//		         * send(Message message, List<String> regIds, int retries)
//		         * Sends a message to many devices, retrying in case of unavailability.
//		         * OR
//		         * send(Message message, String registrationId, int retries)
//		         * Sends a message to one device, retrying in case of unavailability.
//		         */
//			 try {
////			 	sender = new Sender(this.apiKey);
////		        Result result = sender.send(gcmMessage, "APA91bFoQPr1wWLGW2BvXnUR1nwgVlU_LoJEl0RzPD8imf_befoT0PK7cTZ-JuQonnqaBG_I__66ld2Ei4SCCTIyHdaDAsvN2tLaiNnmkUrPz3bgdW8wjn48m4rg-gvLQIp7ZiVaj0o63Kfr6LREe5gfZ4vV8msdvDmAtY31Yf0HQbCs1J7iMEY", 5);
//		        Result result = senders.get(senderId).send(gcmMessage, deviceNotification.getRegId(), 5);
//		        logger.debug("Sent message to one device. RegId :" + deviceNotification.getRegId() +" Result: " + result);
//			 }catch (IOException e) {
//				 logger.error("Error posting messages", e);
//			}
//		}
//		 else {
//		        // send a multicast message using JSON
//		        // must split in chunks of 1000 devices (GCM limit)
//		        int total = deviceNotifications.size();
//		        List<String> partialDevices = new ArrayList<String>(total);
//		        int counter = 0;
//		        int tasks = 0;
//		        for (DeviceNotification deviceNotification : deviceNotifications) {
//		          counter++;
//		          partialDevices.add(deviceNotification.getRegId());
//		          int partialSize = partialDevices.size();
//		          if (partialSize == MULTICAST_SIZE || counter == total) {
//		            asyncSend(partialDevices, message, from, refId, senderId);
//		            partialDevices.clear();
//		            tasks++;
//		          }
//		        }
//		        logger.debug("Asynchronously sending " + tasks + " multicast messages to " +
//		            total + " devices");
//		      }		    
//	}
//
//	@Deprecated
//	 private void asyncSend(List<String> partialDevices, final String message, final String from, final String refId, final String senderId) {
//		    // make a copy
//		    final List<String> devices = new ArrayList<String>(partialDevices);
//		    threadPool.execute(new Runnable() {
//
//		      public void run() {
//		    	  
//		    	  //set tenant into thread
//		    	  String currentTenant = senderId;
//		    	  tenantService.setCurrentTenant(new Tenant(currentTenant));
//		    	  logger.debug("setted tenant for thread with value : " + currentTenant);
//		    	  
//		        Message gcmMessage = new Message.Builder()
//		        .timeToLive(3)
//				.delayWhileIdle(false)
//		        .addData("Type", message)	
//				.addData("From", from)
//				.addData("RefId", refId)
//				.build();
//		        MulticastResult multicastResult;
//		        try {
//		          multicastResult = senders.get(senderId).send(gcmMessage, devices, 5);
//		        } catch (IOException e) {
//		          logger.error("Error posting messages", e);
//		          return;
//		        }
//		        List<Result> results = multicastResult.getResults();
//		        // analyze the results
//		        for (int i = 0; i < devices.size(); i++) {
//		          String regId = devices.get(i);
//		          Result result = results.get(i);
//		          String messageId = result.getMessageId();
//		          if (messageId != null) {
//		            logger.debug("Succesfully sent message to device: " + regId +
//		                "; messageId = " + messageId);
//		            String canonicalRegId = result.getCanonicalRegistrationId();
//		            if (canonicalRegId != null) {
//		              // same device has more than on registration id: update it
//		              logger.info("canonicalRegId " + canonicalRegId);
//		              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
////		              notificationService.updateRegistration(regId, canonicalRegId, DeviceNotification.ANDROID_TYPE);
////		              Datastore.updateRegistration(regId, canonicalRegId);
//		            }
//		          } else {
//		            String error = result.getErrorCodeName();
//		            if (error.equals(Constants.ERROR_NOT_REGISTERED) || error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
//		              // application has been removed from device - unregister it
//		              logger.info("Unregistered device: " + regId);
////		              Datastore.unregister(regId);
//		              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
//		            } else {
//		              logger.error("Error sending message to " + regId + ": " + error);
//		            }
//		          }
//		        }
//		      }});
//		  }
//
//	 
//	
//	//nuovi send //
//	
//	
//	
//	
//	
//	 public void sendToAll(String from, String message, String type, String refId, String senderId) {
//		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByTypeAndTenant(DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
//		 send(from, deviceNotifications, message, type, refId, senderId);
//	 }
//	 public void send(String from, String to, String message, String type, String refId, String senderId) {
//		 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
//		 send(from, deviceNotifications, message, type, refId, senderId);
//	 }
//	 
//	 public void send(String from, List<DeviceNotification> deviceNotifications , String message, String type, String refId, String senderId) {
//		
//		 try {
//		 createSender();
//		 
//			if (deviceNotifications.size()==0) {
//				logger.debug("Nothing to send... ");
//			}else if (deviceNotifications.size()==1) {
//				DeviceNotification deviceNotification = deviceNotifications.get(0);
//				
//				 Message gcmMessage = new Message.Builder()
//	//					.collapseKey("Likes")
//	//		        	.collapseKey(timestamp)
//					/**
//					 * The Time to Live (TTL) feature lets the sender specify the maximum 
//					 * lifespan of a message using the time_to_live parameter in the send 
//					 * request. The value of this parameter must be a duration from 0 to 
//					 * 2,419,200 seconds, and it corresponds to the maximum period of time 
//					 * for which GCM will store and try to deliver the message.
//					 */
//					.timeToLive(3)
//	//					.delayWhileIdle(true)
//					.delayWhileIdle(false)
//	//					.addData("Type", "Like")
//	//					.addData("Message", "Like dall'utente XXX")
//					.addData("Type", type)	
//					.addData("Message", message)					
//					.addData("cURI", refId)
//		        	.build();
//				 
//				 	/**
//			         * send(Message message, List<String> regIds, int retries)
//			         * Sends a message to many devices, retrying in case of unavailability.
//			         * OR
//			         * send(Message message, String registrationId, int retries)
//			         * Sends a message to one device, retrying in case of unavailability.
//			         */
//				 try {
//	//				 	sender = new Sender(this.apiKey);
//	//			        Result result = sender.send(gcmMessage, "APA91bFoQPr1wWLGW2BvXnUR1nwgVlU_LoJEl0RzPD8imf_befoT0PK7cTZ-JuQonnqaBG_I__66ld2Ei4SCCTIyHdaDAsvN2tLaiNnmkUrPz3bgdW8wjn48m4rg-gvLQIp7ZiVaj0o63Kfr6LREe5gfZ4vV8msdvDmAtY31Yf0HQbCs1J7iMEY", 5);
//			        Result result = senders.get(senderId).send(gcmMessage, deviceNotification.getRegId(), 5);
//			        logger.debug("Sent message to one device with regid : "+  deviceNotification.getRegId() + " Result: " + result);
//				 }catch (IOException e) {
//					 logger.error("Error posting messages", e);
//				}
//			}
//			 else {
//			        // send a multicast message using JSON
//			        // must split in chunks of 1000 devices (GCM limit)
//			        int total = deviceNotifications.size();
//			        List<String> partialDevices = new ArrayList<String>(total);
//			        int counter = 0;
//			        int tasks = 0;
//			        for (DeviceNotification deviceNotification : deviceNotifications) {
//			          counter++;
//			          partialDevices.add(deviceNotification.getRegId());
//			          int partialSize = partialDevices.size();
//			          if (partialSize == MULTICAST_SIZE || counter == total) {
//			            asyncSend(partialDevices, message, type, from, refId, senderId);
//			            partialDevices.clear();
//			            tasks++;
//			          }
//			        }
//			        logger.debug("Asynchronously sending " + tasks + " multicast messages to " +
//			            total + " devices");
//			      }		    
//		 }catch (Exception e) {
//			logger.error("Error sending gcm push notification .. ", e);
//		 }
//		}
//
//		 private void asyncSend(List<String> partialDevices, final String message, final String type, final String from, final String refId, final String senderId) {
//			    // make a copy
//			    final List<String> devices = new ArrayList<String>(partialDevices);
//			    threadPool.execute(new Runnable() {
//
//			      public void run() {
//			    	  
//			    	  //set tenant into thread
//			    	  String currentTenant = senderId;
//			    	  tenantService.setCurrentTenant(new Tenant(currentTenant));
//			    	  logger.debug("setted tenant for thread with value : " + currentTenant);
//
//			        Message gcmMessage = new Message.Builder()
//			        .timeToLive(3)
//			        .delayWhileIdle(false)
//			        .addData("Type", type)	
//					.addData("Message", message)					
//					.addData("cURI", refId)
//					.build();
//			        
//			        
//			        MulticastResult multicastResult;
//			        try {
//			          multicastResult = senders.get(senderId).send(gcmMessage, devices, 5);
//			        } catch (IOException e) {
//			          logger.error("Error posting messages", e);
//			          return;
//			        }
//			        logger.debug("multicastResult : " + multicastResult);
//			        logger.debug("multicastResult.getCanonicalIds() : " + multicastResult.getCanonicalIds());
//			        logger.debug("multicastResult.getFailure() : " + multicastResult.getFailure());
//			        logger.debug("multicastResult.getSuccess() : " + multicastResult.getSuccess());
//			        logger.debug("multicastResult.getTotal() : " + multicastResult.getTotal());
//			        logger.debug("multicastResult.getMulticastId() : " + multicastResult.getMulticastId());
//			        
//			        List<Result> results = multicastResult.getResults();
//			        // analyze the results
//			        for (int i = 0; i < devices.size(); i++) {
//			          String regId = devices.get(i);
//			          Result result = results.get(i);
//			          logger.debug(" result : " +  result);
//			          logger.debug(" result.getErrorCodeName() : " +  result.getErrorCodeName());
//			          logger.debug(" result.getMessageId() : " +  result.getMessageId());
//			          logger.debug(" result.getCanonicalRegistrationId() : " +  result.getCanonicalRegistrationId());
//			         
//			          String messageId = result.getMessageId();
//			          if (messageId != null) {
//			            logger.debug("Succesfully sent message to device with regid : " + regId +
//			                "; messageId = " + messageId);
//			            String canonicalRegId = result.getCanonicalRegistrationId();
//			            if (canonicalRegId != null) {
//			              // same device has more than on registration id: update it
//			              logger.info("canonicalRegId " + canonicalRegId);
//			              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
////			              notificationService.updateRegistration(regId, canonicalRegId, DeviceNotification.ANDROID_TYPE);
////			              Datastore.updateRegistration(regId, canonicalRegId);
//			            }
//			          } else {
//			            String error = result.getErrorCodeName();
//			            if (error.equals(Constants.ERROR_NOT_REGISTERED) || error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
//			              // application has been removed from device - unregister it
//			              logger.info("Unregistered device: " + regId);
////			              Datastore.unregister(regId);
//			              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
//			            } else {
//			              logger.error("Error sending message to " + regId + ": " + error);
//			            }
//			          }
//			        }
//			      }});
//			  }
//
//		 
//		 
//		 
//		 //nuovi send con json 
//		 
//		 
//			//nuovi send //
//			
//			
//			
//			
////		@Override
////		 public void sendToAllAsJson(String from, String message,  Map<String,String> data,String type, String senderId) {
////			 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByTypeAndTenant(DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
////			 sendAsJson(from, deviceNotifications, message, data, type,senderId);
////		 }
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
////		 luca conversano
////		 private void sendNotification(Bundle extras){
////		    	//Tipologie di Notifica ("like_msg" o "alert" o "uri").
////		    	
////				Log.d(TAG, "sendNotification");
////				
////				String titleNotification = getResources().getString(R.string.app_name);
////				
////				//Definizione Messaggio riguardante la notifica in base alla tipologia ("like_msg" o "alert" o "uri").
////		    	String notification_message = "";
////		    	String notification_type = extras.getString(FUtils.NOTIFICATION_TYPE);
////		    	if(notification_type.equals(FUtils.NOTIFICATION_TYPE_LIKE)){
////		    		//Notifica di un Like su un determinato prodotto con apertura del dettaglio del prodotto.
////		    		//Username dell'utente che ha eseguito la notifica.
////		    		String notification_from = extras.getString(FUtils.NOTIFICATION_FROM);
////		    		notification_message = String.format(getResources().getString(R.string.notification_like_msg), notification_from);
////		    	}else if(notification_type.equals(FUtils.NOTIFICATION_TYPE_ALERT)){
////		    		//Notifica ALERT con apertura di una popup.
////		    		notification_message = extras.getString(FUtils.NOTIFICATION_MESSAGE);
////		    	}else if(notification_type.equals(FUtils.NOTIFICATION_TYPE_URI)){
////		    		//Notifica di tipo URI (apertura di una webview al link indicato oppure
////		    		//apertura del dettaglio del prodotto indicato).
////		    		notification_message = extras.getString(FUtils.NOTIFICATION_MESSAGE);
////		    	}
////				
////				//Per visualizzare le notifiche push una dopo l'altra (senza raggrupparle)
////				//nel metodo 'notify' cambiare l'id.
////				int notificationId = (int)Calendar.getInstance().getTimeInMillis();
////				
////				// Build intent for notification content
////				Intent viewIntent = new Intent(this, SplashScreenActivity.class);
////				viewIntent.putExtra(FUtils.NOTIFICATION_PRESENT, true);
////				viewIntent.putExtra(FUtils.NOTIFICATION_BUNDLE_EXTRAS, extras);
////				
//////				PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
////				PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
////
////				int largeIcon = R.drawable.ic_logo_app;
////		    	int smallIcon = R.drawable.ic_stat_notify_like;
////				
////				Bitmap icon = BitmapFactory.decodeResource(getResources(), largeIcon);
////		      
////				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
////			        .setLargeIcon(icon)
////			        .setSmallIcon(smallIcon)
////			        .setContentTitle(titleNotification)
//////					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//////			        .setContentText(contentNotification)
////			        .setContentText(notification_message)
//////			        .setContentInfo("1")
////			        .setDefaults(Notification.DEFAULT_ALL)
////			        .setAutoCancel(true)
//////			        .setDefaults(Notification.DEFAULT_SOUND)
////			        .setContentIntent(viewPendingIntent)
////			        ;
////				
////				// Get an instance of the NotificationManager service
////				NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
////
////				// Build the notification and issues it with notification manager.
////				notificationManager.notify(notificationId, notificationBuilder.build());
////		    	
////		    }
//		 
//		 
//		 
//		 
////		 
////		 private void notificationAction(Bundle extras){
////				//Gestione della notifica ricevuta.
////				Log.d(TAG, "Click su una notifica!");
////				
////				//Recupero della tipologia di notifica ("like_msg", "alert" o "uri").
////				String notification_type = extras.getString(FUtils.NOTIFICATION_TYPE);
////		    	if(notification_type.equals(FUtils.NOTIFICATION_TYPE_LIKE)){
////		    		//Notifica di un Like su un determinato item con apertura del dettaglio delo stesso.
////		    		//Id dell'oggetto su cui è stata eseguita la notifica.
////		        	String itemId = extras.getString(FUtils.NOTIFICATION_REF_ID);
////		        	viewItemDetails(itemId);
////		    	}else if(notification_type.equals(FUtils.NOTIFICATION_TYPE_ALERT)){
////		    		//Notifica ALERT con apertura di una popup.
////		    		String message = extras.getString(FUtils.NOTIFICATION_MESSAGE);
////		    		showAlertNotification(message);
////		    	}else if(notification_type.equals(FUtils.NOTIFICATION_TYPE_URI)){
////		    		//Notifica di tipo URI (apertura di una webview al link indicato oppure
////		    		//apertura del dettaglio del prodotto indicato).
////		    		//Recupero dell'Uri ricevuto.
////		        	String strUri = extras.getString(FUtils.NOTIFICATION_URI);
////		        	if(strUri.startsWith("http://")){
////		        		//Apertura di una webview all'url indicato.
////		        		showUrlNotification(strUri);
////		        	}else if(strUri.startsWith("deal://")){
////		        		//Apertura del dettaglio del prodotto.
////		        		//Recupero Id dell'oggetto della notifica.
////		            	String itemId = strUri.replace("deal://", "");
////		            	viewItemDetails(itemId);
////		        	}
////		    	}
////			}
//		 
//		@Override
//		 public void sendAsJson(String from, String to, String message,  Map<String,String> data,  Integer appleBadge, String type,String senderId) {
//			 List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
//			 sendAsJson(from, deviceNotifications, message, data, type, senderId);
//		 }
//		 
//		 public void sendAsJson(String from, List<DeviceNotification> deviceNotifications , String message,  Map<String,String> data, String type, String senderId) {
//			
//			 try {
//			 createSender();
//			 
//				if (deviceNotifications.size()==0) {
//					logger.debug("Nothing to send... ");
//				}else if (deviceNotifications.size()==1) {
//					DeviceNotification deviceNotification = deviceNotifications.get(0);
//					
//					 Message.Builder gcmMessageBuilder = new Message.Builder();
//		//					.collapseKey("Likes")
//		//		        	.collapseKey(timestamp)
//						/**
//						 * The Time to Live (TTL) feature lets the sender specify the maximum 
//						 * lifespan of a message using the time_to_live parameter in the send 
//						 * request. The value of this parameter must be a duration from 0 to 
//						 * 2,419,200 seconds, and it corresponds to the maximum period of time 
//						 * for which GCM will store and try to deliver the message.
//						 */
//					 	gcmMessageBuilder.timeToLive(3);
//		//					.delayWhileIdle(true)
//						gcmMessageBuilder.delayWhileIdle(false);
//		//					.addData("Type", "Like")
//		//					.addData("Message", "Like dall'utente XXX")
//					
//						
//					for (Map.Entry<String, String> entry : data.entrySet()) {
//					    String key = entry.getKey();
//					    String value = entry.getValue();
//						gcmMessageBuilder.addData(key, value);
//					}
//					
//					gcmMessageBuilder.addData("Type", type);
//					gcmMessageBuilder.addData("Message", message);
//						
//			        	Message gcmMessage = gcmMessageBuilder.build();
//					 
//					 	/**
//				         * send(Message message, List<String> regIds, int retries)
//				         * Sends a message to many devices, retrying in case of unavailability.
//				         * OR
//				         * send(Message message, String registrationId, int retries)
//				         * Sends a message to one device, retrying in case of unavailability.
//				         */
//					 try {
//		//				 	sender = new Sender(this.apiKey);
//		//			        Result result = sender.send(gcmMessage, "APA91bFoQPr1wWLGW2BvXnUR1nwgVlU_LoJEl0RzPD8imf_befoT0PK7cTZ-JuQonnqaBG_I__66ld2Ei4SCCTIyHdaDAsvN2tLaiNnmkUrPz3bgdW8wjn48m4rg-gvLQIp7ZiVaj0o63Kfr6LREe5gfZ4vV8msdvDmAtY31Yf0HQbCs1J7iMEY", 5);
//				        Result result = senders.get(senderId).send(gcmMessage, deviceNotification.getRegId(), 5);
//				        logger.debug("Sent message to one device with regid : "+  deviceNotification.getRegId() + " Result: " + result);
//					 }catch (IOException e) {
//						 logger.error("Error posting messages", e);
//					}
//				}
//				 else {
//				        // send a multicast message using JSON
//				        // must split in chunks of 1000 devices (GCM limit)
//				        int total = deviceNotifications.size();
//				        List<String> partialDevices = new ArrayList<String>(total);
//				        int counter = 0;
//				        int tasks = 0;
//				        for (DeviceNotification deviceNotification : deviceNotifications) {
//				          counter++;
//				          partialDevices.add(deviceNotification.getRegId());
//				          int partialSize = partialDevices.size();
//				          if (partialSize == MULTICAST_SIZE || counter == total) {
//				        	  asyncSendAsJson(partialDevices, message, type, from, data, senderId);
//				            partialDevices.clear();
//				            tasks++;
//				          }
//				        }
//				        logger.debug("Asynchronously sending " + tasks + " multicast messages to " +
//				            total + " devices");
//				      }		    
//			 }catch (Exception e) {
//				logger.error("Error sending gcm push notification .. ", e);
//			 }
//			}
//
//			 private void asyncSendAsJson(List<String> partialDevices, final String message, final String type, final String from, final Map<String, String> data, final String senderId) {
//				    // make a copy
//				    final List<String> devices = new ArrayList<String>(partialDevices);
//				    threadPool.execute(new Runnable() {
//
//				      public void run() {
//				    	  
//				    	  //set tenant into thread
//				    	  String currentTenant = senderId;
//				    	  tenantService.setCurrentTenant(new Tenant(currentTenant));
//				    	  logger.debug("setted tenant for thread with value : " + currentTenant);
//
//				    	  Message.Builder gcmMessageBuilder = new Message.Builder();
//				    	  gcmMessageBuilder.timeToLive(3);
//				    	  gcmMessageBuilder.delayWhileIdle(false);
//				    	  				
//				    	  
//				    	  for (Map.Entry<String, String> entry : data.entrySet()) {
//							    String key = entry.getKey();
//							    String value = entry.getValue();
//								gcmMessageBuilder.addData(key, value);
//							}
//				    	  
//				    	  gcmMessageBuilder.addData("Type", type);	
//				    	  gcmMessageBuilder.addData("Message", message);	
//				    	  
//				    	  Message gcmMessage = gcmMessageBuilder.build();
//				        
//				        
//				        MulticastResult multicastResult;
//				        try {
//				          multicastResult = senders.get(senderId).send(gcmMessage, devices, 5);
//				        } catch (IOException e) {
//				          logger.error("Error posting messages", e);
//				          return;
//				        }
//				        List<Result> results = multicastResult.getResults();
//				        // analyze the results
//				        for (int i = 0; i < devices.size(); i++) {
//				          String regId = devices.get(i);
//				          Result result = results.get(i);
//				          String messageId = result.getMessageId();
//				          if (messageId != null) {
//				            logger.debug("Succesfully sent message to device with regid : " + regId +
//				                "; messageId = " + messageId);
//				            String canonicalRegId = result.getCanonicalRegistrationId();
//				            if (canonicalRegId != null) {
//				              // same device has more than on registration id: update it
//				              logger.info("canonicalRegId " + canonicalRegId);
//				              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
////				              notificationService.updateRegistration(regId, canonicalRegId, DeviceNotification.ANDROID_TYPE);
////				              Datastore.updateRegistration(regId, canonicalRegId);
//				            }
//				          } else {
//				            String error = result.getErrorCodeName();
//				            if (error.equals(Constants.ERROR_NOT_REGISTERED) || error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
//				              // application has been removed from device - unregister it
//				              logger.info("Unregistered device: " + regId);
////				              Datastore.unregister(regId);
//				              notificationService.unregisterDevice(regId, DeviceNotification.ANDROID_TYPE);
//				            } else {
//				              logger.error("Error sending message to " + regId + ": " + error);
//				            }
//				          }
//				        }
//				      }});
//				  }
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
//		 
//	@Override
//	public void send(String from, String to, String message, String refId)
//			{
//		throw new UnsupportedOperationException("Method not implemented");
//		
//	}
//
//	@Override
//	public void clean() {
//		this.senders = new HashMap<String, Sender>();
//		logger.info("GCM Senders cleaned");
//		
//	}
//	 
//	 
//}
