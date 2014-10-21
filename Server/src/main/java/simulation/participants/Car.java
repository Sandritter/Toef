package simulation.participants;
import simulation.participants.enums.ParticipantType;
import simulation.traffic.enums.PathType;

/**
 * Represents a car
 */
public class Car extends Participant{
	
	public Car() {
		super(ParticipantType.CABRIOPINK, PathType.STREET, 5, 3, 30, 4, 70);
	}
	
	public Car(ParticipantType type, PathType pathType, float length, float width, 
				float maxVelocity, float maxAcceleration, float fuel) {
		super(type, pathType, length, width, maxVelocity, maxAcceleration, fuel);
	}
}
