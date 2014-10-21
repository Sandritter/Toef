package simulation.participants.enums;

import java.io.Serializable;


/**
 * Participant types
 */
public enum ParticipantType implements Serializable{
	NONE("NONE"), 
	// Car types
	CABRIOPINK("CABRIOPINK"),
	CABRIOBLUE("CABRIOBLUE"),
	CABRIOGREEN("CABRIOGREEN"),
	CABRIOSILVER("CABRIOSILVER"),
	CABRIOYELLOW("CABRIOYELLOW"),
	LIMOBLUE("LIMOBLUE"),	
	//Ship types
	BOAT("BOAT"),
	KANU("KANU");
	
	private String text;
	
	private ParticipantType(String text){
		this.text = text;
	}
	
	public ParticipantUpperType getUpperType() {
		if(this == ParticipantType.NONE){
			return ParticipantUpperType.NONE;
		}
		
		if (this == ParticipantType.CABRIOPINK || this == ParticipantType.CABRIOBLUE || this == ParticipantType.CABRIOGREEN ||
				this == ParticipantType.CABRIOSILVER || this == ParticipantType.CABRIOYELLOW || this == ParticipantType.LIMOBLUE){
			return ParticipantUpperType.CAR;
		}
		
		return ParticipantUpperType.SHIP;
	}
	
	public String getText(){
		return text;
	}
}
