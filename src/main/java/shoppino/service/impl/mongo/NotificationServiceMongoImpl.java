package shoppino.service.impl.mongo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import openrecordz.domain.Notification;
import openrecordz.domain.Person;
import shoppino.exception.ResourceNotFoundException;
import shoppino.exception.ShoppinoException;
import shoppino.exception.TooManyNotificationException;
import shoppino.mail.service.EmailMessageType;
import shoppino.mail.service.MailService;
import shoppino.notification.PushNotificationManager;
import shoppino.notification.dao.DeviceNotificationDAO;
import shoppino.notification.domain.DeviceNotification;
import shoppino.persistence.mongo.NotificationRepository;
import shoppino.security.service.AuthenticationService;
import shoppino.service.NotificationService;
import shoppino.service.PersonSearchService;
import shoppino.service.PersonService;
import shoppino.service.TenantService;
import shoppino.util.DateUtil;

public class NotificationServiceMongoImpl implements NotificationService {
	
	protected Log log = LogFactory.getLog(getClass());

	
	@Autowired
	NotificationRepository repository;
	
	@Autowired
	MongoOperations operations;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired(required=false)
	DeviceNotificationDAO deviceNotificationDAO;
	
//	@Autowired
	PushNotificationManager gcmPushNotificationManager;
	
//	@Autowired
	PushNotificationManager apnsPushNotificationManager;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired(required=false)
	MailService mailService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	PersonSearchService personSearchService;
	
	
	private static String START_WITH_REG_EX = "^";

	
//	register deve essere not authenticated, passare username in chiaro,
//	register deve fare update se dopo passi username
	@Override
	public void register(String registrationId, String type) {
		//check if already inserted
		String username = authenticationService.getCurrentLoggedUsername();
		
		if(authenticationService.isAuthenticated()==false)
			username = null;
		
		List<DeviceNotification> ns1 = deviceNotificationDAO.findByRegIdAndTypeAndTenant(registrationId, type, tenantService.getCurrentTenantName());
//		List<DeviceNotification> ns2 = deviceNotificationDAO.findByRegIdAndUsernameAndType(registrationId, username, type);
		if (ns1.size()==0) {
			DeviceNotification deviceNotification = new DeviceNotification();
			deviceNotification.setRegId(registrationId);
			deviceNotification.setUsername(username);
			deviceNotification.setType(type);
			deviceNotification.setTenant(tenantService.getCurrentTenantName());
			
			deviceNotificationDAO.insert(deviceNotification);
			
			log.info("Notification registred with username : " + username + " type : " + type +" tenant : "+tenantService.getCurrentTenantName() + " regId = " + registrationId);
//		} else if(ns1.size()>0 && (ns1.get(0).getUsername() == null || ns1.get(0).getUsername().equals("")) && (username !=null && !username.equals("")) ) { 
//			//update regid with username
//			DeviceNotification dnUpdate = ns1.get(0);
//			dnUpdate.setUsername(username);
//			deviceNotificationDAO.update(dnUpdate);
//			
//		} 
		} else {
//			log.debug("Notification already registred with username : " + username + ", type : " + type +", tenant : "+tenantService.getCurrentTenantName() + ", regId = " + registrationId);
			log.debug("Notification updated with username : " + username + ", type : " + type +", tenant : "+tenantService.getCurrentTenantName() + ", regId = " + registrationId + " and id = " + ns1.get(0).getId());
			//update regid with username
			DeviceNotification dnUpdate = ns1.get(0);
			dnUpdate.setUsername(username);
			deviceNotificationDAO.update(dnUpdate);
		}
	}
	
//	unregister deve essere not authenticated
	@Override
	public void unregister(String registrationId, String type) {
		List<DeviceNotification> ns1 = deviceNotificationDAO.findByRegIdAndTypeAndTenant(registrationId, type, tenantService.getCurrentTenantName());
		if (ns1.size()==0) {
			log.debug("Nothing to unregister");
		}  else if(ns1.size()==1 ) { 
			DeviceNotification dnUpdate = ns1.get(0);
			dnUpdate.setUsername(null);
			deviceNotificationDAO.update(dnUpdate);
		}
	}
	
//	unregister deve essere not authenticated
	@Override
	public void unregisterDevice(String registrationId, String type) {
		log.debug("registrationId : " + registrationId);
		log.debug("type : " + type);
		log.debug("tenantService.getCurrentTenantName() : " + tenantService.getCurrentTenantName());
		List<DeviceNotification> ns1 = deviceNotificationDAO.findByRegIdAndTypeAndTenant(registrationId, type, tenantService.getCurrentTenantName());
		if (ns1.size()==0) {
			log.debug("Nothing to unregister device");
		}  else if(ns1.size()==1 ) { 
			deviceNotificationDAO.delete(registrationId, type);
			log.debug("Device with regid : " + registrationId + " and type : " + type + " unregisterd." );
		}
	}
	
	@Override
	public List<Notification> findAllPaginated(int page, int size) {
		List<Notification> returns= new ArrayList<Notification>();
		Page<Notification> notifications=repository.findAllByTenants(tenantService.getCurrentTenantName(),new PageRequest(page, size));
		Iterable<Notification> notificationsIterator = notifications;
		for (Notification n : notificationsIterator) {
			returns.add(n);			
		}
		return returns;
	}

	@Override
	@Deprecated
	public String sendLikeNotification(String message, String to, String refId) {
		if (!authenticationService.getCurrentLoggedUsername().equals(to)) {
			List<Notification> sameNotifications = this.findByFromAndToAndRefId(authenticationService.getCurrentLoggedUsername(), to, refId);
			if (sameNotifications.size() ==0 ) {
				Notification n = new Notification(authenticationService.getCurrentLoggedUsername(), to, refId, message);
				n.addTenant(tenantService.getCurrentTenantName());
				n.setHistoryVisible(true);
				
				log.debug("Sending notification with message : " +message +" to : " + to + " with refId : " + refId);
				String nId = repository.save(n).getId();
				
				//sent to gcm
				if (gcmPushNotificationManager!=null)
					gcmPushNotificationManager.send(authenticationService.getCurrentLoggedUsername(), to, message, refId, tenantService.getCurrentTenantName());
	//			gcmPushNotificationManager.send(authenticationService.getCurrentLoggedUsername(), to, message, refId);
				
				if(apnsPushNotificationManager!=null)
				apnsPushNotificationManager.send(authenticationService.getCurrentLoggedUsername(), to, message, refId, tenantService.getCurrentTenantName());
	//			apnsPushNotificationManager.send(authenticationService.getCurrentLoggedUsername(), to, message, refId);
				
				try {
				if(mailService!=null) {
					Map parameters = new HashMap();
					parameters.put("refId", refId);
					parameters.put("from", authenticationService.getCurrentLoggedUsername());
					
					mailService.sendQueuedMail(personService.getByUsername(to).getEmail(), parameters, EmailMessageType.LIKE);					
				}
				} catch (Exception e) {
					log.error("Error sending like email to " + to +" for product " + refId, e);
				}
				
				log.info("Notification sent with message : " +message +" to : " + to + " with refId : " + refId);
				return nId;
			} else {
				log.debug("Notification already sent with message : " +message +" to : " + to + " with refId : " + refId);
				return null;
			}
		} else {	
			log.debug("I don't send a notification for a self liked product");
			return null;
		}
	}
	
	
//	@Override
//	public String send(String from, String message, String type, String refId) {
//			
////		TODO
////		check max 240 caracters
//		
//		//salvare??
////		Notification n = new Notification(from, to, refId, message);
////		log.debug("Sending notification with message : " +message +" to : " + to + " with refId : " + refId +" from : " + from);
////		String nId = repository.save(n).getId();
//		
//		//sent to gcm
//		if (gcmPushNotificationManager!=null)
//			gcmPushNotificationManager.sendToAll(from, message, type, refId, tenantService.getCurrentTenantName());
//		
//		if(apnsPushNotificationManager!=null)
//			apnsPushNotificationManager.sendToAll(from, message, type, refId, tenantService.getCurrentTenantName());
//		
////			apnsPushNotificationManager.send(authenticationService.getCurrentLoggedUsername(), to, message, refId);
//		
//		log.info("Notification sent with message : " +message +" with refId : " + refId + " type : " + type);
////		return nId;
//			
//		return null;
//	}
	
	
	
	
	@Override
	public String send(String to, String message, Map<String, String> data, Integer appleBadge, String type, Boolean historyVisible) {
		
		log.info("Sending notification with message : " +message + " type : " + type + " to: " + to +" data: " + data+ " appleBadge: " + appleBadge);
		
//		TODO
//		check max 240 caracters
		
		String from = authenticationService.getCurrentLoggedUsername();
//		async doest't work
		
		//temporaneo...iphone deve settare historyVisible a false nella request...io qui forzo
		if (type.equals("chat")) {	//per le chat non scrivo su db e mando email
				historyVisible=false;
				
				Map parameters = new HashMap<>();
				parameters.put("message", message);
				parameters.put("from", from);
				parameters.put("to", to);
				mailService.sendQueuedMail(null, "andrea.leo@frontiere21.it", null, "Chat message", "chat", parameters, false);
		}
		
		
		saveAsync(from, to,message, type, historyVisible);
		
		
		
		//sent to gcm
		if (gcmPushNotificationManager!=null) {
//			if (to.equals(ALL_DEVICES))
//				gcmPushNotificationManager.sendToAllAsJson(from, message,data, Notification.JSON_TYPE, tenantService.getCurrentTenantName(),appleBadge);
//			else
			
//			 public void sendAsJson(String from, String to, String message,  Map<String,String> data,  Integer appleBadge, String type,String senderId)
				gcmPushNotificationManager.sendAsJson(from, to, message,data, null, type, tenantService.getCurrentTenantName());
		}
		
		if(apnsPushNotificationManager!=null) {
//			if (to.equals(ALL_DEVICES))
//				apnsPushNotificationManager.sendToAll(from, message, type, refId, tenantService.getCurrentTenantName());
//			else
				apnsPushNotificationManager.sendAsJson(from, to, message, data, appleBadge, type, tenantService.getCurrentTenantName());
		}
		
		
		log.info("Notification sent with message : " +message + " type : " + type + " to: " + to +" data: " + data+ " appleBadge: " + appleBadge);
//		return nId;
			
		return null;
	}
	
//	@Async
	private void saveAsync(String from, String to, String message, String type, Boolean historyVisible) {
		//TODO must be async
				Notification n = null;
				List<Notification> notifications = new ArrayList<Notification>();
				if (to.equals(ALL_DEVICES))  {
					try {
						List<Person> people = personSearchService.findByQueryPaginated("*", 0, 10000);
						for (Person person : people) {
							n = new Notification(from, person.getUsername(), null, message);
							n.setType(type);
							n.setHistoryVisible(historyVisible);
							n.addTenant(tenantService.getCurrentTenantName());
							notifications.add(n);
						}
					} catch (ShoppinoException e) {
						log.error("Error finding people", e);
					}
				}else {
					n = new Notification(from, to, null, message);
					n.setType(type);
					n.setHistoryVisible(historyVisible);
					n.addTenant(tenantService.getCurrentTenantName());
					notifications.add(n);
				}
				
				repository.save(notifications);
				
				log.info("Saved notifications on db with from: " + from + " to : " + to + " message: "+ message + "type: " + type + " historyVisible: " + historyVisible);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String send(String message, String type, String to, String cURI, Boolean forceSend) throws TooManyNotificationException {
		return this.send(authenticationService.getCurrentLoggedUsername(), message, type, to, cURI, forceSend);
	}
	
//	@Async
	private void saveAsync(String from, String to, String refId, String message, String type) {
		//TODO must be async
				Notification n = null;
				List<Notification> notifications = new ArrayList<Notification>();
				if (to.equals(ALL_DEVICES))  {
					try {
						List<Person> people = personSearchService.findByQueryPaginated("*", 0, 10000);
						for (Person person : people) {
							n = new Notification(from, person.getUsername(), refId, message);
							n.setType(type);
							n.setHistoryVisible(true);
							n.addTenant(tenantService.getCurrentTenantName());
							notifications.add(n);
						}
					} catch (ShoppinoException e) {
						log.error("Error finding people", e);
					}
				}else {
					n = new Notification(from, to, refId, message);
					n.setType(type);
					n.setHistoryVisible(true);
					n.addTenant(tenantService.getCurrentTenantName());
					notifications.add(n);
				}
				
				repository.save(notifications);
				
				log.info("Saved notifications on db with from: " + from + " to : " + to + " refid:" +refId + " message: "+ message + "type: " + type );
	}
	
	
	private void saveOperation(String from, String to, String refId, String message, String type) {
		//TODO must be async
				Notification n = new Notification(from, to, refId, message);
				n.setType(Notification.OPERATION_TYPE+"/"+type);
//				rivedere qui.. se faccio cosi non salvo il parametro type sul db..perdo informazioni..non va bene 
				n.setHistoryVisible(false);
				n.addTenant(tenantService.getCurrentTenantName());

				repository.save(n);
				
				log.info("Saved notification operation on db with from: " + from + " to : " + to + " refid:" +refId + " message: "+ message + "type: " + type );
	}
	
	private void rejectMultipleSameNotification(String from, String message, String type, String to) throws TooManyNotificationException {
		if (to.equals(ALL_DEVICES)) {
//			http://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
			
		
			
			 Query query = new Query(Criteria.where("from").is(from).and("to").is(to).and("message").is(message).and("tenants").is(tenantService.getCurrentTenantName())
					 .and("type").is(Notification.OPERATION_TYPE+"/"+type).and("createdOn").gte(DateUtil.addHours(-5)));
			 
//			 query.sort().on("createdOn", Order.DESCENDING);
//			 query.limit(50);
			 List<Notification> oldNotifications = operations.find(query, Notification.class);
			 
			 if (oldNotifications.size()>0){
				 throw new TooManyNotificationException("You have already sent a notification with message: " + message + " from: " + from + " to: "+ to + " type: " + type + " in the last 5 hours");
			 }
		}
	}
	
//	@Override
//	@Deprecated
	//usato metedo internamente non è un metodo di notificationservice
	private String send(String from, String message, String type, String to, String refId, Boolean forceSend) throws TooManyNotificationException {
			
		 log.info("Sending notification with message: " + message + " type : " + type + " to : " + to + " refId : "+ refId+ " from: " +from + " forceSend: " +forceSend );
		 
//		TODO
//		check max 240 caracters
		
		//check another notifications already sent
		 if (forceSend!=null && forceSend==false)
			 rejectMultipleSameNotification(from, message, type, to);
		 
//		doest't work
		saveOperation(from, to, refId, message, type);
		saveAsync(from, to, refId, message, type);
		
		//sent to gcm
		if (gcmPushNotificationManager!=null) {
			if (to.equals(ALL_DEVICES)) {
				gcmPushNotificationManager.sendToAll(from, message, type, refId, tenantService.getCurrentTenantName());
			}else if (to.contains(",")) { 
				String[] toMultiple = to.split(",");
				for (String toIesimo : toMultiple) {
					toIesimo = toIesimo.trim();
					gcmPushNotificationManager.send(from, toIesimo, message, type, refId, tenantService.getCurrentTenantName());
				}
			}else {
				gcmPushNotificationManager.send(from, to, message, type, refId, tenantService.getCurrentTenantName());
			}
		}
		
		if(apnsPushNotificationManager!=null) {
			if (to.equals(ALL_DEVICES)) {
				apnsPushNotificationManager.sendToAll(from, message, type, refId, tenantService.getCurrentTenantName());
			}else if (to.contains(",")) { 
				String[] toMultiple = to.split(",");
				for (String toIesimo : toMultiple) {
					toIesimo = toIesimo.trim();
					apnsPushNotificationManager.send(from, toIesimo, message, type, refId, tenantService.getCurrentTenantName());
				}
			}else {
				apnsPushNotificationManager.send(from, to, message, type, refId, tenantService.getCurrentTenantName());
			}
		}
		
		log.info("Notification sent with message : " +message + " with refId : " + refId + " type : " + type + " to: " + to + " from: "+ from);
//		return nId;
			
		return null;
	}
	
	@Override
	public Notification view(String id) throws ResourceNotFoundException {
		Notification n = repository.findOne(id);
		if (n==null)
			throw new ResourceNotFoundException("Notification not found with id : " + id);
		
		setRead(n);
		
		return n;
	}

    private void setRead(Notification notification) {
		Notification returnN = null;
		try {
			returnN = (Notification) notification.clone();
		} catch (CloneNotSupportedException e) {		
			e.printStackTrace();
		}
		
		returnN.setRead(true);
		repository.save(returnN);
	}
    
    private void setReadList(List<Notification> notifications) {
    	for (Notification notification : notifications) {
			setRead(notification);
		}
    	
    }
    
	@Override
	public void delete(String id) throws ShoppinoException {
		repository.delete(id);		
	}
	
	
	@Override
	public List<Notification> findByTo(String to) {
		
//		int skip = page*size;
//		log.debug("skip row : " +skip);
		
//		Query query = new Query(Criteria.where("to").is(to).and("tenants").is(tenantService.getCurrentTenantName()));
		Query query = new Query(Criteria.where("to").is(to).and("tenants").is(tenantService.getCurrentTenantName()).and("historyVisible").is(true));
		query.sort().on("createdOn", Order.DESCENDING);
		query.limit(50);
//		query.skip(skip).limit(size);
		List<Notification> notifications = operations.find(query, Notification.class);
		
		setReadList(notifications);
		
    	return notifications;
    	
	}
	
	
	
	
	@Override
	public List<Notification> findByType(String type,int page, int size) {
		
		int skip = page*size;
		log.debug("skip row : " +skip);
		
//		Query query = new Query(Criteria.where("to").is(to).and("tenants").is(tenantService.getCurrentTenantName()));
		Query query = new Query(Criteria.where("type").regex(START_WITH_REG_EX + type,"i").and("tenants").is(tenantService.getCurrentTenantName()));
//		Query query = new Query(Criteria.where("type").is(type).and("tenants").is(tenantService.getCurrentTenantName()));
		
		query.sort().on("createdOn", Order.DESCENDING);
		query.limit(50);
		query.skip(skip).limit(size);
		List<Notification> notifications = operations.find(query, Notification.class);
		
		
    	return notifications;
    	
	}
	
	
	
	
	@Override
	public long countByType(String type) {
		
		Query query = new Query(Criteria.where("type").regex(START_WITH_REG_EX + type,"i").and("tenants").is(tenantService.getCurrentTenantName()));
		
		return operations.count(query, Notification.class);
		
	}
	
	
//	@Override
	@Deprecated
	public List<Notification> findByFromAndToAndRefId(String from, String to, String refId) {
		
		Query query = new Query(Criteria.where("from").is(from).and("to").is(to).and("refId").is(refId).and("tenants").is(tenantService.getCurrentTenantName()));
		List<Notification> notifications = operations.find(query, Notification.class);
		
		setReadList(notifications);
		
    	return notifications;
    	
	}
	
	
	@Override
	public int hasNew(String username) {
		
		Query query = new Query(Criteria.where("to").is(username).and("read").is(false).and("tenants").is(tenantService.getCurrentTenantName()).and("historyVisible").is(true));
		query.sort().on("createdOn", Order.DESCENDING);
		query.limit(200);
		List<Notification> notifications = operations.find(query, Notification.class);
		
    	return notifications.size();
    	
	}

	@Override
	public void updateRegistration(String oldRegId, String newRegId, String type) {
		deviceNotificationDAO.update(oldRegId, newRegId, type, tenantService.getCurrentTenantName());
	}

	
	
	public void setGcmPushNotificationManager(
			PushNotificationManager gcmPushNotificationManager) {
		this.gcmPushNotificationManager = gcmPushNotificationManager;
	}

	public void setApnsPushNotificationManager(
			PushNotificationManager apnsPushNotificationManager) {
		this.apnsPushNotificationManager = apnsPushNotificationManager;
	}

	
}

	