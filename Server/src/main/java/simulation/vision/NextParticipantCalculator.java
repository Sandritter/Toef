package simulation.vision;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;

import simulation.control.map.enums.CardinalDirection;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.traffic.Link;
import simulation.vision.interfaces.IExtendedVisionCalculator;

/**
 * Determines the next participant on the route of one participant
 */
public class NextParticipantCalculator implements IExtendedVisionCalculator{
	
	private IParticipant participant;
	private float nextDist = 0;
	private IParticipant nextParticipant;
	private boolean enabled;
	
	public void start(IParticipant participant, Link first) {
		this.participant = participant;
		nextDist = 0;
		Link link = first;
		nextParticipant = link.getNextParticipant(participant);
		
		
		if(nextParticipant != null){
			nextDist = nextParticipant.getTransform().getDistance() - participant.getTransform().getDistance();
		} else {
			nextDist += link.getLength() - participant.getTransform().getDistance();
		}
	}

	public void calculate(Link next) {
		if(nextParticipant == null){
			//link found			
			IParticipant nextP = next.getFirstParticipant();	

			//participant found...				
			if(nextP != null && nextP != participant){
				//.. and not the same participant as the one passed to the method
				nextDist += nextP.getTransform().getDistance();
				nextParticipant = nextP;
			} else {
				nextDist += next.getLength();
			}
		
			if(nextParticipant != null){
				//Correct values
				nextDist -= nextParticipant.getHull().getLength() / 2 + participant.getHull().getLength() / 2;
				nextDist = nextDist < 0 ? 0 : nextDist;
			}
		}
	}

	public PyObject getResult() {
		PyDictionary data = new PyDictionary();
		if(nextParticipant == null){
			return data;
		}
		data = calculateParticipantData(participant, nextParticipant);
		data.put("Distance", nextDist);
		return data;
	}
	
	private PyDictionary calculateParticipantData(IParticipant ownParticipant, IParticipant otherParticipant){
		PyDictionary info = new PyDictionary();
		info.put("Type", otherParticipant.getType().toString());
		info.put("Velocity", otherParticipant.getVelocityKmH());
		info.put("AirDistance", otherParticipant.getTransform().getPosition().sub(ownParticipant.getTransform().getPosition()).length());
		
		//Calculate relative orientation
		CardinalDirection direcOwn = CardinalDirection.getDirectionFromRotation(ownParticipant.getTransform().getRotation());
		CardinalDirection direcP = CardinalDirection.getDirectionFromRotation(otherParticipant.getTransform().getRotation());
		CardinalDirection relativeDirection = direcOwn.relativeDirection(direcP);
		
		//Calculate relative position
		PyList pos = new PyList();
		
		IVector participantPos = otherParticipant.getTransform().getPosition();
		IVector relativePos = participantPos.sub(ownParticipant.getTransform().getPosition());
		IVector relativePos2 = relativePos.rotateAround(ownParticipant.getTransform().getRotation(), new Vector(0, 0));
						
		pos.add(relativePos2.getX());
		pos.add(relativePos2.getY());
		info.put("Position", pos);
		info.put("Direction", relativeDirection);
		return info;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
