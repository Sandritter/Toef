package network.jms;

import org.apache.activemq.broker.BrokerService;

import persistence.Repository;

/**
 * creates an embedded MessageBroker on the server
 */
public class MessageBroker {
	
	private static String messageBrokerUrl = Repository.getInstance().getProperties("network").getProperty("broker-Url");
	
	/**
	 * constructor
	 */
	public MessageBroker(){
		try {
			BrokerService broker = new BrokerService();
			broker.setPersistent(false);
			broker.setUseJmx(false);
			broker.addConnector(messageBrokerUrl);
			broker.start();
		} catch (Exception e) {
			// Handle the exception appropriately
		}
	}
	
}
