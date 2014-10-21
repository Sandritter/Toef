package network.jms.producer;

import interLayer.converters.EntitiyParser;
import interLayer.converters.JSONConverter;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.MessageTopic;
import simulation.control.events.TileAddedEvent;
import simulation.control.events.interfaces.TileAddedListener;

/**
 * 
 * is sending an update of the changes made on the map to all clients using this map
 * is listening to a MapController, when it receives a call it sends the message
 */
public class ViewTileProducer extends Producer implements TileAddedListener{
	
	private JSONConverter jsonConverter;
	private EntitiyParser tileParser;
	
	/**
	 * constructor
	 * @param serverName - name of the simulation server 
	 */
	public ViewTileProducer(String serverName){
		super();
		destinationName = serverName + MessageTopic.VIEWTILEPRODUCER.toString();
		jsonConverter = new JSONConverter();
		tileParser = new EntitiyParser();
		init(false);
	}
	
	/**
	 * sends a view tile to all clients which are subscribed to this simulation server
	 * @param tile tile which holds the changes
	 * @throws JMSException
	 */
	public void send(String message){
		try {
			messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMsg = session.createTextMessage();
			textMsg.setText(message);
			messageProducer.send(textMsg);
		} catch(JMSException e){
			System.err.println("Fehler beim Update der Kacheln vom SimulationsServer ");
			e.printStackTrace();
		} finally {
			try {
				messageProducer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * is listening to the MapController
	 * calls the send-Message when an event is coming in
	 */
	public void listen(TileAddedEvent e) {
		send(jsonConverter.convertViewTileToString(tileParser.tileToViewTile(e.getTile())));
	}
	
	
}
