package simulation.math.interfaces;

import simulation.math.Vector;


public interface IVector {

	public float length();
	public float squareLength();
	public IVector add(IVector vector);
	public IVector sub(IVector vector);
	public IVector mult(float scalar);
	public IVector div(float scalar);
	public float dot(IVector vector);
	public float cross(IVector vector);
	public IVector norm();
	public boolean orthogonalTo(IVector vector);
	public IAngle angleBetween(IVector vector);
	public IAngle angleToPoint(IVector vector);
	public IVector rotateAround(IAngle angle, Vector center);
	public boolean equals(Object o);
	public int hashCode();
	public String toString();
	public IVector copy();
	public float getX();
	public float getY();
	public int getIntX();
	public int getIntY();
	

}