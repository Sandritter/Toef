package simulation.participants.enums;

/**
 * Participant upper types
 */
public enum ParticipantUpperType {
	NONE("NONE"),
	CAR("CAR"),
	SHIP("SHIP");
	
	private String text;
	
	private ParticipantUpperType(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
}
