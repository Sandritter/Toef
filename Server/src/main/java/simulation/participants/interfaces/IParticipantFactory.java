package simulation.participants.interfaces;

import simulation.participants.enums.ParticipantType;

public interface IParticipantFactory {

	public IParticipant create(ParticipantType type);

}