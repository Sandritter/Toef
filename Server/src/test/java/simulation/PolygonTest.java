package simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import simulation.math.Polygon;
import simulation.math.Vector;

public class PolygonTest {

	@Test
	public void cross() {
		Polygon poly1 = new Polygon(4);
		poly1.addPoint(new Vector(0, 1));
		poly1.addPoint(new Vector(1, 1));
		poly1.addPoint(new Vector(1, 0));
		poly1.addPoint(new Vector(0, 0));
		
		Polygon poly2 = new Polygon(4);
		poly2.addPoint(new Vector(0.5f, 3));
		poly2.addPoint(new Vector(1.5f, 3));
		poly2.addPoint(new Vector(1.5f, 2));
		poly2.addPoint(new Vector(0.5f, 2));
		
		Polygon poly3 = new Polygon(4);
		poly3.addPoint(new Vector(0.5f, 1.5f));
		poly3.addPoint(new Vector(1.5f, 1.5f));
		poly3.addPoint(new Vector(1.5f, 0.5f));
		poly3.addPoint(new Vector(0.5f, 0.5f));
		
		assertEquals(0, poly1.checkCollision(poly2).size());
		
		List<Vector> intersections = poly1.checkCollision(poly3);
		assertEquals(new Vector(0.5f, 1.0f), intersections.get(0));
		assertEquals(new Vector(1.0f, 0.5f), intersections.get(1));
	}
	
	@Test
	public void length() {
		Polygon poly1 = new Polygon(4);
		poly1.addPoint(new Vector(0, 1));
		poly1.addPoint(new Vector(1, 1));
		poly1.addPoint(new Vector(1, 0));
		poly1.addPoint(new Vector(0, 0));
		
		Polygon poly2 = new Polygon(4);
		poly2.addPoint(new Vector(0, 1));
		poly2.addPoint(new Vector(5, 1));
		poly2.addPoint(new Vector(5, 0));
		poly2.addPoint(new Vector(0, 0));
		
		assertTrue(equalTo(4, poly1.length()));
		assertTrue(equalTo(12, poly2.length()));
	}
	
	private boolean equalTo(float v1, float v2){
		float epsilon = 0.00000001f;
		return (Math.abs(v1 - v2) < epsilon);
	}
}
