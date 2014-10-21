package network.exception;

/**
 * Exception that gets thrown when the client IP already exists
 */
public class IPAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1761984756064746482L;

	public IPAlreadyExistsException(String s) {
		super(s);
	}

}
