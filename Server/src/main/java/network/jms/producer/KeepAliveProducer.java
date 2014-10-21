package network.jms.producer;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;

/**
 * KeepAliveProducer is sending a keep alive
 */
public class KeepAliveProducer extends Producer{

	private TextMessage txtMessage;
	
	public KeepAliveProducer(String serverName){
		super();
		destinationName = serverName + MessageTopic.KEEPALIVETOPIC.toString();		
		init(false); 
	}
	
	public void send(String message){
		try {
			txtMessage = session.createTextMessage();
			messageProducer.send(txtMessage);
		} catch (JMSException e) {
			e.printStackTrace();		
			}
	}
}
