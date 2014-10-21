package simulation.tiles;

import simulation.math.Angle;
import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.tiles.enums.TileType;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.traffic.TrafficSystem;

/**
 * Participants can move on negotiable tiles
 */
public class Negotiable extends Tile implements INegotiableTile{

	private TrafficSystem trafficSystem;

	public Negotiable(TileType type, TrafficSystem system) {
		this(type, null, new Angle(0), system);
	}

	public Negotiable(TileType type, IVector pos, TrafficSystem system) {
		this(type, pos, new Angle(0), system);
	}

	public Negotiable(TileType type, IVector pos, IAngle rot, TrafficSystem system) {
		super(type, pos, rot);
		
		if (system != null){
			initTrafficSystem(system);
			system.setTile(this);
		}
	}
	
	/**
	 * Rotates and translates the traffic system 
	 * @param system Traffic system
	 */
	private void initTrafficSystem(TrafficSystem system){
		IAngle rot = getRotation();
		IVector pos = getPosition();
	
		if(rot.getValue() != 0 && pos.getX() != 0 || pos.getY() != 0){
			system.translateAndRotate(pos, rot);
		} else if(rot.getValue() != 0){
			system.rotate(rot);
		} else if(pos.getX() != 0 || pos.getY() != 0){
			system.translate(pos);
		}
		this.trafficSystem = system;
	}
	
	public void setTrafficSystem(TrafficSystem system){
		this.trafficSystem = system;
	}
	
	public TrafficSystem getTrafficSystem(){
		return trafficSystem;
	}

	public Negotiable copy(){
		return new Negotiable(getType(), getPosition().copy(), getRotation().copy(), null);
	}


}
