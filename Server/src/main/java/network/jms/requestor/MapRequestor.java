package network.jms.requestor;


import interLayer.converters.EntitiyParser;
import interLayer.converters.JSONConverter;
import interLayer.entities.ViewTile;

import java.util.concurrent.Callable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import simulation.control.Simulation;
import simulation.control.interfaces.ISimulation;
import simulation.control.map.interfaces.IMapController;
import simulation.tiles.interfaces.ITile;

/**
 * this class is called by the ClientConnectionConsumer after a Client was registered successfully
 * it sends a copy of the current map to the client using a queue
 */
public class MapRequestor extends Requestor{
	
	private ISimulation simulation;
	private JSONConverter jsonConverter;
	private EntitiyParser tileParser;
	
	/**
	 * constructor
	 * @param serverName
	 * @param simulation
	 */
	public MapRequestor(String serverName, ISimulation simulation){
		super();
		messageQueueName = serverName + MessageTopic.MAPPRODUCER.toString();
		ackMode = Session.AUTO_ACKNOWLEDGE;
		this.simulation = simulation;
		this.jsonConverter = new JSONConverter();
		this.tileParser = new EntitiyParser();
		
		init();
	}

	/**
	 * is called when the MessageConsumer receives the request from the client to send back the map
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage){
			TextMessage textMsg = (TextMessage) message;
			TextMessage response;
			try {
				final IMapController mapController = simulation.getMapController();
				// get a copy of the current map
				ITile[][] map = simulation.enqueue(new Callable<ITile[][]>(){
					public ITile[][] call() throws Exception {
						return mapController.getMapCopy();
					}
				});
				
				// create a Json-string from a ViewTile[][]
				ViewTile[][] viewTileMap = tileParser.tileToView(map);
				String msgBody = jsonConverter.convertViewTileArrayToString(viewTileMap);
				
				// create a response-Message and send it back to the client
				response = session.createTextMessage();
				response.setText(msgBody);
				response.setJMSCorrelationID(textMsg.getJMSCorrelationID());
				this.replyProducer.send(textMsg.getJMSReplyTo(), response);
			} catch (JMSException e) {
				System.err.println("Fehler beim Versenden der Response-Nachricht während dem Registrieren eines Clients!");
				e.printStackTrace();
			}

		}
		
	}

}