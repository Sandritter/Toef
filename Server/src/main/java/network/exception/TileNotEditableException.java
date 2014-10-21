package network.exception;

/**
 * Exception is appears if a tile is not editable
 */
public class TileNotEditableException extends RuntimeException{
	
	private static final long serialVersionUID = -2278960746653638246L;

	public TileNotEditableException(String s) {
		super(s);
	}
}
