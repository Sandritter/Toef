package network.jms.producer;

import java.util.Observable;
import java.util.Observer;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import network.enums.PropertyNames;
import network.jms.consumer.ChatMessageConsumer;

/**
 * ChatMessageProducer is sending broadcasts to all clients of a server serving chat messages
 */
public class ChatMessageProducer extends Producer implements Observer{
	
	/**
	 * 
	 * @param serverName
	 */
	public ChatMessageProducer(String serverName) {
		destinationName = serverName + MessageTopic.CHATMESSAGEPRODUCER.toString();
		init(false);
	}
	
	/**
	 * 
	 * @param username
	 * @param message
	 * @throws JMSException
	 */
	private void sendChatMessage(String username, String message) throws JMSException {
		try {
			TextMessage textMsg = session.createTextMessage();
			textMsg.setText(message);
			textMsg.setStringProperty(PropertyNames.USERNAME.toString(), username);
			messageProducer.send(textMsg);
		} catch(JMSException e){
			System.err.println("Fehler beim Update der Kacheln vom SimulationsServer ");
			e.printStackTrace();
		}
	}

	/**
	 * once a new chat message was updated the sendChatMessage is called
	 */
	public void update(Observable observ, Object o) {
		if(observ instanceof ChatMessageConsumer){
			if (o instanceof TextMessage){
				TextMessage message = (TextMessage) o;
				try {
					sendChatMessage(message.getStringProperty(PropertyNames.USERNAME.toString()), message.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void send(String message) {
		throw new UnsupportedOperationException();
	}
}
