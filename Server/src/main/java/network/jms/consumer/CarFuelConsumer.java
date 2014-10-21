package network.jms.consumer;

import javax.jms.Message;
import javax.jms.TextMessage;

import network.enums.MessageTopic;

/**
 * CarFuelConsumer is a consumer which receives a client side information to fill the tank of a vehicle
 */
public class CarFuelConsumer extends Consumer {

	/**
	 * Constructor
	 * @param serverName
	 */
	public CarFuelConsumer(String serverName){
		super();
		destinationName = serverName + MessageTopic.CARFUELCONSUMER.toString();
		init(true);
	}
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage){
			TextMessage textMsg = (TextMessage) message;
			setChanged();
			notifyObservers(textMsg);
		}
	}

}
