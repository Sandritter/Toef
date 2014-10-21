package simulation.math;

import simulation.math.interfaces.IVector;

/**
 * Line between two points
 */
public class Line {
	private Vector p1;
	private IVector p2;
	
	public Line(Vector p1, IVector p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	/**
	 * Calculates the intersection point with the given line
	 * @param line line
	 */
	public Vector cross(Line line){
		float numerator = -p2.getX() * line.p1.getX() * p1.getY() + p2.getX() * line.p2.getX() * p1.getY() + p1.getX() * line.p1.getX() * p2.getY() 
				- p1.getX() * line.p2.getX() * p2.getY() + p1.getX() * line.p2.getX() * line.p1.getY() - p2.getX() * line.p2.getX() * line.p1.getY() 
				- p1.getX() * line.p1.getX() * line.p2.getY() + p2.getX() * line.p1.getX() *line.p2.getY();
		float denominator = -line.p1.getX() * p1.getY() + line.p2.getX() * p1.getY() + line.p1.getX() * p2.getY() 
				- line.p2.getX() * p2.getY() + p1.getX() * line.p1.getY() - p2.getX() * line.p1.getY() 
				- p1.getX() * line.p2.getY() + p2.getX() * line.p2.getY();
		
		float x = numerator / denominator;
		float y = 0;
		
		if ((p2.getX() - p1.getX()) != 0)
			y = ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * (x - p1.getX()) + p1.getY();
		else if((line.p2.getX() - line.p1.getX()) != 0)
			y = ((line.p2.getY() - line.p1.getY()) / (line.p2.getX() - line.p1.getX())) * (x - line.p1.getX()) + line.p1.getY();
		else
			return null;
		
		if (Float.isNaN(x) || Float.isNaN(y))
			return null;

		return new Vector(x, y);
	}
		
	/**
	 * Calculates the intersection point of the line segment with the given line
	 * @param line line
	 */
	public Vector crossLineSegments(Line line) {
		Vector s = cross(line);
		if (s == null)
			return null;
		
		IVector ba = getVector();
		Vector ca = s.sub(p1);
		
		float dot = ba.dot(ca);
		if (dot < 0){
			return null;
		}
		
		float length = ba.squareLength();
		if (dot > length)
			return null;
		
		return s;
	}
	
	/**
	 * Calculates the intersection point of the line segment with the given line segment
	 * @param line line
	 */
	public Vector crossLineSegments2(Line line){
		IVector r = p2.sub(p1);
		IVector s = line.p2.sub(line.p1);
        
        float numeratorT = line.p1.sub(p1).cross(s);
        float numeratorU = line.p1.sub(p1).cross(r);
        float denominator = r.cross(s);
        
        //Parallel or same
        if(denominator == 0){
        	return null;
        }
        
        float t = numeratorT / denominator;
        float u = numeratorU / denominator;
        
        if(t >= 0 && t <= 1 && u >= 0 && u <= 1){
        	return p1.add(r.mult(t));
        }
        
        return null;
	}
	
	public float length(){
		return getVector().length();
	}
		
	public IVector getVector(){
		return p2.sub(p1);
	}
}
