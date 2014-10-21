package simulation.items.interfaces;

import simulation.items.enums.ItemType;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;

public interface IItem {
	public ItemType getType();
	public void setType(ItemType type);
	public IAngle getRotation();
	public void setRotation(IAngle rotation);
	public IVector getPosition();
	public void setPosition(IVector position);
	public float getValue();
	public void setValue(float value);
}
