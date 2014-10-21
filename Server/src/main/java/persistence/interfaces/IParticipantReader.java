package persistence.interfaces;

import simulation.participants.enums.ParticipantType;
import simulation.participants.interfaces.IParticipant;

public interface IParticipantReader {

	public IParticipant readParticipant(String path, ParticipantType type);

}