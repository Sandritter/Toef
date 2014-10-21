package simulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import simulation.math.BezierCurveNP;
import simulation.math.Vector;
import simulation.math.interfaces.ICurve;

public class BezierCurveTest {

	@Test
	public void position() {
		ICurve bezier = new BezierCurveNP(new Vector(1,2), new Vector(1,0));
		
		assertEquals(new Vector(1, 2), bezier.getPoint(0));
		assertEquals(new Vector(1, 0), bezier.getPoint(1));
	}
}
