package simulation.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;

import simulation.control.map.enums.CardinalDirection;
import simulation.control.map.interfaces.IMapController;
import simulation.math.Vector;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.tiles.interfaces.ITile;
import simulation.traffic.Link;
import simulation.vision.interfaces.IExtendedVisionCalculatorTiles;

/**
 * Determines all participants which are visible to one participant
 */
public class VisibleParticipantsCalculator implements IExtendedVisionCalculatorTiles{
	
	private boolean enabled;
	private IParticipant participant;
	private List<IParticipant> participants;
	
	private Set<INegotiableTile> visibleTiles;
	private IMapController map;
	
	public VisibleParticipantsCalculator(IMapController map){
		this.map = map;
	}
	
	public void start(IParticipant participant, Link first){
		this.participant = participant;
		participants = new ArrayList<IParticipant>();	
		
		for (INegotiableTile tile : visibleTiles){
			participants.addAll(tile.getTrafficSystem().getParticipantsAsCollection(first.getPathSystem().getLayer()));
		}
	}
	
	public void updateVisibleTiles(IParticipant participant, Set<INegotiableTile> tiles) {
		this.visibleTiles = tiles;
		
		//Add missing tiles
		IAngle rot = participant.getTransform().getRotation();
		CardinalDirection direc = CardinalDirection.getDirectionFromRotation(rot);
		ITile currentTile = map.getTile(participant.getTransform().getCurrentTile(), direc);
		
		if (currentTile instanceof INegotiableTile){
			addToList(visibleTiles, map.getTile(currentTile, direc.right()));
			addToList(visibleTiles, map.getTile(currentTile, direc.left()));
		}
	}

	public PyObject getResult() {
		PyList list = new PyList();
		for(IParticipant p: participants){
			if(p != participant){
				list.add(calculateData(participant, p));
			}
		}
		
		return list;
	}
	
	private PyObject calculateData(IParticipant ownParticipant, IParticipant otherParticipant){
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
	
	private void addToList(Set<INegotiableTile> set, ITile tile){
		if (tile instanceof INegotiableTile){
			INegotiableTile negTile = (INegotiableTile) tile;
			set.add(negTile);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
