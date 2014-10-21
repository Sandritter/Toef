package simulation.math;

import java.util.ArrayList;
import java.util.List;

import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.ICurve;
import simulation.math.interfaces.IVector;

/**
 * Bezier curve
 */
public class BezierCurveNP implements ICurve{
	
	private List<IVector> points = new ArrayList<IVector>();
	private float approxLength;
	
	public BezierCurveNP(IVector p1, IVector p2){
		points.add(p1);
		points.add(p2);
		calculateLength();
	}
	
	public BezierCurveNP(IVector p1, IVector p2, IVector p3){
		points.add(p1);
		points.add(p2);
		points.add(p3);
		calculateLength();
	}
	
	public BezierCurveNP(IVector p1, IVector p2, IVector p3, IVector p4){
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		calculateLength();
	}
	
	public BezierCurveNP(IVector p1, List<IVector> controlPoints, IVector p2){
		points.add(p1);
		points.addAll(controlPoints);
		points.add(p2);
		calculateLength();
	}
	
	private BezierCurveNP(List<IVector> points){
		this.points = points;
		calculateLength();
	}
	
	/**
	 * Calculates the length of the bezier curve
	 */
	private void calculateLength(){
		float t = 0;
		float inc = 0.05f;
		IVector v0 = getPoint(t);
		
		while(t <= 1.0f){
			t += inc;
			IVector v1 = getPoint(t);
			approxLength += v1.sub(v0).length();
			v0 = v1;
		}
	}

	/**
	 * Returns the position on the bezier curve defined by t element of [0,1]
	 * @param t value between 0 and 1
	 */
	public IVector getPoint(float t) {
		t = adjustRange(t);
		
		IVector v = new Vector(0, 0);
		int n = points.size() - 1;
		for(int i = 0; i <= n; i++){
			if(i == 0 || i == n){
				v = v.add(points.get(i).mult((float) (Math.pow(t, i) * Math.pow(1 - t, n - i))));
			} else {
				v = v.add(points.get(i).mult(n * (float) (Math.pow(t, i) * Math.pow(1 - t, n - i))));
			}
		}
		return v;
	}

	/**
	 * Returns the first point in the bezier curve
	 * @return Vector First point
	 */
	public IVector first() {
		return points.get(0);
	}

	/**
	 * Returns the last point in the bezier curve
	 * @return Vector Last point
	 */
	public IVector last() {
		return points.get(points.size() - 1);
	}
	
	/**
	 * Returns the point on the given index
	 * @param i index
	 * @return Vector Point
	 */
	public IVector pointByIndex(int i) {
        return points.get(i);
	}
	
	/**
	 * Translates the bezier curve by the given delta
	 * @param delta Delta
	 */
	public void translate(IVector delta) {
		for(int i = 0; i < points.size(); i++){
			points.set(i, points.get(i).add(delta));
		}
	}
		
	/**
	 * Rotates the bezier curve by the given angle around the given center
	 * @param angle Angle
	 * @param center Center
	 */
	public void rotate(IAngle angle, IVector center) {
		float s = 360 - angle.getValue();
		
		for(int i = 0; i < points.size(); i++){
			IVector p = points.get(i);
			IVector d = p.sub(center);
			points.set(i, new Vector((float) (center.getX()  + d.getX() * Math.cos(Math.toRadians(s)) - d.getY() * Math.sin(Math.toRadians(s))),
					(float) (center.getY() + d.getX() * Math.sin(Math.toRadians(s)) + d.getY() * Math.cos(Math.toRadians(s)))));
		}
	}
	
	/**
	 * Adjust the range of the parameter to be in range [0,1]
	 * @param i Value to be adjusted
	 * @return adjusted value
	 */
	private float adjustRange(float i){
		if (i > 1){
			return 1;
		}
		
		if (i < 0){
			return 0;
		}
		
		return i;
	}
	
	public int getSize() {
		return points.size();
	}
	
	public float getLength(){
		return approxLength;
	}
	
	public ICurve copy(){
		List<IVector> vectorCopies = new ArrayList<IVector>();
		for(IVector p : points){
			vectorCopies.add(p.copy());
		}
		return new BezierCurveNP(vectorCopies);
	}
}
