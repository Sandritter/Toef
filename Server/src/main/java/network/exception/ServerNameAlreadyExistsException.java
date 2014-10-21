package network.exception;

/**
 * Exception which is thrown if the the name of the server already exists
 */
public class ServerNameAlreadyExistsException extends RuntimeException{
	
	private static final long serialVersionUID = -2994867398845471132L;

	public ServerNameAlreadyExistsException() {
		super();
	}
	
	public ServerNameAlreadyExistsException(String s) {
		super(s);
	}
}
