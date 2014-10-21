package network.jms.consumer;

import javax.jms.Message;
import javax.jms.TextMessage;

import network.enums.MessageTopic;

/**
 * ChatMessage Consumer is a Consumer which receives a chatMessage from a client and notifies the simulationServer
 */
public class ChatMessageConsumer extends Consumer {

	/**
	 * Constructor
	 * @param serverName
	 */
	public ChatMessageConsumer(String serverName) {
		super();
		destinationName = serverName + MessageTopic.CHATMESSAGECONSUMER.toString();
		init(true);
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			// the ChatMessageProsucer is getting notified
			setChanged();
			notifyObservers(message);
		}
	}
}
