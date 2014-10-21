package simulation.participants.interfaces;
import java.util.Map;

import simulation.participants.Driver;
import simulation.participants.enums.ParticipantType;
import simulation.traffic.enums.Direction;
import simulation.traffic.enums.PathType;
import simulation.vision.interfaces.IVision;

/**
 * Interface for all traffic participants (cars, people, etc)
 */
public interface IParticipant {
	//Drive
	public void steer(Direction nextDirection);
	public void accelerate(float i);
	public void decelerate(float i);
	public void refill();
	
	//Update
	public void update(float t);
	
	//Collide
	public void collideWith(IParticipant participant);
	
	//Getter
	public float getVelocity();
	public float getVelocityKmH();
	public Direction getNextDirection();
	public Driver getDriver();
	public float getMaxVelocity();
	public float getMaxVelocityKmH();
	public float getMaxAcceleration();
	public int getId();
	public ParticipantType getType();
	public boolean getDisabled();
	public float getFuel();
	public IHull getHull();
	public ITransform getTransform();
	public PathType getPathType();
	
	//Properties
	public Map<String, String> getProperties();
	public void addProperty(String key, String value);
	public void removeProperty(String key);

	//Setter
	public void setDriver(Driver driver);
	public void setId(int id);
	public void setVelocity(float velocity);
	public void setVision(IVision vision);
	public void setDisabled(boolean disabled);
	public void setBehaviour(IBehaviour behavoir);
}
