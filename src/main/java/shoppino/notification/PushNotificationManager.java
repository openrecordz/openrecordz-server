//package shoppino.notification;
//
//import java.util.Map;
//
//
//public interface PushNotificationManager {
//
//	@Deprecated
//	public void send(String from, String to, String message, String refId);
//	
//	@Deprecated
//	public void send(String from, String to, String message, String refId, String senderId);
//	
//	
//	public void send(String from, String to, String message, String type, String refId, String senderId);
//	
//	public void sendToAll(String from, String message, String type, String refId, String senderId);
//	
//	public void clean();
//	
//	
//	
////	 public void sendToAllAsJson(String from, String message, Map<String,String> data, String type, String senderId);
//		
//	public void sendAsJson(String from, String to, String message,  Map<String,String> data,  Integer appleBadge, String type,String senderId);
//}
