package persistence.interfaces;

import simulation.participants.enums.DriverType;
import simulation.participants.enums.ParticipantUpperType;
import simulation.participants.interfaces.IBehaviour;

public interface IBehaviourReader {

	public IBehaviour read(String path, ParticipantUpperType type, DriverType driverType);
}