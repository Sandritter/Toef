package simulation.traffic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulation.math.BezierCurveNP;
import simulation.math.interfaces.ICurve;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.ITile;
import simulation.traffic.enums.Direction;

/**
 * Link between waypoints on a traffic system
 */
public class Link {
	
	private Waypoint w1;
	private Waypoint w2;
	
	private ICurve curve;
	private Direction direction = Direction.AHEAD;
	private int unit = 1;
	private PathSystem pathSystem;
	private List<IParticipant> participants = new ArrayList<IParticipant>();
	private HashMap<String, List<Link>> relevantLinks = new HashMap<String, List<Link>>();
	private Map<String, String> properties = new HashMap<String, String>();
	
	public Link(Waypoint w1, Waypoint w2, Direction tag){
		this.w1 = w1;
		this.w2 = w2;
		this.direction = tag;
		curve = new BezierCurveNP(w1.getPosition(), w2.getPosition());
	}
	
	public Link(Waypoint w1, Waypoint w2, List<IVector> controlPoints, Direction tag){
		this.w1 = w1;
		this.w2 = w2;
		this.direction = tag;
		curve = new BezierCurveNP(w1.getPosition(), controlPoints, w2.getPosition());
	}

	/**
	 * Adds a reference to a relevant link to this link
	 * @param tag Tag
	 * @param link Link
	 */
	public void addRelevantLink(String tag, Link link){
		if(!relevantLinks.containsKey(tag)){
			relevantLinks.put(tag, new ArrayList<Link>());
		}
		
		relevantLinks.get(tag).add(link);
	}
	
	/**
	 * Adds a property to the link
	 * @param key Key of property
	 * @param value Value of property
	 */
	public void addProperty(String key, String value){
		properties.put(key, value);
	}
	
	/**
	 * Adds a participant to the link
	 * @param participant Participant
	 */
	public void addParticipant(IParticipant participant){
		if(!participants.contains(participant)){
			participants.add(0, participant);
			pathSystem.addParticipant(participant);
		}
	}
		
	/**
	 * Removes a participant from the link
	 * @param participant Participant
	 */
	public void removeParticipant(IParticipant participant){
		participants.remove(participant);
		pathSystem.removeParticipant(participant);
	}
	
	/**
	 * Returns the first Participant on the link
	 * @return Participant
	 */
	public IParticipant getFirstParticipant(){
		if(!participants.isEmpty()){
			return participants.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the next participant after the given participant
	 * @param participant Participant
	 * @return Participant
	 */
	public IParticipant getNextParticipant(IParticipant participant){
		int size = participants.size();
		if(size == 0){
			return null;
		}
		
		int idx = participants.indexOf(participant);	
		if(idx + 1 < size){
			return participants.get(idx + 1);
		}
		
		return null;
	}
	
	/**
	 * Returns a position on the link
	 * @param meter Distance from the start point
	 * @return position
	 */
	public IVector getPoint(float meter){
		float length = curve.getLength() * unit;
		float i = 1 / (length / meter);
		return curve.getPoint(i);
	}
	
	public Map<String, String> getProperties(){
		return properties;
	}
	
	public int getPropertiesCount(){
		return properties.size();
	}
	
	public List<IParticipant> getParticipants(){
		return participants;
	}
			
	public Direction getDirection(){
		return direction;
	}
	
	public ICurve getCurve(){
		return curve;
	}

	public Waypoint getEndPoint() {
		return w2;
	}

	public void setEndPoint(Waypoint w2) {
		this.w2 = w2;
	}

	public Waypoint getStartPoint() {
		return w1;
	}

	public void setStartPoint(Waypoint w1) {
		this.w1 = w1;
	}
	
	public float getLength(){
		return curve.getLength() * unit;
	}
	
	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	public PathSystem getPathSystem() {
		return pathSystem;
	}
	
	public ITile getTile() {
		return pathSystem.getTrafficSystem().getTile();
	}

	public void setPathSystem(PathSystem pathSystem) {
		this.pathSystem = pathSystem;
	}

	public HashMap<String, List<Link>> getRelevantLinks() {
		return relevantLinks;
	}
	
	public Map<String, List<IParticipant>> getRelevantParticipants() {
		Map<String, List<IParticipant>> relevantParticipants = new HashMap<String, List<IParticipant>>();
		
		for(String tag: relevantLinks.keySet()){
			if(!relevantParticipants.containsKey(tag)){
				relevantParticipants.put(tag, new ArrayList<IParticipant>());
			}
			
			for(Link link : relevantLinks.get(tag)){
				relevantParticipants.get(tag).addAll(link.getParticipants());
			}
		}
		return relevantParticipants;
	}
}
