package network.jms.consumer;

import interLayer.converters.JSONConverter;
import interLayer.entities.ClientPosition;
import interLayer.logic.interfaces.IClientManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import network.enums.PropertyNames;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;

/**
 * receives the client position from every client joined to the SimulationsServer with the given serverName
 * it's passing the message information from the client to the clientManager
 */
public class ClientPositionConsumer extends Consumer {
	
	private IClientManager clientManager;
	private JSONConverter jsonConverter;
	
	/**
	 * constructor
	 * @param serverName - name of the SimulationServer
	 * @param clientManager - ClientManager which holds the clients joined to the SimulationServer
	 */
	public ClientPositionConsumer(String serverName, IClientManager clientManager){
		super();
		destinationName = serverName + MessageTopic.CLIENTPOSITIONCONSUMER.toString();
		this.clientManager = clientManager;
		jsonConverter = new JSONConverter();
		init(true);
	}
	
	/**
	 * 
	 * is a MessageListener which is called when a client sends it's position
	 * passes the message information to the clientManager
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage){
			TextMessage textMsg = (TextMessage) message;
			try {
				String ipAddress = textMsg.getStringProperty(PropertyNames.IPADDRESS.toString());
				ClientPosition cp = jsonConverter.convertStringToClientPosition(textMsg.getText());
				IVector bottomLeft = new Vector(cp.getBottomLeftX(), cp.getBottomLeftY());
				IVector upperRight = new Vector(cp.getTopRightX(), cp.getTopRightY());
				clientManager.updatePosition(ipAddress, bottomLeft, upperRight);
			} catch (JMSException e){
				e.printStackTrace();
			}
		}
		
	}

}
