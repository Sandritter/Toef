package simulation.items;

import simulation.items.enums.ItemType;
import simulation.items.interfaces.IItem;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;

/**
 * Items can be placed on top of tiles, providing additional interaction with participants
 */
public class Item implements IItem {

	private ItemType type;
	private IAngle rotation;
	private IVector position;
	private float value;
	
	public Item(ItemType type, float value, IVector position, IAngle rotation) {
		this.setType(type);
		this.value = value;
		this.position = position;
		this.rotation = rotation;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public IAngle getRotation() {
		return rotation;
	}

	public void setRotation(IAngle rotation) {
		this.rotation = rotation;
	}
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public IVector getPosition() {
		return position;
	}

	public void setPosition(IVector position) {
		this.position = position;
	}
}
