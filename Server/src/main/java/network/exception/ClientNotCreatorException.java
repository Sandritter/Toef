package network.exception;

/**
 * Exception which appears if a Client can not be created
 */
public class ClientNotCreatorException extends RuntimeException{

	private static final long serialVersionUID = -69297403714397438L;

	public ClientNotCreatorException() {
		super();
	}
	
	public ClientNotCreatorException(String s) {
		super(s);
	}
}
