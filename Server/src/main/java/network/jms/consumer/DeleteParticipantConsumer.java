package network.jms.consumer;

import javax.jms.Message;

import network.enums.MessageTopic;

/**
 * DeleteParticipantConsumer is a Consumer which receives information from clients which are disconnecting from the simulationServer
 */
public class DeleteParticipantConsumer extends Consumer {

	/**
	 * Constructor
	 * @param serverName
	 */
	public DeleteParticipantConsumer(String serverName) {
		super();
		destinationName = serverName + MessageTopic.DELETEPARTICIPANT.toString();
		init(true);
	}
	
	public void onMessage(Message message){
		setChanged();
		notifyObservers(message);
	}

}
