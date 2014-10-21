package simulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import simulation.math.Line;
import simulation.math.Vector;

public class LineTest {

	@Test
	public void cross() {
		// Crossing
		Line l1 = new Line(new Vector(0,0), (new Vector(1,1)));
		Line l2 = new Line(new Vector(0,1), (new Vector(1,0)));
		assertEquals(new Vector(0.5f, 0.5f), l1.cross(l2));

		// Parallel
		l1 = new Line(new Vector(0,2), (new Vector(1,2)));
		l2 = new Line(new Vector(0,1), (new Vector(1,1)));
		assertEquals(null, l1.cross(l2));
		
		// Identical
		l1 = new Line(new Vector(1,1), (new Vector(1,1)));
		l2 = new Line(new Vector(1,1), (new Vector(1,1)));
		assertEquals(null, l1.cross(l2));
	}
	
	@Test
	public void crossLineSegments() {
		// Crossing
		Line l1 = new Line(new Vector(0,0), (new Vector(1,1)));
		Line l2 = new Line(new Vector(0.5f,0), (new Vector(0.5f,1)));
		assertEquals(new Vector(0.5f, 0.5f), l1.crossLineSegments(l2));
		
		// Crossing
		l1 = new Line(new Vector(0, 2), (new Vector(2, 0)));
		l2 = new Line(new Vector(0, 1.0f), (new Vector(3.0f, 1.0f)));
		assertEquals(new Vector(1.0f, 1.0f), l1.crossLineSegments(l2));
		
		// No Crossing
		l1 = new Line(new Vector(0, 1.0f), (new Vector(0.9f, 1.0f)));
		l2 = new Line(new Vector(0, 2), (new Vector(2, 0)));
		assertEquals(null, l1.crossLineSegments(l2));
		
		// No Crossing
		l1 = new Line(new Vector(0,0), (new Vector(1,1)));
		l2 = new Line(new Vector(1.1f, 0), (new Vector(1.1f,1)));
		assertEquals(null, l1.crossLineSegments(l2));
		
		// No Crossing
		l1 = new Line(new Vector(0, 0), (new Vector(1,1)));
		l2 = new Line(new Vector(-0.1f, 0), (new Vector(-0.1f, 1)));
		assertEquals(null, l1.crossLineSegments(l2));
	}
	
	@Test
	public void crossLineSegments2() {
		// Crossing
		Line l1 = new Line(new Vector(0,0), (new Vector(1,1)));
		Line l2 = new Line(new Vector(0.5f,0), (new Vector(0.5f, 1)));
		assertEquals(new Vector(0.5f, 0.5f), l1.crossLineSegments2(l2));

		// No Crossing
		l1 = new Line(new Vector(0, 2), (new Vector(2, 0)));
		l2 = new Line(new Vector(0, 1.0f), (new Vector(0.9f, 1.0f)));
		assertEquals(null, l1.crossLineSegments2(l2));
	}
}
