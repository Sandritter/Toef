package simulation.participants;

import java.util.HashMap;
import java.util.Map;

import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.IParticipantProxy;
import simulation.traffic.enums.Direction;

/**
 * Is used by all python scripts
 */
public class ParticipantProxy implements IParticipantProxy {
	
	private IParticipant participant;
	
	public ParticipantProxy(IParticipant participant){
		this.participant = participant;
	}
		
	/**
	 * Accelerates the participant
	 * @param i Value between 0 and 1
	 * @param t Time
	 */
	public void accelerate(float i) {
		participant.accelerate(i);
	}

	/**
	 * Decelerates the participant
	 * @param i Value between 0 and 1
	 * @param t Time
	 */
	public void decelerate(float i) {
		participant.decelerate(i);
	}
	
	/**
	 * Changes the direction the participant moves to
	 * @param nextDirection Direction
	 */
	public void steer(String direc){
		participant.steer(Direction.valueOf(direc));
	}
		
	/**
	* Returns the participant info as key value pairs
	* @return Map of infos
	*/
	public Object getInfo(){
		Map<String, Object> infos = new HashMap<String, Object>();
		infos.put("Fuel", participant.getFuel());
		infos.put("Velocity", participant.getVelocityKmH());
		infos.put("MaxVelocity", participant.getMaxVelocityKmH());
		return infos;
	}
	
	public float getVelocity(){
		return participant.getVelocityKmH();
	}
	
	public float getFuel(){
		return participant.getFuel();
	}
	
	public float getMaxVelocity(){
		return participant.getMaxVelocityKmH();
	}
	
	/**
	* Adds a property to the participant
	* @param key Key of property
	* @param value Value of property
	*/
	public void addProperty(String key, String value){
		participant.addProperty(key, value);
	}
	
	/**
	 * Removes a property by its key
	 * @param key Key of property to be removed
	 */
	public void removeProperty(String key){
		participant.removeProperty(key);
	}
}
