package network.jms.producer;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;

/**
 * The HostDownProducer sends a message to all clients joined to the given SimulationServer 
 * informing them about the shutDown of the Server
 */
public class HostDownProducer extends Producer{
	
	/**
	 * constructor
	 */
	public HostDownProducer(){
		super();
		init();
	}
	
	protected void init(){
		try {
			connection = connectionFactory.createConnection();
			connection.start();			
			this.session = connection.createSession(transacted, ackMode);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * is sending an emtpy message via broadcast to all clients to inform them about the SimulationServer-Shutdown
	 * @param serverName
	 * @throws JMSException 
	 */
	public void send(String serverName){
		
		destinationName = serverName + MessageTopic.HOSTDOWNPRODUCER.toString();
		try {
			destination = session.createTopic(destinationName);
			messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage message;
			message = session.createTextMessage();
			messageProducer.send(message);
		} catch (JMSException e) {
			System.err.println("FEHLER beim Senden the Message an den Client. Betreffend SimulationsServer: " + serverName);
			e.printStackTrace();
		} finally {
			try {
				messageProducer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
