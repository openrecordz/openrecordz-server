package shoppino.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import shoppino.notification.dao.DeviceNotificationDAO;
import shoppino.notification.domain.DeviceNotification;
import shoppino.service.NotificationService;
import shoppino.service.TenantService;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GcmPushNotificationManagerImpl implements PushNotificationManager {

	protected final Log logger = LogFactory.getLog(getClass());
	private static final int MULTICAST_SIZE = 1000;
	private static final Executor threadPool = Executors.newFixedThreadPool(5);
	
	private Sender sender;
	private String apiKey;
	 
	@Autowired
	DeviceNotificationDAO deviceNotificationDAO;
	
	@Autowired
	NotificationService notificationService;
	 
	@Autowired
	TenantService tenantService;
	
	@Deprecated
	public void send(String from, String to, String message, String refId) {
		List<DeviceNotification> deviceNotifications = deviceNotificationDAO.findByUsernameAndTypeAndTenant(to, DeviceNotification.ANDROID_TYPE, tenantService.getCurrentTenantName());
		if (deviceNotifications.size()==0) {
			logger.debug("Nothing to send");
		}else if (deviceNotifications.size()==1) {
			DeviceNotification deviceNotification = deviceNotifications.get(0);
			
			 Message gcmMessage = new Message.Builder()
//				.collapseKey("Likes")
//	        	.collapseKey(timestamp)
				/**
				 * The Time to Live (TTL) feature lets the sender specify the maximum 
				 * lifespan of a message using the time_to_live parameter in the send 
				 * request. The value of this parameter must be a duration from 0 to 
				 * 2,419,200 seconds, and it corresponds to the maximum period of time 
				 * for which GCM will store and try to deliver the message.
				 */
				.timeToLive(3)
				.delayWhileIdle(true)
//				.addData("Type", "Like")
//				.addData("Message", "Like dall'utente XXX")
				.addData("Type", message)	
				.addData("From", from)
				.addData("RefId", refId)
	        	.build();
			 
			 	/**
		         * send(Message message, List<String> regIds, int retries)
		         * Sends a message to many devices, retrying in case of unavailability.
		         * OR
		         * send(Message message, String registrationId, int retries)
		         * Sends a message to one device, retrying in case of unavailability.
		         */
			 try {
//			 	sender = new Sender(this.apiKey);
//		        Result result = sender.send(gcmMessage, "APA91bFoQPr1wWLGW2BvXnUR1nwgVlU_LoJEl0RzPD8imf_befoT0PK7cTZ-JuQonnqaBG_I__66ld2Ei4SCCTIyHdaDAsvN2tLaiNnmkUrPz3bgdW8wjn48m4rg-gvLQIp7ZiVaj0o63Kfr6LREe5gfZ4vV8msdvDmAtY31Yf0HQbCs1J7iMEY", 5);
		        Result result = sender.send(gcmMessage, deviceNotification.getRegId(), 5);
		        logger.debug("Sent message to one device: " + result);
			 }catch (IOException e) {
				 logger.error("Error posting messages", e);
			}
		}
		 else {
		        // send a multicast message using JSON
		        // must split in chunks of 1000 devices (GCM limit)
		        int total = deviceNotifications.size();
		        List<String> partialDevices = new ArrayList<String>(total);
		        int counter = 0;
		        int tasks = 0;
		        for (DeviceNotification deviceNotification : deviceNotifications) {
		          counter++;
		          partialDevices.add(deviceNotification.getRegId());
		          int partialSize = partialDevices.size();
		          if (partialSize == MULTICAST_SIZE || counter == total) {
		            asyncSend(partialDevices, message, from, refId);
		            partialDevices.clear();
		            tasks++;
		          }
		        }
		        logger.debug("Asynchronously sending " + tasks + " multicast messages to " +
		            total + " devices");
		      }		    
	}

	 private void asyncSend(List<String> partialDevices, final String message, final String from, final String refId) {
		    // make a copy
		    final List<String> devices = new ArrayList<String>(partialDevices);
		    threadPool.execute(new Runnable() {

		      public void run() {
		        Message gcmMessage = new Message.Builder()
		        .addData("Type", message)	
				.addData("From", from)
				.addData("RefId", refId)
				.build();
		        MulticastResult multicastResult;
		        try {
		          multicastResult = sender.send(gcmMessage, devices, 5);
		        } catch (IOException e) {
		          logger.error("Error posting messages", e);
		          return;
		        }
		        List<Result> results = multicastResult.getResults();
		        // analyze the results
		        for (int i = 0; i < devices.size(); i++) {
		          String regId = devices.get(i);
		          Result result = results.get(i);
		          String messageId = result.getMessageId();
		          if (messageId != null) {
		            logger.debug("Succesfully sent message to device: " + regId +
		                "; messageId = " + messageId);
		            String canonicalRegId = result.getCanonicalRegistrationId();
		            if (canonicalRegId != null) {
		              // same device has more than on registration id: update it
		              logger.info("canonicalRegId " + canonicalRegId);
		              notificationService.updateRegistration(regId, canonicalRegId, DeviceNotification.ANDROID_TYPE);
//		              Datastore.updateRegistration(regId, canonicalRegId);
		            }
		          } else {
		            String error = result.getErrorCodeName();
		            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
		              // application has been removed from device - unregister it
		              logger.info("Unregistered device: " + regId);
//		              Datastore.unregister(regId);
		              notificationService.unregister(regId, DeviceNotification.ANDROID_TYPE);
		            } else {
		              logger.error("Error sending message to " + regId + ": " + error);
		            }
		          }
		        }
		      }});
		  }
	 
	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		 sender = new Sender(this.apiKey);
	}

	@Deprecated
	@Override
	public void send(String from, String to, String message, String refId,
			String senderId) {
		throw new UnsupportedOperationException("Method not implemented");
		
	}

	@Override
	public void send(String from, String to, String message, String type,
			String refId, String senderId) {
		throw new UnsupportedOperationException("Method not implemented");
		
	}

	@Override
	public void sendToAll(String from, String message, String type,
			String refId, String senderId) {
		throw new UnsupportedOperationException("Method not implemented");
		
	}

	@Override
	public void clean() {
		throw new UnsupportedOperationException("Method not implemented");
		
	}

	@Override
	public void sendAsJson(String from, String to, String message,
			Map<String, String> data, Integer appleBadge, String type,
			String senderId) {
		throw new UnsupportedOperationException("Method not implemented");
		
	}
	 
}
