package simulation.math;
import simulation.math.interfaces.IAngle;

/**
 * Represents an angle
 */
public class Angle implements IAngle{
	
	private float value = 0;

	public Angle(){
		
	}
	
	public Angle(float angle){
		float v = angle % 360;	
		this.value = v < 0 ? 360 + v : v;
	}
	
	/**
	 * Calculates the result of the addition of two angles
	 * @param angle Angle
	 */
	public IAngle add(IAngle angle){
		return new Angle ((value + angle.getValue()) % 360);
	}
	
	/**
	 * Calculates the result of the addition of two angles
	 * @param angle Angle
	 */
	public IAngle add(float angle){
		return new Angle ((value + angle) % 360);
	}
	
	/**
	 * Calculates the result of the subtraction of two angles
	 * @param angle Angle
	 */
	public IAngle sub(IAngle angle){
		float v = (value - angle.getValue()) % 360;	
		return new Angle (v < 0 ? 360 + v : v);
	}
	
	/**
	 * Calculates the result of the subtraction of two angles
	 * @param angle Angle
	 */
	public IAngle sub(float angle){
		float v = (value - angle) % 360;	
		return new Angle (v < 0 ? 360 + v : v);
	}
	
	
	/**
	 * Returns the difference angle between to given angles
	 * @param angle Angle
	 */
	public IAngle differenceTo(IAngle angle){
		float value2 = angle.getValue();
		IAngle difference = new Angle(180 - Math.abs(Math.abs(value - value2) - 180)); 
		return difference;
	}
			
	/**
	 * Resets the angle
	 */
	public void reset() {
		this.value = 0;
	}
		
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}	
		
	/**
	 * Checks if 2 angles are equal
	 */
	public boolean equals(Object o){
	    if (o == null || !o.getClass().equals(getClass()))
	        return false;
	        	
	    if (o == this)
	        return true;
	      
	   Angle a = (Angle) o;
	   return equalTo(value, a.value);
	}
	
	private boolean equalTo(float vector1, float vector2){
		float epsilon = 0.00000001f;
		return (Math.abs(vector1 - vector2) < epsilon);
	}
	
	public IAngle copy(){
		return new Angle(value);
	}
	
	@Override
	public String toString() {
		return "(" + value +" Grad)";
	}
}
