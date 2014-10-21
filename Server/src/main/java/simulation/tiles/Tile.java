package simulation.tiles;

import simulation.math.Angle;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.tiles.enums.TileType;
import simulation.tiles.interfaces.ITile;

/**
 * Tile which can be placed in a map
 */
public abstract class Tile implements ITile {
	
	private TileType type;
	private IVector pos;
	private IAngle rot;

	public Tile(TileType type) {
		this(type, null, new Angle(0));
	}

	public Tile(TileType type, IVector pos) {
		this(type, pos, new Angle(0));
	}

	public Tile(TileType type, IVector pos, IAngle rot) {
		this.type = type;
		this.pos = pos;
		this.rot = rot;
	}
	
	/**
	 * Rotates the tile 90 degrees clockwise
	 */
	public void rotateCW() {
		rot = rot.add(90);
	}

	/**
	 * Rotates the tile 90 degrees counterclockwise
	 */
	public void rotateCC() {
		rot = rot.sub(90);
	}

	/**
	 * Resets the rotation of the tile to zero
	 */
	public void resetRotation() {
		rot.reset();
	}

	/**
	 * Returns the type of the tile
	 */
	public TileType getType() {
		return type;
	}

	/**
	 * Returns the tiles position in the world
	 */

	public IVector getPosition() {
		return pos;
	}

	/**
	 * Sets the tiles position in the world
	 */

	public void setPosition(IVector pos) {
		this.pos = pos;
	}

	/**
	 * Returns rotation of the tile
	 */
	public IAngle getRotation() {
		return rot;
	}
	
	public void setRotation(IAngle rot) {
		this.rot = rot;
	}

	public abstract ITile copy();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
}
