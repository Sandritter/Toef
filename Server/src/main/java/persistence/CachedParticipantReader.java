package persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import persistence.interfaces.IParticipantReader;
import simulation.participants.Car;
import simulation.participants.Ship;
import simulation.participants.enums.ParticipantType;
import simulation.participants.enums.ParticipantUpperType;
import simulation.participants.interfaces.IParticipant;
import simulation.traffic.enums.PathType;

/**
 * Reads a xml file and creates a participant out of it
 */
public class CachedParticipantReader implements IParticipantReader{
	
	private HashMap<ParticipantType, Document> participantTrees = new HashMap<ParticipantType, Document>();
	
	public CachedParticipantReader(){
	}
	
	/**
	 * Reads the participant on the given path
	 * @param path Path
	 * @param type Participant type
	 * @return Participant
	 */
	public IParticipant readParticipant(String path, ParticipantType type) {	
		Document tree;
		if (participantTrees.containsKey(type)){
			tree = participantTrees.get(type);
		} else {
			tree = readParticipantTree(path);
			participantTrees.put(type, tree);
		}

		return buildParticipant(tree, type);
	}
	
	/**
	 * Reads the participant tree on the given path
	 * @return Document tree
	 */
	private Document readParticipantTree(String path){		
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
	 * Builds the participant out of the given document tree
	 * @param tree Document
	 * @param type the type of the participant
	 * @return Participant
	 */
	private IParticipant buildParticipant(Document tree, ParticipantType type) {
		Element root = tree.getRootElement();
		PathType pathType = PathType.valueOf(root.getAttributeValue("pathType").toUpperCase());
		List<Element> children = root.getChildren();
		Element defaultElement = null;
		
		for(Element child : children){
			if(child.getName().equals("default")){
				defaultElement = child;
			} else {
				ParticipantType childType = ParticipantType.valueOf(child.getAttributeValue("type").toUpperCase());
				if(childType == type){
					return buildParticipant(child, type, pathType);
				}
			}
		}
		return buildParticipant(defaultElement, type, pathType);
	}
	
	/**
	 * Builds the participant out of the passed parameters
	 * @param routeElement The Route Element of the participant
	 * @param type the type of the participant
	 * @param pathType The type of path the participant can move on
	 * @return Participant
	 */
	private IParticipant buildParticipant(Element routeElement, ParticipantType type, PathType pathType){
		if(type.getUpperType() == ParticipantUpperType.CAR || type.getUpperType() == ParticipantUpperType.SHIP){
			//Read Velocity
			float maxVelocity = Float.parseFloat(routeElement.getChild("maxVelocity").getText());
			String unit = routeElement.getChild("maxVelocity").getAttributeValue("unit");
			maxVelocity = unit != null && unit.equals("kmh") ? maxVelocity / 3.6f : maxVelocity;
			
			//Read Acceleration
			float maxAcceleration = Float.parseFloat(routeElement.getChild("maxAcceleration").getText());
			unit = routeElement.getChild("maxAcceleration").getAttributeValue("unit");
			maxAcceleration = unit != null && unit.equals("kmh") ? maxAcceleration / 3.6f : maxAcceleration;
			
			//Read Fuel
			float fuel = Float.parseFloat(routeElement.getChild("fuel").getText());
			
			//Read Width
			float width = Float.parseFloat(routeElement.getChild("width").getText());
			
			//Read Length
			float length = Float.parseFloat(routeElement.getChild("length").getText());

			if (type.getUpperType() == ParticipantUpperType.CAR){
				return new Car(type, pathType, length, width, maxVelocity, maxAcceleration, fuel);
			} else {
				return new Ship(type, pathType, length, width,  maxVelocity, maxAcceleration, fuel);
			}
		} else {
			return null;
		}
	}
}
