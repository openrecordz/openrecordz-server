/*
 * Copyright 2010 shareway
 * Lorenzo
 */
package openrecordz.queue;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author Lorenzo
 *
 */


public class NotificationListener implements MessageListener {

	private static final Log log = LogFactory.getLog(NotificationListener.class);



	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			log.info("Got PushNotification from queue...");
			System.out.println("PushNotification: " + message);
			String messageType=null;
			try {
				messageType = ((MapMessage)message).getString("type");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PushNotification type: " + messageType);
		}
		else
		{
			throw new IllegalArgumentException("Message must be of type TextMessage");
		}
	}

}

