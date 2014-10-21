package simulation.vision;

import java.util.Set;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;

import simulation.participants.interfaces.IParticipant;
import simulation.traffic.Link;
import simulation.traffic.enums.Direction;
import simulation.vision.interfaces.IExtendedVisionCalculator;

/**
 * Determines the next junction on the route of one participant
 */
public class NextJunctionCalculator implements IExtendedVisionCalculator {

	private boolean enabled;
	private IParticipant participant;
	private float distance;
	private Set<Direction> directions;
	
	public void start(IParticipant participant, Link first) {
		this.participant = participant;
		directions = null;
		distance = first.getLength() - participant.getTransform().getDistance();
		junctionTest(first);
	}

	public void calculate(Link next) {
		if(directions == null){
			distance += next.getLength();
			junctionTest(next);
		}
	}
	
	private void junctionTest(Link link){
		if(link.getEndPoint().getLinkCount() > 1){
			//junction
			directions = link.getEndPoint().getDirections();
			
			//Correct distances
			distance -= participant.getHull().getLength() / 2;
	
			if(distance < 0){
				distance = 0;
			}
		}
	}

	public PyObject getResult() {
		PyDictionary data = new PyDictionary();
		if(directions == null){
			return data;
		}
		
		data.put("Distance", distance);
		
		PyList linkData = new PyList();
		
		for(Direction direc : directions){
			linkData.add(direc.toString());
		}
		
		data.put("Directions", linkData);
		return data;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
