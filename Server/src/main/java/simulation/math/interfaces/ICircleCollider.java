package simulation.math.interfaces;

import simulation.math.CircleCollider;

public interface ICircleCollider {

	/**
	 * Checks collision with an other circle
	 * @param circle other CollisionCircle
	 * @return True if a collision happened, else False
	 */
	public boolean collide(CircleCollider circle);

	public float getRadius();

	public IVector getTranslation();

	public void setTranslation(IVector translation);

	public IVector getCenter();

	public void setCenter(IVector center);

}