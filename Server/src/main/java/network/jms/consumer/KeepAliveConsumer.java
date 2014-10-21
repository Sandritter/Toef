package network.jms.consumer;

import interLayer.logic.interfaces.IClientManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;

import network.enums.MessageTopic;
import network.enums.PropertyNames;

/**
 * Checks whether a Client is Down or Alive in regular interval
 */
public class KeepAliveConsumer extends Consumer {

	private Timer timer; // Previous time an answer arrived
	private final int SECONDS = 10;
	private IClientManager clientManager;
	private Map<String, Integer> aliveClients;
	private String ip;
	private static final Logger log = Logger.getLogger(KeepAliveConsumer.class.getName());
	private final int STARTPING = 2; //Starting ping for clients
	private final int REMOVEPING = 0; //Fixed ping for removing clients
	private Map<String, Integer> newMap;
	
	/**
	 * Constructor
	 * @param serverName
	 * @param clientManager
	 */
	public KeepAliveConsumer(String serverName, IClientManager clientManager) {
		destinationName = serverName + MessageTopic.KEEPALIVEQUEUE.toString();
		this.clientManager = clientManager;
		aliveClients = new HashMap<String, Integer>();
		init(true);
	}

	/**
	 * If message from client is recieved his ip adress is added to array
	 */
	@Override
	public void onMessage(Message message) {
		try {
			ip = message.getStringProperty(PropertyNames.IPADDRESS.toString());
			aliveClients.put(ip, STARTPING); //If the client answered his ping is reseted
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes Clients if they didnt answer
	 */
	public synchronized void checkClients() {
		newMap = new HashMap<String, Integer>();
		for (String actIp : aliveClients.keySet()) {
			int value = aliveClients.get(actIp) - 1; //Ping is reduced
			aliveClients.put(actIp, value);
			if (aliveClients.get(actIp) <= REMOVEPING) { //If client did not answer for certain time 
				log.log(Level.INFO, "Client is removed: " + actIp);
				clientManager.removeClient(actIp); //he is removed
			} else {
				newMap.put(actIp, value); //if the answererd they can be saved
			}
		}
		aliveClients = newMap;
	}
}
