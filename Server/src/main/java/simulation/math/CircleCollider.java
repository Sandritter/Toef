package simulation.math;

import simulation.math.interfaces.ICircleCollider;
import simulation.math.interfaces.IVector;

/**
 * A circle collider which can check collisions with other circle colliders
 */
public class CircleCollider implements ICircleCollider {
	
	private IVector center;
	private float radius;
	private IVector translation;
	
	public CircleCollider(IVector center, float radius){
		this(center, radius, new Vector(0, 0));
	}
	
	public CircleCollider(IVector center, float radius, IVector translation){
		this.center = center;
		this.radius = radius;
		this.translation = translation;
	}
	
	/**
	 * Checks collision with an other circle
	 * @param circle other CollisionCircle
	 * @return True if a collision happened, else False
	 */
	public boolean collide(CircleCollider circle){
		IVector center2 = circle.center;
		float dist = center.sub(center2).length();
		if((radius + circle.radius) >= dist){
			return true;
		}

		return false;		
	}
	
	public float getRadius(){
		return radius;
	}

	public IVector getTranslation() {
		return translation;
	}

	public void setTranslation(IVector translation) {
		this.translation = translation;
	}
	
	public IVector getCenter() {
		return center;
	}

	public void setCenter(IVector center) {
		this.center = center;
	}
}
