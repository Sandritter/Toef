package network.jms.consumer;

import javax.jms.Message;
import javax.jms.TextMessage;

import network.enums.MessageTopic;

/**
 * 
 * is receiving messages (tile changes) from clients asynchronously and notifies
 * the simulation server which is handling the message
 * 
 */
public class ViewTileConsumer extends Consumer {

	/**
	 * constructor
	 * 
	 * @param serverName
	 *            - name of the simulation server used to specify the queue name
	 */
	public ViewTileConsumer(String serverName) {
		super();
		destinationName = serverName + MessageTopic.VIEWTILECONSUMER.toString();
		init(true);
	}

	/**
	 * is notified from the broker when a message is coming in and notifies the
	 * simulation server afterwards
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			// the SimulationServer is getting notified, so it can pass it to
			// the MapController
			setChanged();
			notifyObservers(message);
		}
	}
}
