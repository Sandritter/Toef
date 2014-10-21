package simulation.tiles.interfaces;

import simulation.math.Angle;
import simulation.math.Vector;
import simulation.tiles.enums.TileType;

public interface ITileFactory {

	public ITile create(TileType type, Vector pos, Angle rot);

}