package simulation.participants;
import simulation.participants.enums.ParticipantType;
import simulation.traffic.enums.PathType;

/**
 * Represents a ship
 */
public class Ship extends Participant {
	
	public Ship() {
		super(ParticipantType.BOAT, PathType.SEAROUTE, 5, 3, 30, 4, 70);
	}
	
	public Ship(ParticipantType type, PathType pathType, float length, float width,
				float maxVelocity, float maxAcceleration, float fuel) {
		super(type, pathType, length, width, maxVelocity, maxAcceleration, fuel);
	}
}
