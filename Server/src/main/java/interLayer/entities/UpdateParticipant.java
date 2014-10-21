package interLayer.entities;

import java.io.Serializable;
import java.util.Map;

import simulation.participants.enums.ParticipantType;

/**
 * ContainerBean, which is used to carry the specific car information
 *
 */
public class UpdateParticipant implements Serializable{

	private static final long serialVersionUID = -8709017032587583705L;
	private ParticipantType type;
	private int id;
	private float relPosX;
	private float relPosY;
	private float rot;
	private Map<String,String> optInfos;
	
	public UpdateParticipant(){
	}
	
	/**
	 * Constructor
	 * @param type
	 * @param id
	 * @param posX
	 * @param posY
	 * @param rot
	 * @param optInfos
	 */
	public UpdateParticipant(ParticipantType type, int id, float posX, float posY, float rot, Map<String,String> optInfos){
		this.setId(id);
		this.setType(type);
		this.setRelPosX(posX);
		this.setRelPosY(posY);
		this.setRot(rot);
		this.setOptInfos(optInfos);
	}
	
	/**
	 * Constructor
	 * @param posX
	 * @param posY
	 * @param rot
	 */
	public UpdateParticipant(float posX, float posY, float rot){
		this.setRelPosX(posX);
		this.setRelPosY(posY);
		this.setRot(rot);
	}

	public void setRelPosX(float posX) {
		this.relPosX = posX;
	}
	
	public float getRelPosX(){
		return relPosX;
	}
	
	public float getRelPosY(){
		return relPosY;
	}


	public void setRelPosY(float posY) {
		this.relPosY = posY;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ParticipantType getType() {
		return type;
	}

	public void setType(ParticipantType type) {
		this.type = type;
	}

	public Map<String, String> getOptInfos() {
		return optInfos;
	}

	public void setOptInfos(Map<String, String> optInfos) {
		this.optInfos = optInfos;
	}
	
}
