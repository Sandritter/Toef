package simulation.participants;

import simulation.math.CircleCollider;
import simulation.participants.interfaces.IHull;
import simulation.participants.interfaces.IParticipant;

/**
 * The hull of a participant
 */
public class Hull implements IHull {
	
	private float length;
	private float width;
	private CircleCollider collider;
	private IParticipant participant;
	
	public Hull(IParticipant participant, float length, float width){
		this.length = length;
		this.width = width;
		this.participant = participant;
		collider = new CircleCollider(this.participant.getTransform().getPosition(), length >= width ? length / 2 : width / 2);
	}	
	
	/**
	 * Checks the collision with an other hull
	 * @param hull Hull
	 * @return boolean
	 */
	public boolean collide(IHull hull){
		return collider.collide(hull.getCollider());
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public CircleCollider getCollider() {
		return collider;
	}

	public void setCollider(CircleCollider collider) {
		this.collider = collider;
	}

}
