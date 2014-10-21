package simulation.participants;

import simulation.math.Angle;
import simulation.math.Vector;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.ITransform;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.traffic.Link;
import simulation.traffic.PathSystem;
import simulation.traffic.enums.Direction;

/**
 * Position and orientation specific informations
 */
public class Transform implements ITransform {
	
	private IVector pos; //Position
	private IAngle rot; //Rotation
	private Link currentLink;
	private float distance; // Distance from last Waypoint
	private IParticipant participant;
	
	public Transform(IParticipant participant){
		this.participant = participant;
		this.pos = new Vector(0, 0);
		this.rot = new Angle(0);
		this.distance = 0;
	}
		
	/**
	 * Moves the participant dependending on his velocity
	 * @param deltaTime Delta time
	 * @param velocity Velocity
	 * @param nextDirection next Direction
	 */
	public void move(float deltaTime, float velocity, Direction nextDirection){
		Link link = currentLink;
		float distanceLeft = velocity * deltaTime + distance;
		float linkLength = link.getLength();
		IVector newPos;
		
		//Calculate Position
		while(distanceLeft >= linkLength){
			if(link.getEndPoint().getLink(nextDirection) == null){
				distanceLeft = link.getLength();
				participant.setVelocity(0);
				break;
			}
			distanceLeft -=  linkLength;
			link = link.getEndPoint().getLink(nextDirection);
			linkLength = link.getLength();
		}
		
		newPos = link.getPoint(distanceLeft); 
		
		//Calculate Rotation
		IVector oldPos = pos;
		
		//Set new values
		if(!oldPos.equals(newPos)){
			IAngle rot = oldPos.angleToPoint(newPos);
			updatePosition(newPos, rot, distanceLeft, link);
		} else {
			updatePosition(newPos, distanceLeft, link);
		}
	}
	
	/**
	 * Updates the position of a car
	 * @param newPos Position of the car
	 * @param distance Distance to the last passed Waypoint
	 * @param link Link the car is currently on
	 */
	public void updatePosition(IVector newPos, float distance, Link link) {
		updatePosition(newPos, rot, distance, link);	
	}
	
	/**
	 * Updates the position of the car
	 * @param newPos Position of the car
	 * @param rot Rotation of the car
	 * @param distance Distance to the last passed Waypoint
	 * @param link Link the car is currently on
	 */
	public void updatePosition(IVector newPos, IAngle rot, float distance, Link link) {
		if(currentLink != link){
			if(currentLink != null){
				currentLink.removeParticipant(participant);
			}
			link.addParticipant(participant);
			this.currentLink = link;	
		}
				
		this.pos = newPos;
		this.rot = rot;
		this.distance = distance;
	}

	public IVector getPosition() {
		return pos;
	}

	public void setPosition(IVector pos) {
		this.pos = pos;
	}

	public IAngle getRotation() {
		return rot;
	}

	public void setRotation(IAngle rot) {
		this.rot = rot;
	}

	public Link getCurrentLink() {
		return currentLink;
	}

	public void setCurrentLink(Link currentLink) {
		this.currentLink = currentLink;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public PathSystem getCurrentPathSystem() {
		return currentLink.getPathSystem();
	}
	
	public INegotiableTile getCurrentTile() {
		return currentLink.getPathSystem().getTrafficSystem().getTile();
	}
}
