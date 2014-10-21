package simulation.tiles;

import simulation.math.Angle;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.tiles.enums.TileType;

/**
 * Landscape tiles provide no interaction with participants
 */
public class Landscape extends Tile{
	
	public Landscape(TileType type) {
		this(type, null, new Angle(0));
	}

	public Landscape(TileType type, IVector pos) {
		this(type, pos, new Angle(0));
	}

	public Landscape(TileType type, IVector pos, IAngle rot) {
		super(type, pos, rot);
	}

	public Landscape copy(){
		return new Landscape(getType(), getPosition().copy(), getRotation().copy());
	}

}
