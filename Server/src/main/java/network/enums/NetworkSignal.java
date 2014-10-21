package network.enums;

/**
 * enum used for different network signals to define the state of network activities
 */
public enum NetworkSignal {
	
	CLIENTCONNECT_SUCCSESSFULL("000"),
	USERNAME_ALREADY_EXISTS("101"),
	SERVERNAME_NOT_EXISTS("103"),
	SERVERNAME_ALREADY_EXISTS("104"),
	CLIENT_NOT_HOST("105"),
	ACTION_SUCCESSFUL("200"),
	;
	private NetworkSignal(final String NETWORKSIGNAL){
		this.NETWORKSIGNAL = NETWORKSIGNAL;
	}
	
	private final String NETWORKSIGNAL;
	
	public String toString(){
		return NETWORKSIGNAL;
	}
}
