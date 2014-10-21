package simulation.math;
import java.io.Serializable;

import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;

/**
 * Represents a 2D Vector
 */
public class Vector implements Serializable, IVector{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3651125448338632925L;
	private float x;
	private float y;
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates the length of the Vector
	 */
	public float length(){
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	/**
	 * Calculates the square length of the Vector
	 */
	public float squareLength(){
		return (float) (Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	/**
	 * Calculates the result of the addition of two vectors
	 * @param vector Vector
	 * @return new Vector
	 */
	public Vector add(IVector vector){
		float vX = vector.getX();
		float vY = vector.getY();
		return new Vector(x + vX, y + vY);
	}
	
	/**
	 * Calculates the result of the subtraction of two vectors
	 * @param vector Vector
	 * @return new Vector
	 */
	public Vector sub(IVector vector){
		float vX = vector.getX();
		float vY = vector.getY();
		return new Vector(x - vX, y - vY);
	}
	
	/**
	 * Calculates the result of the multiplication with a scalar
	 * @param scalar Scalar
	 * @return new Vector
	 */
	public Vector mult(float scalar){
		return new Vector(x * scalar, y * scalar);
	}
	
	/**
	 * Calculates the result of the division with a scalar
	 * @param scalar Scalar
	 * @return new Vector
	 */
	public Vector div(float scalar){
		return new Vector(x / scalar, y / scalar);
	}
	
	/**
	 * Calculates the result of the dot product of two vectors
	 * @param Vector vector
	 */
	public float dot(IVector vector){
		float vX = vector.getX();
		float vY = vector.getY();
		return x * vX + y * vY;
	}
	
	/**
	 * Calculates the result of the cross of two vectors
	 * @param Vector vector
	 */
	public float cross(IVector vector){
		float vX = vector.getX();
		float vY = vector.getY();
		return (x * vY) - (y * vX);
	}
	
	/**
	 * Calculates the result of the normalization of the vector
	 * @return new Vector
	 */
	public IVector norm(){
		return div(length());
	}
	
	/**
	 * Calculates if both vectors are orthogonal to each other
	 * @param vector Vector
	 * @return new Vector
	 */
	public boolean orthogonalTo(IVector vector){
		return equalTo(dot(vector), 0);
	}
	
	/**
	 * Calculates the angle between two given vectors
	 * @param vector Vector
	 * @return Angle
	 */
	public IAngle angleBetween(IVector vector){
		float numerator = dot(vector);
		float denominator = length() * vector.length();
		float cos = numerator / denominator;
		return (new Angle((float)Math.toDegrees(Math.acos(cos))));
	}
	
	/**
	 * Calculates the angle between two given points
	 * @param vector Vector
	 * @return Angle
	 */
	public IAngle angleToPoint(IVector vector){
		float vX = vector.getX();
		float vY = vector.getY();
		double radians = Math.atan2(vX - x, vY - y);
		return (new Angle((float)Math.toDegrees(radians)));
	}
	
	public IVector rotateAround(IAngle angle, Vector center){
		float s = angle.getValue();			
		IVector d = this.sub(center);
		IVector pNew = new Vector((float) (center.getX()  + d.getX() * Math.cos(Math.toRadians(s)) - d.getY() * Math.sin(Math.toRadians(s))),
				(float) (center.getY() + d.getX() * Math.sin(Math.toRadians(s)) + d.getY() * Math.cos(Math.toRadians(s))));
		
		return pNew;		
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * Checks if 2 vectors are equal
	 */
	public boolean equals(Object o){
	    if (o == null || !o.getClass().equals(getClass()))
	        return false;
	        	
	    if (o == this)
	        return true;
	      
	   Vector v = (Vector) o;
	   return (equalTo(x, v.x) && equalTo(y, v.y));
	}
	
	@Override
	public String toString() {
		return "(" + x +" , " + y + ")";
	}
	
	public Vector copy(){
		return new Vector(x,y);
	}
	
	private boolean equalTo(float v1, float v2){
		float epsilon = 0.00001f;
		return (Math.abs(v1 - v2) < epsilon);
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public int getIntX(){
		return (int) x;
	}
	
	public int getIntY(){
		return (int) y;
	}
}
