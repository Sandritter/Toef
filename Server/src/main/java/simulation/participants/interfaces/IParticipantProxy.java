package simulation.participants.interfaces;

public interface IParticipantProxy {

	/**
	 * Accelerates the participant
	 * @param i Value between 0 and 1
	 * @param t Time
	 */
	public void accelerate(float i);

	/**
	 * Decelerates the participant
	 * @param i Value between 0 and 1
	 * @param t Time
	 */
	public void decelerate(float i);

	/**
	 * Changes the direction the participant moves to
	 * @param nextDirection Direction
	 */
	public void steer(String direc);

	/**
	 * Returns the participant info as key value pairs
	 * @return Map of infos
	 */
	public Object getInfo();

	public float getVelocity();

	public float getFuel();

	public float getMaxVelocity();

	/**
	 * Adds a property to the participant
	 * @param key Key of property
	 * @param value Value of property
	 */
	public void addProperty(String key, String value);

	/**
	 * Removes a property by its key
	 * @param key Key of property to be removed
	 */
	public void removeProperty(String key);

}