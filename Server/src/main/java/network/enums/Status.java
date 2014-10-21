package network.enums;

/**
 * enum used to describe different states
 */
public enum Status {
	OK("OK"),
    ERROR("ERROR"),
    IPERROR("IPERROR"),
    USERNAMEERROR("USERNAMEERROR"),
    WAIT("WAIT")
    ;
	
	private Status(final String text) {
        this.text = text;
    }
	
	private final String text;
	
	@Override
    public String toString() {
        return text;
    }
}
