package network.enums;

/**
 * enums used for messaging to identify the diffrent queues and topics
 */
public enum MessageTopic {
	
	CLIENTCONNECTIONCONSUMER("_clientconnection_queue"),
	MAPPRODUCER("_map_queue"),
	VIEWTILEPRODUCER("_viewTile_topic"),
	VIEWTILECONSUMER("_viewTile_queue"),
	CLIENTPOSITIONCONSUMER("_position_queue"),
	LOGOUTCONSUMER("_logout_queue"),
	HOSTDOWNPRODUCER("_hostDown_topic"),
	PLACEDCARCONSUMER("_placeViewCar_queue"),
	VIEWCARUPDATEPRODUCER("_viewCarUpdate_queue"),
	DELETEPARTICIPANT("_deleteParticipant_queue"),
	CHATMESSAGECONSUMER("_chatMessage_queue"),
	KEEPALIVETOPIC("_keepAlive_topic"),
	KEEPALIVEQUEUE("_keepAlive_queue"),
	CHATMESSAGEPRODUCER("_chatMessage_topic"),
	CLIENTLISTPRODUCER("_clientList_topic"),
	CARFUELCONSUMER("_carFuelInfo_queue")

	;
	private MessageTopic(final String MESSAGETOPIC){
		this.MESSAGETOPIC = MESSAGETOPIC;
	}
	
	private final String MESSAGETOPIC;
	
	public String toString(){
		return MESSAGETOPIC;
	}
}