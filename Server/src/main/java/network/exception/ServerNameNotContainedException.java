package network.exception;

/**
 * Exception is thrown when a client wants to join to a server that in mean time does not exist anymore
 */
public class ServerNameNotContainedException extends RuntimeException{
	
	private static final long serialVersionUID = 2040003842925691684L;


	public ServerNameNotContainedException() {
		super();
	}
	
	public ServerNameNotContainedException(String s) {
		super(s);
	}
}