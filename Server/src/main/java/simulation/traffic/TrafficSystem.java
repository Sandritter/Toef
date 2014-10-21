package simulation.traffic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.traffic.enums.PathType;

/**
 * Describes the traffic system of one specific tile
 */
public class TrafficSystem {
	private Map<PathType, PathSystem> systems;
	private Map<Integer, IParticipant> participants;
	private INegotiableTile tile;
	
	public TrafficSystem(){
		participants = new HashMap<Integer, IParticipant>();
		systems = new HashMap<PathType, PathSystem>();
	}
	
	public void addPathSystem(PathType type, PathSystem system){
		systems.put(type, system);
		system.setTrafficSystem(this);
	}
	
	public PathSystem getPathSystem(PathType type){
		return systems.get(type);
	}
	
	/**
	 * Finalizes the traffic system. After this, no manipulations should be done to either the waypoints or the links, except on methods of the traffic system object.
	 */
	public void finalizeSystem(){
		for (PathSystem s : systems.values()){
			s.finalizeSystem();
		}
	}
	
	/**
	 * Docks one traffic system to an other one
	 * @param  tSystem Traffic system
	 */
	public void dock(TrafficSystem tSystem){
		for(PathType type : tSystem.systems.keySet()){
			if(systems.containsKey(type)){
				systems.get(type).dock(tSystem.systems.get(type));
			}
		}
	}
	
	/**
	 * Docks one traffic system to an other one
	 * @param  tSystem Traffic system
	 */
	public void undock(TrafficSystem tSystem){
		for(PathType type : tSystem.systems.keySet()){
			if(systems.containsKey(type)){
				systems.get(type).undock(tSystem.systems.get(type));
			}
		}
	}
		
	/**
	 * Rotates the whole traffic system
	 * @param angle Angle
	 */
	public void rotate(IAngle angle){
		for(PathSystem s : systems.values()) {
			s.rotate(angle);
		}
	}
	
	/**
	 * Translates the traffic system to a new position
	 * @param vector Translation
	 */
	public void translate(IVector vector){
		for(PathSystem s : systems.values()) {
			s.translate(vector);
		}
	}
	
	/**
	 * Translates and rotates the traffic system to a new position
	 * @param angle Angle
	 * @param vector Translation
	 */
	public void translateAndRotate(IVector vector, IAngle angle){
		for(PathSystem s : systems.values()) {
			s.translateAndRotate(vector, angle);
		}
	}
	
	/**
	 * Adds a participant to the traffic system
	 * @param participant Participant
	 */
	public void addParticipant(IParticipant participant){
		participants.put(participant.getId(), participant);
	}
	
	/**
	 * Removes a participant from the traffic system
	 * @param participant Participant
	 */
	public void removeParticipant(IParticipant participant){
		participants.remove(participant.getId());
	}
	
	/**
	 * Removes a participant from the traffic system
	 * @param id ID of participant
	 */
	public void removeParticipant(int id){
		participants.remove(id);
	}
	
	public String toString(){
		String s = "";
		for(PathType type : systems.keySet()){
			s += type.getText() + ":\n";
			s += systems.get(type);
			s += "\n";
		}
		return s;
	}
		
	public Collection<IParticipant> getParticipantsAsCollection(int layer) {
		ArrayList<IParticipant> participantLayer = new ArrayList<IParticipant>();
		for(PathSystem system : systems.values()){
			if(system.getLayer() == layer){
				participantLayer.addAll(system.getParticipants());
			}
		}
		return participantLayer;
	}
	
	public int getParticipantCount(int layer){
		int size = 0;
		for(PathSystem system : systems.values()){
			if(system.getLayer() == layer){
				size += system.getParticipantCount();
			}
		}
		return size;
	}
	
	public Collection<IParticipant> getParticipantsAsCollection() {
		return participants.values();
	}
	
	public Map<Integer, IParticipant> getParticipants() {
		return participants;
	}

	public INegotiableTile getTile() {
		return tile;
	}

	public void setTile(INegotiableTile tile) {
		this.tile = tile;
	}
	
	public int getParticipantCount(){
		return participants.size();
	}
}
