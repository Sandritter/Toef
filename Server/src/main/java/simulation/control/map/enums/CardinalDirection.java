package simulation.control.map.enums;

import simulation.math.Vector;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;

public enum CardinalDirection {
	NORTH(new Vector(0, 1)), 
	EAST(new Vector(1, 0)), 
	SOUTH(new Vector(0, -1)), 
	WEST(new Vector(-1, 0));

	private IVector vector;

	private CardinalDirection(IVector vector){
		this.vector = vector;
	}
	
	public IVector getVector(){
		return vector;
	}
	
	/**
	 * Turns the Direction to the right
	 * @return New Direction
	 */
	public CardinalDirection right(){
		CardinalDirection[] values = values();
		return values[(this.ordinal() + 1) % 4];
	}
	
	/**
	 * Turns the Direction to the left
	 * @return New Direction
	 */
	public CardinalDirection left(){
		CardinalDirection[] values = values();
		return values[this.ordinal() - 1 < 0 ? 3 : this.ordinal() - 1];
	}
	
	public CardinalDirection relativeDirection(CardinalDirection direc){
		if(direc == this){
			return CardinalDirection.NORTH;
		}
		
		IVector vOwn = vector;
		IVector vOther = direc.vector;
		float cross =  vOwn.cross(vOther);
		
		if(cross == -1){
			return CardinalDirection.EAST;
		} else if(cross == 1){
			return CardinalDirection.WEST;
		} else {
			return CardinalDirection.SOUTH;
		}
	}
	
	/**
	 * Returns the Direction that corrospondents to the given rotation
	 * @param rot Rotation
	 * @return New Direction
	 */
	public static CardinalDirection getDirectionFromRotation(IAngle rot){
		float value = rot.getValue();
		if (value > 315 || value <= 45){
			return NORTH;
		} else if(value > 45 && value <= 135){
			return EAST;
		} else if(value > 135 && value <= 215){
			return SOUTH;
		} else {
			return WEST;
		}
	}
}
