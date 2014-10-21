package network.exception;

/**
 * Exception that gets thrown when the client name already exists
 */
public class UsernameAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8258855584720247671L;

	public UsernameAlreadyExistsException(String s) {
		super(s);
	}

}
