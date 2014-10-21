package simulation.vision;
import simulation.vision.interfaces.IVision;

/**
 * Relevant vision data
 */
public class Vision implements IVision {
	private Object info;
	
	public Vision(Object info){
		this.info = info;
	}
	
	public Object getData(){
		return info;
	}
}
