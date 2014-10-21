package persistence.interfaces;

import simulation.tiles.enums.TileType;
import simulation.traffic.TrafficSystem;

public interface ITrafficSystemReader {
	public TrafficSystem readTrafficSystem(String path, TileType type, int size, int unit);
}
