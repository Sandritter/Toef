package simulation.vision.interfaces;

import org.python.core.PyObject;

import simulation.participants.interfaces.IParticipant;
import simulation.traffic.Link;

public interface IVisionCalculator {
	
	public void start(IParticipant participant, Link first);
	public PyObject getResult();
	public boolean isEnabled();
	public void setEnabled(boolean enabled);

}
