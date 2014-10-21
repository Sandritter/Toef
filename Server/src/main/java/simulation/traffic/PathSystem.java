package simulation.traffic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import simulation.math.Angle;
import simulation.math.Vector;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.traffic.enums.Direction;

/**
 * Describes the path system of one path type of a specific tile
 */
public class PathSystem {
	private Map<String, Waypoint> points; //All Points of that TrafficSystem
	private Map<String, Waypoint> innerPoints; //Points that are referenced and contains children
	private Map<String, Waypoint> endPoints; //Points without children
	private Map<String, Waypoint> startPoints; //Points that are not referenced by other points
	private int size;
	private int unit; // In meter
	private Vector center;
	private Map<Integer, IParticipant> participants;
	private int layer;
	private TrafficSystem trafficSystem;
	
	public PathSystem(int size, int units){
		this(size, units, 0);
	}
	
	public PathSystem(int size, int units, int layer){
		this.size = size;
		this.unit = units;
		this.layer = layer;
		
		center = new Vector(size, size).div(2);
		points = new HashMap<String, Waypoint>();
		innerPoints = new HashMap<String, Waypoint>();
		endPoints = new HashMap<String, Waypoint>();
		startPoints = new HashMap<String, Waypoint>();
		participants = new HashMap<Integer, IParticipant>();
	}
	
	/**
	 * Adds a Waypoint to the path system
	 * @param point Waypoint
	 * @return Returns false, if the path system already contains a Waypoint with the same id
	 */
	public boolean addWaypoint(Waypoint point){
		if (!points.containsKey(point.getId())){
			points.put(point.getId(), point);
			return true;
		}
		return false;
	}
	
	/**
	 * Finalizes the path system. After this, no manipulations should be done to either the waypoints or the links, except on methods of the traffic system object.
	 */
	public void finalizeSystem(){
		Map<String, Waypoint> referencedPoints = new HashMap<String, Waypoint>();
		
		for(Waypoint point : points.values()){
			//Set traffic system on waypoints and links
			point.setPathSystem(this);
			
			for(Link link : point.getLinks()){
				link.setPathSystem(this);
				link.setUnit(unit);
			}
			
			//Calculate end points
			if (point.getLinks().size() == 0){
				endPoints.put(point.getId(), point);
			}
			
			//Calculate referenced points
			for(Waypoint p : point.getLinkedWaypoints()){
				if(!referencedPoints.containsKey(p.getId())){
					referencedPoints.put(p.getId(), p);
				}
			}
		}

		//Calculate start and inner points
		for(Waypoint point : points.values()){
			if(!referencedPoints.containsKey(point.getId())){
				startPoints.put(point.getId(), point);
			} else {
				if(!endPoints.containsKey(point.getId())){
					innerPoints.put(point.getId(), point);
				}
			}
		}
	}
	
	/**
	 * Docks one path system to an other one
	 * @param pSystem PathSystem
	 */
	public void dock(PathSystem pSystem){
		Map<String, Waypoint> start = pSystem.startPoints;
		Map<String, Waypoint> end = pSystem.endPoints;
		
		//Connect end points with start points
		for(Waypoint e : endPoints.values()){
			IVector p1 = e.getPosition();
			for(Waypoint s : start.values()){
				IVector p2 = s.getPosition();
				if(p1.equals(p2)){
					e.link(s, Direction.AHEAD);
					e.getLink(Direction.AHEAD).setPathSystem(this);
				}
			}
		}
		
		for(Waypoint e : end.values()){
			IVector p1 = e.getPosition();
			for(Waypoint s : startPoints.values()){
				IVector p2 = s.getPosition();
				if(p1.equals(p2)){
					e.link(s, Direction.AHEAD);
					e.getLink(Direction.AHEAD).setPathSystem(pSystem);
				}
			}
		}
	}
	
	/**
	 * Undocks one path system from an other one
	 * @param pSystem Path system
	 */
	public void undock(PathSystem pSystem){
		Map<String, Waypoint> start = pSystem.startPoints;
		Map<String, Waypoint> end = pSystem.endPoints;
		
		for(Waypoint e : endPoints.values()){
			IVector p1 = e.getPosition();
			for(Waypoint s : start.values()){
				IVector p2 = s.getPosition();
				if(p1.equals(p2)){
					e.clearLinks();
				}
			}
		}
		
		for(Waypoint e : end.values()){
			IVector p1 = e.getPosition();
			for(Waypoint s : startPoints.values()){
				IVector p2 = s.getPosition();
				if(p1.equals(p2)){
					e.clearLinks();
				}
			}
		}
	}
	
	/**
	 * Rotates the whole path system
	 * @param angle Angle
	 */
	public void rotate(IAngle angle){
		for(Waypoint wp : points.values()) {
			//Rotate waypoint
			IVector pNew = wp.getPosition().rotateAround(new Angle(360).sub(angle), center);
			
			wp.setPosition(pNew);
			
			for(Link link : wp.getLinks()){
				//Rotate bezier curve
				link.getCurve().rotate(angle, center);
			}
		}
	}
	
	/**
	 * Translates the path system to a new position
	 * @param vector Translation
	 */
	public void translate(IVector vector){
		for(Waypoint wp : points.values()) {
			//Translate waypoint to new position
			IVector translation = new Vector(vector.getX() * size, vector.getY() * size);
			IVector p = wp.getPosition();
			wp.setPosition(p.add(translation));			
			for(Link link : wp.getLinks()){
				//Translate bezier curve
				link.getCurve().translate(translation);
			}
		}
	}
	
	/**
	 * Translates and rotates the path system to a new position
	 * @param angle Angle
	 * @param vector Translation
	 */
	public void translateAndRotate(IVector vector, IAngle angle){
		for(Waypoint wp : points.values()) {
			//Rotate waypoint
			IVector pNew = wp.getPosition().rotateAround(new Angle(360).sub(angle), center);
			
			//Translate waypoint to new position
			Vector translation = new Vector(vector.getX() * size, vector.getY() * size);
			wp.setPosition(pNew.add(translation));		
			
			for(Link link : wp.getLinks()){
				//Rotate bezier curve
				link.getCurve().rotate(angle, center);
				//Translate bezier curve
				link.getCurve().translate(translation);
			}
		}
	}
	
	/**
	 * Returns all end points of the path system
	 * @param  end points
	 */
	public Collection<Waypoint> getEndPoints(){
		return endPoints.values();
	}
	
	/**
	 * Returns all start points of the path system
	 * @return start points
	 */
	public Collection<Waypoint> getStartPoints(){
		return startPoints.values();
	}
	
	/**
	 * Returns a random start point
	 * @return Random start point
	 */
	public Waypoint getRandomStartpoint(){
		Random random = new Random();
		return (Waypoint)startPoints.values().toArray()[random.nextInt(startPoints.size())];
	}
	
	/**
	 * Returns a Waypoints to a given rotation
	 * @param rotation Rotation
	 * @return Waypoint
	 */
	public Waypoint getStartpoint(IAngle rotation){
		Waypoint current = null;
		float lastDifference = 1000;

		for(Waypoint start : startPoints.values()){
			Link link = start.getLink(Direction.AHEAD);
			IAngle startRotation;
			
			if(link != null){
				IVector p = link.getCurve().getPoint(0.01f);
				startRotation = start.getPosition().angleToPoint(p);
			} else {
				startRotation = rotation.sub(180);
			}
			
			float difference = rotation.differenceTo(startRotation).getValue();
			if(difference < lastDifference){
				current = start;
				lastDifference = difference;
			}
		}
		
		return current;
	}
	
	/**
	 * Adds a participant to the path system
	 * @param participant Participant
	 */
	public void addParticipant(IParticipant participant){
		participants.put(participant.getId(), participant);
		trafficSystem.addParticipant(participant);
	}
	
	/**
	 * Removes a participant from the path system
	 * @param participant Participant
	 */
	public void removeParticipant(IParticipant participant){
		participants.remove(participant.getId());
		trafficSystem.removeParticipant(participant);
	}
	
	/**
	 * Removes a participant from the path system
	 * @param id ID of participant
	 */
	public void removeParticipant(int id){
		participants.remove(id);
		trafficSystem.removeParticipant(id);
	}
	
	public String toString(){
		String s = "";
		s += "<TrafficSystem>" + "\n";
		//Start Waypoints
		s += "   <StartWaypoints>" + "\n";
		for (Waypoint point : startPoints.values()){
			for(String zeile : point.toString().split("\n")){
				s += "      " + zeile + "\n";
			}
		}
		s += "   </StartWaypoints>" + "\n\n";
		//Inner Waypoints
		s += "   <InnerWaypoints>" + "\n";
		for (Waypoint point : innerPoints.values()){
			for(String zeile : point.toString().split("\n")){
				s += "      " + zeile + "\n";
			}
		}
		s += "   </InnerWaypoints>" + "\n\n";
		//End Waypoints
		s += "   <EndWaypoints>" + "\n";
		for (Waypoint point : endPoints.values()){
			for(String zeile : point.toString().split("\n")){
				s += "      " + zeile + "\n";
			}
		}
		s += "   </EndWaypoints>" + "\n";
		s += "</TrafficSystem>";
		return s;
	}
	
	public Collection<IParticipant> getParticipants() {
		return participants.values();
	}

	public TrafficSystem getTrafficSystem() {
		return trafficSystem;
	}

	public void setTrafficSystem(TrafficSystem trafficSystem) {
		this.trafficSystem = trafficSystem;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public int getParticipantCount(){
		return participants.size();
	}
}
