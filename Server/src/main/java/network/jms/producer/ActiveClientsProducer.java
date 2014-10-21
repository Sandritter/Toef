package network.jms.producer;

import java.util.Observable;
import java.util.Observer;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import network.enums.PropertyNames;

/**
 * ActiveClientsProducer sends a list of all active clients to a client
 */
public class ActiveClientsProducer extends Producer implements Observer{
	
	/**
	 * Constructor
	 * @param serverName
	 */
	public ActiveClientsProducer(String serverName) {
		destinationName = serverName + MessageTopic.CLIENTLISTPRODUCER.toString();
		init(false);
	}
	
	public void send(String message){
		try {
			TextMessage textMsg = session.createTextMessage();
			textMsg.setStringProperty(PropertyNames.CLIENTLIST.toString(), message);
			messageProducer.send(textMsg);
		} catch(JMSException e){
			System.err.println("Fehler beim Aktualisieren der Client Liste");
			e.printStackTrace();
		}
	}

	/**
	 * Gets notified when client logs in / logs out
	 */
	public void update(Observable observ, Object o) {
		if (o instanceof String){
			String userNames = (String)o;
			send(userNames);
		}
	}
}

