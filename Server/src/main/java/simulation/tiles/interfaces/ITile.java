package simulation.tiles.interfaces;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.tiles.enums.TileType;


public interface ITile {
	public TileType getType();
	public IVector getPosition();
	public void setRotation(IAngle rot);
	public void setPosition(IVector pos);
	public IAngle getRotation();
	public void rotateCW();
	public void rotateCC();
	public void resetRotation();
	public ITile copy();
	public int hashCode();
	public boolean equals(Object obj);
}
