package network.enums;

/**
 * enum used to define the property names of the message headers
 */
public enum PropertyNames {
	SIMULATIONNAME("SIMULATIONNAME"),
	EDITMODE("EDITMODE"),
	IPADDRESS("IPADDRESS"),
	USERNAME("USERNAME"),
	PARTICIPANTID("PARTICIPANTID"),
	CLIENTLIST("CLIENTLIST")
	;
	private PropertyNames(final String PROPERTYNAMES){
		this.PROPERTYNAMES = PROPERTYNAMES;
	}
	
	private final String PROPERTYNAMES;
	
	public String toString(){
		return PROPERTYNAMES;
	}
}
