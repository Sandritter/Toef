package simulation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import simulation.math.Angle;
import simulation.math.interfaces.IAngle;

public class AngleTest {

	@Test
	public void add() {
		IAngle a1 = new Angle(20);
		Angle a2 = new Angle(45);
		
		IAngle result = a1.add(a2);
		assertTrue(equalTo(65, result.getValue()));
		
		a1 = new Angle(359);
		a2 = new Angle(1);
		
		result = a1.add(a2);
		assertTrue(equalTo(0, result.getValue()));
		
		a1 = new Angle(359);
		a2 = new Angle(30);
		
		result = a1.add(a2);
		assertTrue(equalTo(29, result.getValue()));
		
		a1 = new Angle(350);
		a2 = new Angle(600);
		
		result = a1.add(a2);
		assertTrue(equalTo(230, result.getValue()));
	}
	
	@Test
	public void sub() {
		IAngle a1 = new Angle(20);
		Angle a2 = new Angle(14);
		
		IAngle result = a1.sub(a2);
		assertTrue(equalTo(6, result.getValue()));
		
		a1 = new Angle(5);
		a2 = new Angle(6);
		
		result = a1.sub(a2);
		assertTrue(equalTo(359, result.getValue()));
		
		a1 = new Angle(30);
		a2 = new Angle(60);
		
		result = a1.sub(a2);
		assertTrue(equalTo(330, result.getValue()));
		
		a1 = new Angle(30);
		a2 = new Angle(700);
		
		result = a1.sub(a2);
		assertTrue(equalTo(50, result.getValue()));
	}
	
	
	private boolean equalTo(float v1, float v2){
		float epsilon = 0.00000001f;
		return (Math.abs(v1 - v2) < epsilon);
	}
}
