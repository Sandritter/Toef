package simulation.vision;

import java.util.NoSuchElementException;
import java.util.Set;

import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.ITransform;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.traffic.Link;
import simulation.traffic.enums.Direction;

/**
 * Used to navigate through a traffic system
 */
public class Navigator{
	
	private Link link;
	private float distance;
	private Direction nextDirection;
	private Set<INegotiableTile> validTiles;

	public Navigator(IParticipant participant, float sightRange, Set<INegotiableTile> validTiles){
		ITransform t = participant.getTransform();

		this.validTiles = validTiles;
		this.link = t.getCurrentLink();
		this.nextDirection = participant.getNextDirection();		
		this.distance = sightRange;
		this.distance -= link.getLength() - participant.getTransform().getDistance();
		
	}
	
	/**
	 * Checks if there is a next link in the traffic system
	 */
	public boolean hasNext() {
		float linkLength = link.getLength();

		Link nextLink = link.getEndPoint().getLink(nextDirection);
		if(distance < linkLength || nextLink == null ||
			!validTiles.contains(nextLink.getTile())){
			return false;
		}
		
		return true;
	}

	/**
	 * Returns the next link in the traffic system
	 */
	public Link next() {
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		
		link = link.getEndPoint().getLink(nextDirection);
		distance -= link.getLength();
		return link;
	}
}
