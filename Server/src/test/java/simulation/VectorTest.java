package simulation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import simulation.math.Vector;
import simulation.math.interfaces.IVector;

public class VectorTest {
	IVector v = new Vector(20, 20);
	
	@Test
	public void equal() {
		IVector v1 = new Vector(20.0005f, 34.4230f);
		IVector v2 = new Vector(20.0005f, 34.4230f);
		
		v1.mult(20.20f);
		v2.mult(20.20f);
		
		v1.div(20.20f);
		v2.div(20.20f);
		
		assertTrue(v1.equals(v2));
		
		v1 = new Vector(230.123f, 40.123f);
		v2 = new Vector(230.1234f, 40.1234f);
		
		assertFalse(v1.equals(v2));
		
		for (int i = 0; i < 10; i++){
			float x = (float) Math.random();
			float y = (float) Math.random();
			
			v1 = new Vector(x, y);
			v2 = new Vector(x, y);
			
			v1.mult(123.123123f);
			v2.mult(123.123123f);
			
			v1.div(120.234126f);
			v2.div(120.234126f);
			
			assertTrue(v1.equals(v2));
			
			v1 = v2.mult(0.0015f);
			
			assertFalse(v1.equals(v2));
		}		
	}

	@Test
	public void add() {
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				IVector v1 = new Vector(i, i + j);
				Vector v2 = new Vector(j, j + i);
				IVector result = v1.add(v2);
				assertTrue(equalTo(i + j, result.getX()));
				assertTrue(equalTo((i + j) + (j + i), result.getY()));
			}
		}
		
		for (int i = 0; i < 10; i++){
			float x1 = (float) Math.random();
			float x2 = (float) Math.random();
			float y1 = (float) Math.random();
			float y2 = (float) Math.random();
			
			IVector v1 = new Vector(x1, y1);
			Vector v2 = new Vector(x2, y2);
			IVector result = v1.add(v2);
			assertTrue(equalTo(x1 + x2, result.getX()));
			assertTrue(equalTo(y1 + y2, result.getY()));
		}		
	}
	
	@Test
	public void norm(){
		float x = 20.50f;
		float y = 370.75f;

		float length = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		float nX = x / length;
		float nY = y / length;
		
		IVector v = new Vector(x, y);
		
		assertTrue(equalTo(nX, v.norm().getX()));
		assertTrue(equalTo(nY, v.norm().getY()));
	}
	
	@Test
	public void dot(){
		float x1 = 5;
		float x2 = 10;
		float y1 = 165;
		float y2 = 230;
		IVector v1 = new Vector(x1, y1);
		Vector v2 = new Vector(x2, y2);
		
		float result = v1.dot(v2);
		
		assertTrue(equalTo((x1 * x2) + (y1 * y2), result));
	}
	
	@Test
	public void orthogonal(){
		IVector v1 = new Vector(2, 2);
		Vector v2 = new Vector(-2, 2);
		
		assertTrue(v1.orthogonalTo(v2));
		
		v1 = new Vector(2, 2);
		v2 = new Vector(-2, 3);
		
		assertFalse(v1.orthogonalTo(v2));
	}
	
	private boolean equalTo(float v1, float v2){
		float epsilon = 0.00000001f;
		return (Math.abs(v1 - v2) < epsilon);
	}

}
