package simulation.participants;
import java.util.HashMap;
import java.util.Map;

import simulation.participants.enums.DriverType;
import simulation.participants.enums.ParticipantType;
import simulation.participants.interfaces.IBehaviour;
import simulation.participants.interfaces.IHull;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.ITransform;
import simulation.traffic.enums.Direction;
import simulation.traffic.enums.PathType;
import simulation.vision.interfaces.IVision;

/**
 * Represents a participant
 */
public abstract class Participant implements IParticipant
{
	private int id;

	private IBehaviour behaviour; //Python script
	private ITransform transform; //Position, rotation..
	private IHull hull; // Dimensions and collision
	private IVision vision;
	
	private ParticipantType type;
	private PathType pathType;
	private Driver driver;
	
	private float velocity = 0; // m/s
	private float maxVelocity; // m/s
	private float maxAcceleration;  // m/s^2
	private float maxFuel;
	private float fuel;
	
	private boolean disabled = false; // True when the participant is damaged
	private boolean actionPerformed = false;
	
	private Direction nextDirection = Direction.AHEAD;
	private Map<String, String> properties = new HashMap<String, String>();
	
	private float deltaTime = 0;
		
	public Participant() {
		this(ParticipantType.CABRIOPINK, PathType.STREET, 5, 3, 30, 4, 70);
	}

	public Participant(ParticipantType type, PathType pathType, float length, float width, 
						float maxVelocity, float maxAcceleration, float fuel) {
		this.type = type;
		this.driver = new Driver(DriverType.getRandomType());
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.maxFuel = fuel;
		this.fuel = fuel;
		this.velocity = 0;
		this.pathType = pathType;
		this.transform = new Transform(this);
		this.hull = new Hull(this, length, width);
	}
	
	/**
	 * Accelerates the participant
	 * @param t Time
	 */
	public void accelerate(float i) {
		if (actionPerformed){
			return;
		}

		i = adjustRange(i);
		
		if(velocity + i * maxAcceleration * deltaTime <= maxVelocity){
			velocity += (i * maxAcceleration) * deltaTime;
		} else {
			velocity = maxVelocity;
		}
		
		actionPerformed = true;
	}

	/**
	 * Decelerates the participant
	 * @param t Time
	 */
	public void decelerate(float i) {
		if (actionPerformed){
			return;
		}
		
		float fac = 15;
		i = adjustRange(i);
		
		if(velocity - i * fac * deltaTime >= 0){
			velocity -= (i * fac) * deltaTime;
		} else {
			velocity = 0;
		}
		
		actionPerformed = true;
	}
			
	public void update(float t){
		if(disabled){
			return;
		}
		
		deltaTime = t;
		actionPerformed = false;
		
		if (behaviour != null){
			behaviour.update(vision);
		}

		updateFuel(t);
		
		//Move if the participant got enough fuel
		if(fuel > 0){	
			transform.move(t, velocity, nextDirection);
		}
		
		//Update the collider position
		hull.getCollider().setCenter(transform.getPosition());
	}
	
	/**
	 * Adjust the range of the parameter to be in range [0,1]
	 * @param i Value to be adjusted
	 * @return adjusted value
	 */
	private float adjustRange(float i){
		if (i > 1){
			return 1;
		}
		
		if (i < 0){
			return 0;
		}
		
		return i;
	}
	
	/**
	 * Updates the fuel 
	 * @param t Time
	 */
	private void updateFuel(float t){
		float dec = 0.001f  * velocity * t;
		if(fuel - dec >= 0){
			fuel = fuel - dec;
		} else {
			fuel = 0;
		}
	}
			
	/**
	 * Checks the collision between this and an other participant. Both participants get disabled on collision
	 * @param participant Other participant
	 */
	public void collideWith(IParticipant participant){
		//check if participant collided with other participant
		boolean collided = hull.collide(participant.getHull());
		
		// if a collision happened disable both participants
		if(collided){
			setDisabled(true);
			participant.setDisabled(true);
		}
	}
			
	/**
	 * Adds a property to the participant
	 * @param key Key of property
	 * @param value Value of property
	 */
	public void addProperty(String key, String value){
		properties.put(key, value);
	}
	
	/**
	 * Removes a property by its key
	 * @param key Key of property to be removed
	 */
	public void removeProperty(String key){
		properties.remove(key);
	}
	
	/**
	 * Refills the participant
	 */
	public void refill(){
		fuel = maxFuel;
	}
	
	/**
	 * Changes the direction the participant moves to
	 * @param nextDirection Direction
	 */
	public void steer(Direction nextDirection) {
		this.nextDirection = nextDirection;
	}
	
	//Setter and Getter
	
	public Map<String, String> getProperties(){
		properties.put("Velocity", Float.toString(getVelocityKmH()));
		properties.put("DriverType",driver.getType().getText());
		properties.put("Fuel", Float.toString(fuel));
		properties.put("Damaged", disabled == true ? "true" : "false");
		properties.put("MaxVelocity", Float.toString(getMaxVelocityKmH()));
		properties.put("MaxFuel", Float.toString(maxFuel));
		
		return properties;
	}
	
	public void setVision(IVision vision){
		this.vision = vision;
	}
		
	public Driver getDriver() {
		return driver;
	}
	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getVelocity() {
		return velocity;
	}
	
	public float getVelocityKmH() {
		return velocity * 3.6f;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}
	
	public float getMaxVelocityKmH() {
		return maxVelocity * 3.6f;
	}
	
	public Direction getNextDirection() {
		return nextDirection;
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

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		if(disabled){
			velocity = 0;
		}
		this.disabled = disabled;
	}

	public float getMaxAcceleration() {
		return maxAcceleration;
	}
	
	public void setBehaviour(IBehaviour behaviour){
		this.behaviour = behaviour;
	}

	public float getFuel() {
		return fuel;
	}
	
	public PathType getPathType(){
		return pathType;
	}
	
	public ITransform getTransform(){
		return transform;
	}

	public IHull getHull() {
		return hull;
	}
}
