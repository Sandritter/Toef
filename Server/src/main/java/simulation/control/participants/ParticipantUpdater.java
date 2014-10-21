package simulation.control.participants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simulation.control.interfaces.IUpdatable;
import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.interfaces.IParticipantController;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.ITransform;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.tiles.interfaces.ITile;
import simulation.vision.NextJunctionCalculator;
import simulation.vision.NextParticipantCalculator;
import simulation.vision.NextPropertiesCalculator;
import simulation.vision.VisibleParticipantsCalculator;
import simulation.vision.VisionFactory;
import simulation.vision.interfaces.IVisionFactory;

/**
 * Updates each traffic participant every frame
 */
public class ParticipantUpdater implements IUpdatable{

	private IParticipantController manager;
	private IMapController map;
	private Collection<IParticipant> participants;
	private IVisionFactory factory;
	
	public ParticipantUpdater(IParticipantController manager, IMapController map) {
		this.manager = manager;
		this.map = map;
		this.factory = new VisionFactory(map);
		
		//VisionCalculator hinzufügen
		factory.addCalculator("NextParticipant", new NextParticipantCalculator());
		factory.addCalculator("NextJunction", new NextJunctionCalculator());
		factory.addCalculator("VisibleParticipants", new VisibleParticipantsCalculator(map));
		factory.addCalculator("NextProperties", new NextPropertiesCalculator());
	}
	
	/**
	 * Is called every frame
	 */
	public void update(float deltaTime) {
		participants = manager.getParticipantsAsCollection();	
		for(IParticipant p : participants){
			if(!p.getDisabled()){	
				checkCollision(p);
				p.setVision(factory.createVision(p));
				p.update(deltaTime);
			}
		}
	}
	
	/**
	 * Checks Collision with other traffic participants
	 */
	private void checkCollision(IParticipant p){
		List<IParticipant> relevantCars = new ArrayList<IParticipant>();
		ITransform t = p.getTransform();
		
		int layer = t.getCurrentPathSystem().getLayer();
		for(ITile tile : map.getNeighborTiles(t.getCurrentTile())){
			if(tile instanceof INegotiableTile){
				INegotiableTile negotiableTile = (INegotiableTile) tile;
				relevantCars.addAll(negotiableTile.getTrafficSystem().getParticipantsAsCollection(layer));
			}
		}
		
		relevantCars.addAll(t.getCurrentTile().getTrafficSystem().getParticipantsAsCollection(layer));
		for(IParticipant p2 : relevantCars){
			if(p != p2){
				p.collideWith(p2);
			}
		}
	}	
}
