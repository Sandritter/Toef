package persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import persistence.interfaces.ITrafficSystemReader;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;
import simulation.tiles.enums.TileType;
import simulation.traffic.Link;
import simulation.traffic.PathSystem;
import simulation.traffic.TrafficSystem;
import simulation.traffic.Waypoint;
import simulation.traffic.enums.Direction;
import simulation.traffic.enums.PathType;

/**
 * Reads a xml file and creates a traffic system out of it
 */
public class CachedTrafficSystemReader implements ITrafficSystemReader {
	
	private HashMap<TileType, Document> trafficSystemTrees = new HashMap<TileType, Document>();
	private Map<String, Float> constants = new HashMap<String, Float>();
	private Map<String, Waypoint> waypoints = new HashMap<String, Waypoint>();
	private Map<PathType, Map<String, Link>> systemLinks = new HashMap<PathType, Map<String,Link>>();
	private float scaleFactor;
	private TrafficSystem trafficSystem;
	
	public CachedTrafficSystemReader(){
	}
	
	/**
	 * Reads the traffic system on the given path
	 * @param path Path of the traffic system in the file system
	 * @param type Tile type
	 * @param size Size of the system
	 * @param unit How many meters are equal to 1 unit
	 * @return Traffic system
	 */
	public TrafficSystem readTrafficSystem(String path, TileType type, int size, int unit) {	
		Document tree;
		
		if (trafficSystemTrees.containsKey(type)){
			tree = trafficSystemTrees.get(type);
		} else {
			tree = readTrafficSystemTree(path);
			trafficSystemTrees.put(type, tree);
		}
		
		return buildTrafficSystem(tree, size, unit);
	}
	
	/**
	 * Reads the traffic system tree on the given path
	 * @return Document tree
	 */
	private Document readTrafficSystemTree(String path){
		Document tree = null;
		
		try {
			tree = new SAXBuilder().build(path);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tree;
	}

	/**
	 * Builds the traffic system out of the given document tree
	 * @param size Size of the system
	 * @param unit How many meters are equal to 1 unit
	 * @return Traffic system
	 */
	private TrafficSystem buildTrafficSystem(Document tree, int size, int unit) {
		trafficSystem = new TrafficSystem();

		Element root = tree.getRootElement();
		float size2 = Integer.parseInt(root.getAttributeValue("size"));
		scaleFactor = size / size2 ;
		
		//Read Streets
		List<Element> pathSystems = root.getChildren("pathSystem");
		
		for(Element system : pathSystems){
			String type = system.getAttributeValue("type").toUpperCase();
			PathType pathType = PathType.valueOf(type);
			String layerString = system.getAttributeValue("layer");
			int layer = layerString != null ? Integer.parseInt(layerString) : 0;
			Element propertiesElement = system.getChild("properties");
			Element constantsElement = system.getChild("constants");
			
			if(!systemLinks.containsKey(pathType)){
				systemLinks.put(pathType, new HashMap<String, Link>());
			}
			
			//Read constants
			if (constantsElement != null){
				readConstants(constantsElement);
			}
			
			//Read path system
			PathSystem pathSystem = buildSystem(system, size, unit, pathType, layer);
			
			//Read properties
			if(propertiesElement != null){
				readProperties(propertiesElement, pathType);
			}
			
			//Add the path system to the traffic system
			trafficSystem.addPathSystem(pathType, pathSystem);
			
			//clean up
			waypoints.clear();
			constants.clear();
		}
		
		Element relevances = root.getChild("relevances");
		if(relevances != null){
			readRelevances(relevances);
		}
		
		trafficSystem.finalizeSystem();
		
		//clean up
		systemLinks.clear();
		
		return trafficSystem;
	}
	
	/**
	 * Builds a path system which is part of a traffic system
	 * @param routeElement Element of the path system
	 * @param size Size of the path system
	 * @param unit How many meters are equal to 1 unit
	 * @param pathType Type of path the participant can move on
	 * @param layer Layer of the path system
	 * @return PathSystem
	 */
	private PathSystem buildSystem(Element routeElement, int size, int unit, PathType pathType, int layer){
		PathSystem pathSystem = new PathSystem(size, unit, layer);
		List<Element> points = routeElement.getChild("points").getChildren("point");
		
		//Read waypoints
		for(Element point : points){
			//Read coordinates
			String xS = point.getAttributeValue("x");
			float x = constants.containsKey(xS) ? constants.get(xS) : Float.parseFloat(xS);
			String yS = point.getAttributeValue("y");
			float y = constants.containsKey(yS) ? constants.get(yS) : Float.parseFloat(yS);
			//Create vector
			IVector v = new Vector(x, y);
			v = v.mult(scaleFactor);
			//Create waypoint
			Waypoint waypoint = new Waypoint(v, point.getAttributeValue("id"));
			if(!waypoints.containsKey((point.getAttributeValue("id")))){
				waypoints.put(point.getAttributeValue("id"), waypoint);
			}
						
			//Add to the path system
			pathSystem.addWaypoint(waypoint);
		}
		
		//Read links between waypoints
		List<Element> links = routeElement.getChild("links").getChildren("link");
		for(Element link : links){
			List<Element> controlPoints = link.getChildren("controlPoint");
			Waypoint start = waypoints.get(link.getAttributeValue("start"));
			Waypoint end = waypoints.get(link.getAttributeValue("end"));
			String direction = link.getAttributeValue("direction").toUpperCase();
			String id = link.getAttributeValue("id");
			
			Link createdLink = null;
			
			if(controlPoints.size() == 0){
				createdLink = start.link(end, Direction.valueOf(direction));
			} else {
				List<IVector> vectors = new ArrayList<IVector>();
				for(Element point : controlPoints){
					String xS = point.getAttributeValue("x");
					float x = constants.containsKey(xS) ? constants.get(xS) : Float.parseFloat(xS);
					String yS = point.getAttributeValue("y");
					float y = constants.containsKey(yS) ? constants.get(yS) : Float.parseFloat(yS);
					
					Vector v = new Vector(x, y);
					v = v.mult(scaleFactor);
					vectors.add(v);
				}

				createdLink = start.link(end, vectors, Direction.valueOf(direction));
			}
			
			if(id != null){
				systemLinks.get(pathType).put(id, createdLink);
			}
		}		
		
		return pathSystem;
	}
	
	/**
	 * Reads the constants which can be defined at the top of each path system
	 * @param constants Route element of the constants
	 */
	private void readConstants(Element constants){
		for (Element constant : constants.getChildren("constant")){
			this.constants.put(constant.getAttributeValue("name"), Float.parseFloat(constant.getValue()));
		}
	}
	
	/**
	 * Reads the relevances between links
	 * @param relevances Route element of the relevances
	 */
	private void readRelevances(Element relevances){
		for(Element relevance : relevances.getChildren("relevance")){
			String type = relevance.getAttributeValue("pathSystem").toUpperCase();
			PathType pathType = PathType.valueOf(type);
			String id = relevance.getAttributeValue("id");
			Link link = systemLinks.get(pathType).get(id);
			
			for(Element group : relevance.getChildren("group")){
				String tag = group.getAttributeValue("tag");
				
				for(Element l : group.getChildren("linkReference")){
					type = l.getAttributeValue("pathSystem").toUpperCase();
					pathType = PathType.valueOf(type);
					
					id = l.getAttributeValue("id");
					link.addRelevantLink(tag, systemLinks.get(pathType).get(id));
				}
			}				
		}	
	}
	
	/**
	 * Reads the properties which can be attached to waypoints and links
	 * @param properties Route element of the properties
	 * @param pathType Type of the path system
	 */
	private void readProperties(Element properties, PathType pathType){
		//Waypoint properties
		Element wpProperties = properties.getChild("pointProperties");
		
		if(wpProperties != null){
			List<Element> attach = wpProperties.getChildren("attach");
			
			if (attach != null){
				for(Element a : attach){
					String[] ids = a.getAttributeValue("toIDs").split(",");
					for(int i = 0; i < ids.length; i++){
						String id = ids[i];
						Waypoint wp = waypoints.get(id);
						
						for(Element p: a.getChildren()){
							wp.addProperty(p.getName(), p.getValue());
						}
					}
				}
			}
		}
		
		//Link properties
		Map<String, Link> links = systemLinks.get(pathType);
		
		Element linkProperties = properties.getChild("linkProperties");
		
		if(linkProperties != null){
			List<Element> attach = linkProperties.getChildren("attach");
			
			if (attach != null){
				for(Element a : attach){
					String[] ids = a.getAttributeValue("toIDs").split(",");
					for(int i = 0; i < ids.length; i++){
						String id = ids[i];
						Link link = links.get(id);
						
						for(Element p: a.getChildren()){
							link.addProperty(p.getName(), p.getValue());
						}
					}
				}
			}
		}
	}
}
