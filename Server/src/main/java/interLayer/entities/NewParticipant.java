package interLayer.entities;

import java.io.Serializable;

import simulation.participants.enums.ParticipantType;

/**
 * Representation a new Participant
 */
public class NewParticipant implements Serializable{
	
	private static final long serialVersionUID = -3736018399393939512L;

	private ParticipantType type;
	private int tilePosX;
	private int tilePosY;
	private float rot;
	
	public NewParticipant(){
		
	}
	
	/**
	 * Constructor
	 * @param type
	 * @param tilePosX
	 * @param tilePosY
	 * @param rot
	 */
	public NewParticipant(ParticipantType type, int tilePosX, int tilePosY, float rot){
		setType(type);
		setTilePosX(tilePosX);
		setTilePosY(tilePosY);
		setRot(rot);
	}

	public ParticipantType getType() {
		return type;
	}

	public void setType(ParticipantType type) {
		this.type = type;
	}

	public int getTilePosX() {
		return tilePosX;
	}

	public void setTilePosX(int tilePosX) {
		this.tilePosX = tilePosX;
	}

	public int getTilePosY() {
		return tilePosY;
	}

	public void setTilePosY(int tilePosY) {
		this.tilePosY = tilePosY;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}

}
