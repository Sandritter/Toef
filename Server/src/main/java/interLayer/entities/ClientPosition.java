package interLayer.entities;

/**
 * Representation the position of a client
 */
public class ClientPosition {

	private int bottomLeftX;
	private int bottomLeftY;
	private int topRightX;
	private int topRightY;
	
	/**
	 * Constructor
	 */
	public ClientPosition(){
		
	}
	
	/**
	 * Constructor
	 * @param bottomLeftX
	 * @param bottomLeftY
	 * @param topRightX
	 * @param topRightY
	 */
	public ClientPosition(int bottomLeftX, int bottomLeftY, int topRightX, int topRightY){
		this.bottomLeftX = bottomLeftX;
		this.bottomLeftY = bottomLeftY;
		this.topRightX = topRightX;
		this.topRightY = topRightY;
	}

	public int getBottomLeftX() {
		return bottomLeftX;
	}

	public void setBottomLeftX(int bottomLeftX) {
		this.bottomLeftX = bottomLeftX;
	}

	public int getBottomLeftY() {
		return bottomLeftY;
	}

	public void setBottomLeftY(int bottomLeftY) {
		this.bottomLeftY = bottomLeftY;
	}

	public int getTopRightX() {
		return topRightX;
	}

	public void setTopRightX(int topRightX) {
		this.topRightX = topRightX;
	}

	public int getTopRightY() {
		return topRightY;
	}

	public void setTopRightY(int topRightY) {
		this.topRightY = topRightY;
	}	
}
