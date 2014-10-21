package simulation.vision.interfaces;

import simulation.participants.interfaces.IParticipant;

public interface IVisionFactory {

	public void addCalculator(String name, IVisionCalculator calc);
	public void removeCalculator(String description);
	public void enableCalculator(String description, boolean enabled);
	public IVision createVision(IParticipant p);

}