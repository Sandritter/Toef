package simulation.participants.interfaces;

import simulation.math.CircleCollider;

public interface IHull {

	public boolean collide(IHull hull);

	public float getLength();

	public void setLength(float length);

	public float getWidth();

	public void setWidth(float width);

	public CircleCollider getCollider();

	public void setCollider(CircleCollider collider);

}