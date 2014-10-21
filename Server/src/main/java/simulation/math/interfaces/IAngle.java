package simulation.math.interfaces;

public interface IAngle {

	public IAngle add(IAngle angle);
	public IAngle add(float angle);
	public IAngle sub(IAngle angle);
	public IAngle sub(float angle);
	public IAngle differenceTo(IAngle angle);
	public void reset();
	public float getValue();
	public void setValue(float value);
	public boolean equals(Object o);
	public IAngle copy();
	public String toString();

}