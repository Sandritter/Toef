package network.jms.consumer;

import javax.jms.Message;

import network.enums.MessageTopic;

/**
 * Consumer which receives a participant from the client
 */
public class ParticipantConsumer extends Consumer {

	/**
	 * constructor
	 * 
	 * @param serverName
	 */
	public ParticipantConsumer(String serverName) {
		super();
		destinationName = serverName + MessageTopic.PLACEDCARCONSUMER.toString();
		init(true);
	}

	/**
	 * listener-method which is called when a message was received
	 */
	public void onMessage(Message message) {
		setChanged();
		// the SimulationServer is getting notified, so it can pass it to the
		// TrafficParticipantController
		notifyObservers(message);
	}

}
