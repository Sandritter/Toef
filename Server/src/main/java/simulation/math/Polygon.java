package simulation.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Polygon
 */
public class Polygon {
	
	private List<Vector> points;
	private List<Line> lines;
	private int size;
	private float length;
	
	public Polygon(int size){
		points =  new ArrayList<Vector>();
		lines = new ArrayList<Line>();
		this.size = size;
	}
	
	/**
	 * Adds a point to the polygon
	 * @param point Point
	 */
	public void addPoint(Vector point){
		if(lines.size() < size){
			points.add(point);
			int pos = points.size() - 1;
			
			if (pos > 0){
				Line l = new Line(points.get(pos - 1), point);
				lines.add(l);
				length += l.length();
				
				if (pos + 1 == size) {
					l = new Line(point, points.get(0));
					lines.add(l);
					length += l.length();
				}
			}
		}		
	}
	
	/**
	 * Checks collision with an other polygon
	 * @param poly Polygon
	 * @return List oft intersection points
	 */
	public List<Vector> checkCollision(Polygon poly) {
		List<Vector> intersections = new ArrayList<Vector>();
		for (Line l1:lines) {
			for (Line l2:poly.lines) {
				Vector intersection = l1.crossLineSegments2(l2);
				if (intersection != null){
					intersections.add(intersection);
				}
			}
		}
		return intersections;		
	}
	
	/**
	 * Checks collision with a line
	 * @param line Line
	 * @return List oft intersection points
	 */
	public List<Vector> checkCollision(Line line) {
		List<Vector> intersections = new ArrayList<Vector>();
		for (Line l1: lines){
			Vector intersection = l1.crossLineSegments(line);
			if (intersection != null){
				intersections.add(intersection);
			}
		}
		return intersections;		
	}
	
	public float length(){
		return length;
	}
}
