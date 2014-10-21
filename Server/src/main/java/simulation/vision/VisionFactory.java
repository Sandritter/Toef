package simulation.vision;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.python.core.PyDictionary;

import persistence.Repository;
import simulation.control.map.enums.CardinalDirection;
import simulation.control.map.interfaces.IMapController;
import simulation.math.interfaces.IAngle;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.tiles.interfaces.ITile;
import simulation.traffic.Link;
import simulation.vision.interfaces.IExtendedVisionCalculator;
import simulation.vision.interfaces.IExtendedVisionCalculatorTiles;
import simulation.vision.interfaces.IVision;
import simulation.vision.interfaces.IVisionCalculator;
import simulation.vision.interfaces.IVisionFactory;

/**
 * Creates the vision data for a participant
 */
public class VisionFactory implements IVisionFactory {

	private IMapController map;
	private int frontView = 5;
	private float sightRange = 100;
	
	private Map<String, IVisionCalculator> calculators;
	
	public VisionFactory(IMapController map){
		calculators = new HashMap<String, IVisionCalculator>();
		this.map = map;	
		Properties properties = Repository.getInstance().getProperties("simulation");
		sightRange = Float.parseFloat(properties.getProperty("sightRange")) + 10;
		frontView = (int) (sightRange / Float.parseFloat(properties.getProperty("trafficSystemSize")));
	}
		
	/**
	 * Adds a vision calculator to the vision factory
	 * @param name Name of calculator
	 * @param calc Calculator
	 */
	public void addCalculator(String name, IVisionCalculator calc){
		calculators.put(name, calc);
	}
	
	/**
	 * Removes a vision calculator from the vision factory
	 * @param name Name of calculator
	 */
	public void removeCalculator(String name){
		calculators.remove(name);
	}
	
	/**
	 * Enables a vision calculator
	 * @param name Name of calculator
	 * @param enabled Enabled
	 */
	public void enableCalculator(String name, boolean enabled){
		calculators.get(name).setEnabled(enabled);
	}
	
	/**
	 * Calculates the Vision of the participant
	 * @param participant Participant
	 */
	public IVision createVision(IParticipant participant){	
		start(participant);
		calculate(participant);
		
		PyDictionary dict = new PyDictionary();
		for (String desc: calculators.keySet()){
			dict.put(desc, calculators.get(desc).getResult());
		}
		
		return new Vision(dict);
	}
	
	/**
	 * Calls all calculators which implement the IVisionCalculator interface
	 * @param participant Participant
	 */
	private void start(IParticipant participant){
		for (IVisionCalculator calculator: calculators.values()){
			if (calculator instanceof IExtendedVisionCalculatorTiles){
				((IExtendedVisionCalculatorTiles)calculator).updateVisibleTiles(participant, calculateTilesInVision(participant));
			}
			calculator.start(participant, participant.getTransform().getCurrentLink());
		}
	}
	
	/**
	 * Calls all calculators which implement the IExtendedVisionCalculator interface
	 * @param participant Participant
	 */
	private void calculate(IParticipant p){
		Navigator navigator = new Navigator(p, sightRange, calculateTilesInVision(p));
		
		Link link;
		while(navigator.hasNext()){
			link = navigator.next();			
			for (IVisionCalculator calculator: calculators.values()){
				if (calculator instanceof IExtendedVisionCalculator){
					((IExtendedVisionCalculator)calculator).calculate(link);
				}
			}
		}
	}
	
	/**
	 * Calculates all tiles in the vision of a participant
	 * @param participant Participant
	 * @return Set of tiles
	 */
	private Set<INegotiableTile> calculateTilesInVision(IParticipant participant){
		//Calculate Tiles in Vision
		Set<INegotiableTile> validTiles = new HashSet<INegotiableTile>();
		ITile currentTile = participant.getTransform().getCurrentTile();
		IAngle rot = participant.getTransform().getRotation();
		CardinalDirection direc = CardinalDirection.getDirectionFromRotation(rot);

		for(int i = 0; i < frontView; i++){
			if(i == 0){
				addToList(validTiles, currentTile);
				addToList(validTiles, map.getTile(currentTile, direc.right()));
				addToList(validTiles, map.getTile(currentTile, direc.left()));
			} else {
				addToList(validTiles, currentTile);
			}
			
			currentTile = map.getTile(currentTile, direc);
			
			if(currentTile == null){
				break;
			}
		}
		
		return validTiles;
	}
	
	private void addToList(Set<INegotiableTile> set, ITile tile){
		if (tile instanceof INegotiableTile){
			INegotiableTile negTile = (INegotiableTile) tile;
			set.add(negTile);
		}
	}
}
