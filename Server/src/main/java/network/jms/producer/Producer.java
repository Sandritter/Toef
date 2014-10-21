package network.jms.producer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import network.jms.interfaces.IProducer;

import org.apache.activemq.ActiveMQConnectionFactory;

import persistence.Repository;

/**
 * Superclass for messageproducers
 */
public abstract class Producer implements IProducer{

	protected ActiveMQConnectionFactory connectionFactory;
	protected Connection connection;
	protected Session session;
	protected MessageProducer messageProducer;
	protected Destination destination;
	protected int ackMode;
	protected String destinationName;
	protected boolean transacted = false;
	
	public Producer(){
		ackMode = Session.AUTO_ACKNOWLEDGE;
		connectionFactory = new ActiveMQConnectionFactory(Repository.getInstance().getProperties("network").getProperty("broker-Url"));
	}
	
	/**
	 * Initialises connection to Messagebroker
	 */
	protected void init(boolean queue){
		try {
			connection = connectionFactory.createConnection();
			connection.start();			
			this.session = connection.createSession(transacted, ackMode);
			if(queue){
				destination = session.createQueue(destinationName);
			}else{
				destination = session.createTopic(destinationName);				
			}
			messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			System.err.println("Fehler: Der Verbindungsaufbau zum MessageBroker ist fehlgeschlagen!");
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if(messageProducer!=null && session != null && connection != null) {
				messageProducer.close();
				session.close();
				connection.close();				
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public abstract void send(String message);
	
	
}
