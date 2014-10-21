package network.enums;

/**
 * enums used for the client connection
 */
public enum ClientConnectionKeys {
	IP("IP"),
	USERNAME("USERNAME"),
	SIMULATIONNAME("SIMULATIONNAME")
	;
	
	private ClientConnectionKeys(final String CONNECTIONKEY){
		this.CONNECTIONKEY = CONNECTIONKEY;
	}
	
	private final String CONNECTIONKEY;
	
	public String toString(){
		return CONNECTIONKEY;
	}
}