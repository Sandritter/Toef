package simulation.control.events;

import java.util.Collection;
import java.util.EventObject;

import simulation.participants.interfaces.IParticipant;

/**
 * Event that gets fired after all traffic participants where updated
 */
public class ParticipantsUpdatedEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	private Collection<IParticipant> participants;
	
	public ParticipantsUpdatedEvent(Object source, Collection<IParticipant> participants)
	{
		super(source);
		this.participants = participants;
	}

	public Collection<IParticipant> getParticipants() {
		return participants;
	}
}
