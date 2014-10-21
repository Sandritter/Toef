package simulation.control.participants.interfaces;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import simulation.math.interfaces.IAngle;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;

public interface IParticipantController {

	public int addParticipant(IParticipant participant, IVector tilePos, IAngle angle);
	public void removeParticipant(IParticipant participant);
	public void removeParticipant(int id);
	public void removeParticipants(Map<Integer, IParticipant> parts);
	public Collection<IParticipant> getParticipantsAsCollection();
	public IParticipant getParticipantByID(Integer id);
	public void addListener(EventListener listener);
	public void removeListener(EventObject listener);

}