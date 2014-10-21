package network.jms.consumer;
import interLayer.logic.interfaces.IClientManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import network.enums.PropertyNames;

/**
 * this class receives messages from clients, which like to disconnect from the given SimulationServer
 * and calls methods from the ClientManager to remove this client from the list
 */
public class ClientDisconnectConsumer extends Consumer{

	private IClientManager clientManager;
	
	/**
	 * constructor
	 * @param serverName
	 * @param clientManager
	 */
	public ClientDisconnectConsumer(String serverName, IClientManager clientManager){
		super();
		destinationName = serverName + MessageTopic.LOGOUTCONSUMER.toString();
		this.clientManager = clientManager;
		init(true);
	}

	/**
	 * when a logout message was received from a client
	 * the client will be deleted from the clientList
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage){
			TextMessage textMsg = (TextMessage) message;
			try {
				// get the ip address from the header info of the message
				String ipAddress = textMsg.getStringProperty(PropertyNames.IPADDRESS.toString());
				clientManager.removeClient(ipAddress);
			} catch (JMSException e){
				e.printStackTrace();
			}
			
		}
		
	}
}
