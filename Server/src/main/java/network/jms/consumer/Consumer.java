package network.jms.consumer;

import java.util.Observable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import network.jms.interfaces.IConsumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import persistence.Repository;

/**
 * Superclass for messageconsumers
 */
public abstract class Consumer extends Observable implements MessageListener, IConsumer {

	protected ActiveMQConnectionFactory connectionFactory;
	protected Connection connection;
	protected Session session;
	protected MessageConsumer messageConsumer;
	protected Destination destination;
	protected int ackMode;
	protected String destinationName;
	protected boolean transacted = false;

	public Consumer() {
		ackMode = Session.AUTO_ACKNOWLEDGE;
		connectionFactory = new ActiveMQConnectionFactory(Repository.getInstance().getProperties("network").getProperty("broker-Url"));
	}


	/**
	 * Initialises connection to Messagebroker
	 */
	protected void init(boolean queue) {
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(transacted, ackMode);
			if(queue){
				destination = session.createQueue(destinationName);				
			}else{
				destination = session.createTopic(destinationName);
			}
			messageConsumer = session.createConsumer(destination);
			messageConsumer.setMessageListener(this);
		} catch (JMSException e) {
			System.err.println("Verbindung konnte nicht aufgebaut werden: ");
			e.printStackTrace();
		}
	}

	/**
	 * callback method for incoming messages
	 */
	public abstract void onMessage(Message message);
	
	public void close() {
		try {
			messageConsumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
