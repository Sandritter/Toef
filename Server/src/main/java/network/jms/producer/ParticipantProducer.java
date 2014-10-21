package network.jms.producer;

import interLayer.converters.JSONConverter;
import interLayer.entities.Client;
import interLayer.entities.UpdateParticipant;
import interLayer.logic.interfaces.IClientManager;
import interLayer.validators.ClientViewValidator;
import interLayer.validators.interfaces.IClientViewValidator;

import java.util.HashMap;
import java.util.List;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import simulation.control.events.ParticipantsUpdatedEvent;
import simulation.control.events.interfaces.ParticipantUpdatedListener;

/**
 * send all participants from a clients view to this client
 */
public class ParticipantProducer extends Producer implements ParticipantUpdatedListener{
	
	private JSONConverter jsonConverter;
	private IClientManager clientManager;
	private IClientViewValidator clientViewValidator;
	private String serverName;
	private String ip;
	
	/**
	 * Constructor
	 * @param clientManager
	 * @param serverName
	 */
	public ParticipantProducer(IClientManager clientManager, String serverName){
		super();
		this.serverName = serverName;
		this.clientManager = clientManager;
		this.clientViewValidator = new ClientViewValidator();
		this.jsonConverter = new JSONConverter();
		init();
	}
	
	
	protected void init(){
		try {
			connection = connectionFactory.createConnection();
			connection.start();		
			session = connection.createSession(transacted, ackMode);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sending an array with all participants situated on a clients view to the client
	 * @throws JMSException
	 */
	public void send(String message){
		String messageQueueName = ip + "_" + serverName + MessageTopic.VIEWCARUPDATEPRODUCER.toString();
		try {
			destination = session.createQueue(messageQueueName);
			messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMsg = session.createTextMessage();
			textMsg.setText(message);
			messageProducer.send(textMsg);
		} catch (JMSException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				messageProducer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * listener-method that listens to an event from the TrafficParticipantController
	 * @param e - ParticipantsUpdatedEvent
	 */
	public void listen(ParticipantsUpdatedEvent e) {
		HashMap<String, Client> clientMap = (HashMap<String, Client>)clientManager.getClients();
		for (String ip : clientMap.keySet()){
			this.ip = ip;
			// validator which returns a list of all participants visible in a clients view
			List<UpdateParticipant> list = clientViewValidator.validate(clientMap.get(ip), e.getParticipants());
			UpdateParticipant[] participants = list.toArray(new UpdateParticipant[list.size()]);
			send(jsonConverter.convertViewCarArraylistToString(participants));
		}
	}
}
