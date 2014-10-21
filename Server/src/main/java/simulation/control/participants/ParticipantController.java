package simulation.control.participants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.events.ParticipantsUpdatedEvent;
import simulation.control.events.interfaces.ParticipantUpdatedListener;
import simulation.control.interfaces.IUpdatable;
import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.interfaces.IParticipantController;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.ITransform;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.tiles.interfaces.ITile;
import simulation.traffic.Link;
import simulation.traffic.PathSystem;
import simulation.traffic.TrafficSystem;
import simulation.traffic.Waypoint;
import simulation.traffic.enums.Direction;

/**
 * Manages the vehicles in the world
 */
public class ParticipantController implements IParticipantController, IUpdatable{
	private static final Logger log = Logger.getLogger(ParticipantController.class.getName());
	private HashMap<Integer, IParticipant> participants = new HashMap<Integer, IParticipant>();
	private int nextID = 0;
	private int maxParticipants = 10;
	private IMapController map;
	private IRepository repository;
	
	//Listener 
	private EventListenerList listeners = new EventListenerList();
	
	public ParticipantController(IMapController map){
		this.map = map;
		repository = Repository.getInstance();
		Properties properties = repository.getProperties("simulation");
		maxParticipants = Integer.parseInt(properties.getProperty("maxParticipants"));
	}
	
	/**
	 * Adds a car on the local position of a specific tile
	 * @param car Car
	 * @param tile Tile
	 * @param pos Position of the tile
	 */
	public int addParticipant(IParticipant participant, IVector tilePos, IAngle angle){
		if(participants.size() >= maxParticipants){
			log.log(Level.INFO, "Participant count reached max size(" + maxParticipants + ")");
			return -1;
		}
		
		ITile tile = map.getTile(tilePos);
		
		if(tile instanceof INegotiableTile){
			INegotiableTile negotiableTile = (INegotiableTile) tile;
			TrafficSystem system = negotiableTile.getTrafficSystem();
			PathSystem pathSystem = system.getPathSystem(participant.getPathType());
			
			if(pathSystem != null){
				participant.setId(nextID);
				participants.put(nextID, participant);
				
				Waypoint p = pathSystem.getStartpoint(angle);	
				Link l = p.getLink(Direction.AHEAD);
				IAngle rot = p.getPosition().angleToPoint(l.getCurve().getPoint(0.1f));
				participant.getTransform().updatePosition(p.getPosition(), rot, 0, l);
				
				log.log(Level.INFO, "Successfully added traffic participant " + "with id: " + nextID + " on Waypoint:\n " + p.toString());
				return nextID++;
			}
		}
		
		log.log(Level.INFO, "Couldn't add the traffic participant on this position");
		return -1;
	}

	/**
	 * Removes a participant
	 * @param participant Participant
	 */
	public void removeParticipant(IParticipant participant){
		removeParticipant(participant.getId());
	}	
	
	/**
	 * Removes participant based on its id
	 * @param id ID of participant
	 */
	public void removeParticipant(int id){
		IParticipant participant = participants.get(id);
		if(participant != null){
			ITransform t = participant.getTransform();
			t.getCurrentLink().removeParticipant(participant);
			participants.remove(id);
		}
	}
	
	/**
	 * Removes all participants which are contained in the map of participants passed to the method
	 * @param parts Map of Participants(key, participant)
	 */
	public void removeParticipants(Map<Integer, IParticipant> parts){
		for(int key : parts.keySet()){
			participants.remove(key);
		}
	}
	
	public Collection<IParticipant> getParticipantsAsCollection(){
		return participants.values();
	}
	
	public IParticipant getParticipantByID(Integer id) {
		return participants.get(id);
	}
	
	public void update(float deltaTime) {
		notifyEvent(new ParticipantsUpdatedEvent(this, getParticipantsAsCollection()));	
	}
	
	//Listener
	
	/**
     * Adds a Listener
     * @param listener Listener
     */
	public void addListener(EventListener listener){
		if(listener instanceof ParticipantUpdatedListener){
			listeners.add(ParticipantUpdatedListener.class, (ParticipantUpdatedListener) listener);
		}
	}

	/**
     * Removes a Listener
     * @param listener Listener
     */
	public void removeListener(EventObject listener){
		if(listener instanceof ParticipantUpdatedListener){
			listeners.remove(ParticipantUpdatedListener.class, (ParticipantUpdatedListener) listener);
		}
  	}

	private void notifyEvent(EventObject event){
		if (event instanceof ParticipantsUpdatedEvent){
		    for (ParticipantUpdatedListener l : listeners.getListeners(ParticipantUpdatedListener.class)){
		    	l.listen((ParticipantsUpdatedEvent) event);
		    }
		}
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    oos.writeObject(participants);
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    participants = (HashMap<Integer, IParticipant>) ois.readObject();
	}
}
