package simulation.traffic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import simulation.items.interfaces.IItem;
import simulation.math.Angle;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.traffic.enums.Direction;

/**
 * Represents a waypoint on a traffic system
 */
public class Waypoint {
	
	private IVector position;
	private String id;
	private Map<Direction, Link> links = new HashMap<Direction, Link>();
	private List<Link> backwardLinks = new ArrayList<Link>();
	private Map<String, String> properties = new HashMap<String, String>();
	private PathSystem pathSystem;
	private List<IItem> items = new ArrayList<IItem>();
	
	public Waypoint(IVector position, String id){
		this.position = position;
		this.id = id;
	}
		
	/**
	 * Connects the waypoint with an other one
	 */
	public Link link(Waypoint w, Direction tag){
		Link link = new Link(this, w, tag);
		links.put(link.getDirection(), link);
		w.addBackwardLink(link);
		return link;
	}
	
	/**
	 * Connects the waypoint with an other one
	 */
	public Link link(Waypoint w, List<IVector> controlPoints, Direction tag){
		Link link = new Link(this, w, controlPoints, tag);
		links.put(link.getDirection(), link);
		w.addBackwardLink(link);
		return link;
	}

	/**
	 * Adds a property to the waypoint
	 * @param key Key of property
	 * @param value Value of property
	 */
	public void addProperty(String key, String value){
		properties.put(key, value);
	}
		
	/**
	 * Adds a backward link to the waypoint
	 * @param link Link
	 */
	public void addBackwardLink(Link link){
		backwardLinks.add(link);
	}
	
	/**
	 * Removes a backward link from the waypoint
	 * @param link Link
	 */
	public void removeBackwardLink(Link link){
		backwardLinks.remove(link);
	}
	
	/**
	 * Adds an item to the waypoint
	 * @param item Item
	 */
	public void addItem(IItem item) {
		IAngle rot = new Angle(180);
		
		if(backwardLinks.size() == 1){
			Link l = backwardLinks.get(0);
			IVector p = l.getCurve().getPoint(0.9f);
			rot = position.angleToPoint(p);
		}
		
		item.setRotation(rot);
		items.add(item);
	}
	
	/**
	 * Returns the Link which corresponds to the given direction
	 */
	public Link getLink(Direction direction){
		if(links.containsKey(direction)){
			return links.get(direction);
		} else if (links.containsKey(Direction.AHEAD)){
			return links.get(Direction.AHEAD);
		} else if(links.size() > 0) {
			return (Link)links.values().toArray()[0];
		} else {
			return null;
		}
	}
		
	public List<Waypoint> getLinkedWaypoints(){
		List<Waypoint> linkedWaypoints = new ArrayList<Waypoint>();
		for(Link link : links.values()){
			linkedWaypoints.add(link.getEndPoint());
		}
		
		return linkedWaypoints;
	}
	
	public Map<String, String> getProperties(){
		return properties;
	}
	
	public int getPropertiesCount(){
		return properties.size();
	}
	
	public void clearLinks(){
		links.clear();
	}
	
	public int getLinkCount(){
		return links.size();
	}
	
	public Map<Direction, Link> getLinkMap(){
		return links;
	}
	
	public Collection<Link> getLinks(){
		return links.values();
	}
	
	public Set<Direction> getDirections(){
		return links.keySet();
	}

	public IVector getPosition(){
		return position;
	}
	
	public void setPosition(IVector position){
		this.position = position;
	}
	
	public String getId(){
		return id;
	}
		
	public PathSystem getPathSystem() {
		return pathSystem;
	}

	public void setPathSystem(PathSystem pathSystem) {
		this.pathSystem = pathSystem;
	}
	
	public List<IItem> getItems() {
		return items;
	}
		
	public String toString(){
		String s = "";
		s += "<Waypoint " + id + ">" + "\n";
		s += "   Coordinates: " + position.toString() + "\n";
		
		if(links.size() != 0){
			s += "   <Linked to>" + "\n";
			for(Direction direction : links.keySet()){
				Link link = links.get(direction);
				s += "      " + direction + " : " + "<Waypoint " + link.getEndPoint().getId() + ">"  + link.getEndPoint().getPosition().toString() + "</Waypoint " + link.getEndPoint().getId() + ">" + "\n";
			}
			s += "   </Linked to>" + "\n";
		}
		s += "</Waypoint> "
		+ id + ">" + "\n";
		return s;
	}
}
