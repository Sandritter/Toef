package simulation.math.interfaces;


public interface ICurve {
	
	public IVector getPoint(float t);
	public IVector first();
	public IVector last();
	public IVector pointByIndex(int i);
	public float getLength();	
	public int getSize();	
	public ICurve copy();
	public void translate(IVector delta);
	public void rotate(IAngle angle, IVector center);
}
