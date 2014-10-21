package interLayer.entities;

import java.io.Serializable;

import simulation.tiles.enums.TileType;

/**
 * Containerbean for the map data which is necessary for the clients
 * 
 */
public class ViewTile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4399443611480414300L;

	private TileType type;
	
	private float rot;
	
	private int posX;
	
	private int posY;
	
	public ViewTile()
	{
		
	}
	
	/**
	 * Constructor
	 * @param type
	 * @param posX
	 * @param posY
	 * @param rot
	 */
	public ViewTile(TileType type, int posX, int posY, float rot){
		this.type = type;
		this.posX = posX;
		this.posY = posY;
		this.rot = rot;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	
}
