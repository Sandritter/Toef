package network.jms.requestor;

import java.util.Observable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import persistence.Repository;

/**
 * Superclass for Requestors
 */
public abstract class Requestor extends Observable implements MessageListener{
	
	protected int ackMode;
	protected String messageQueueName;
	protected Session session;
	protected boolean transacted = false;
	protected MessageProducer replyProducer;
	protected ActiveMQConnectionFactory connectionFactory;
	protected Connection connection;
	protected Destination destination;
	protected MessageConsumer consumer;
	
	/**
	 * Constructor
	 */
	public Requestor(){
		
	}
	
	protected void init(){
		ackMode = Session.AUTO_ACKNOWLEDGE;
		connectionFactory = new ActiveMQConnectionFactory(Repository.getInstance().getProperties("network").getProperty("broker-Url"));
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			this.session = connection.createSession(this.transacted, ackMode);
			destination = this.session.createQueue(messageQueueName);

			// Setup a message producer to respond to messages from clients, we
			// will get the destination
			// to send to from the JMSReplyTo header field from a Message
			this.replyProducer = this.session.createProducer(null);
			this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Set up a consumer to consume messages off of the clientConnection queue
			consumer = this.session.createConsumer(destination);
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			System.err.println("Das erstellen des Producer und Consumers ist fehlgeschlagen!");
			e.printStackTrace();
		}
	}

	public abstract void onMessage(Message message);
	
	public void close()
	{
		try {
			consumer.close();
			replyProducer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
