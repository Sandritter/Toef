package simulation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import simulation.math.CircleCollider;
import simulation.math.Vector;
import simulation.math.interfaces.ICircleCollider;

public class CollisionTest {

	@Test
	public void collide() {
		ICircleCollider circle1 = new CircleCollider(new Vector(1,0), 2);
		CircleCollider circle2 = new CircleCollider(new Vector(1,0), 2);

		assertTrue(circle1.collide(circle2));
		
		circle1 = new CircleCollider(new Vector(1, 0), 2);
		circle2 = new CircleCollider(new Vector(1.5f, 1.5f), 2);

		assertTrue(circle1.collide(circle2));
		
		circle1 = new CircleCollider(new Vector(5, 5), 2);
		circle2 = new CircleCollider(new Vector(8, 8), 2);

		assertFalse(circle1.collide(circle2));
		
		
	}
}
