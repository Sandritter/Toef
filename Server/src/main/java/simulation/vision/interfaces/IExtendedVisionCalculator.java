package simulation.vision.interfaces;

import simulation.traffic.Link;

public interface IExtendedVisionCalculator extends IVisionCalculator{
	
	public void calculate(Link next);

}
