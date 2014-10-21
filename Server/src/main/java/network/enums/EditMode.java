package network.enums;

/**
 * enums used to define the edit mode
 */
public enum EditMode {
	SET("SET"),
	DELETE("DELETE")
	;
	private EditMode(final String EDITMODE){
		this.EDITMODE = EDITMODE;
	}
	
	private final String EDITMODE;
	
	public String toString(){
		return EDITMODE;
	}
}
