package simulation.vision.interfaces;

import java.util.Set;

import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.INegotiableTile;

public interface IExtendedVisionCalculatorTiles extends IVisionCalculator{
	
	public void updateVisibleTiles(IParticipant participant, Set<INegotiableTile> tiles);

}
