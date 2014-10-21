package simulation.participants.interfaces;

import simulation.vision.interfaces.IVision;

/**
 * Interface for Behaviour scripts
 */
public interface IBehaviour {
	public void setParticipant(IParticipantProxy participant);
	public void update(IVision vision);
}
