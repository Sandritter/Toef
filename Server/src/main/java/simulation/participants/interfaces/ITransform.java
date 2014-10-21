package simulation.participants.interfaces;

import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.traffic.Link;
import simulation.traffic.PathSystem;
import simulation.traffic.enums.Direction;

public interface ITransform {

	public void updatePosition(IVector newPos, float distance, Link link);
	public void updatePosition(IVector newPos, IAngle rot, float distance, Link link);
	public void move(float deltaTime, float velocity, Direction nextDirection);
	public void setPosition(IVector pos);
	public IAngle getRotation();
	public void setRotation(IAngle rot);
	public Link getCurrentLink();
	public void setCurrentLink(Link currentLink);
	public float getDistance();
	public void setDistance(float distance);
	public PathSystem getCurrentPathSystem();
	public INegotiableTile getCurrentTile();
	public IVector getPosition();

}