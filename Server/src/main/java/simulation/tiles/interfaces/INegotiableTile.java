package simulation.tiles.interfaces;

import simulation.traffic.TrafficSystem;

/**
 * Interface that all negotiable tiles implement
 */
public interface INegotiableTile extends ITile {
	public void setTrafficSystem(TrafficSystem system);
	public TrafficSystem getTrafficSystem();
}
