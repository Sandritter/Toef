package interLayer.validators.interfaces;

import interLayer.entities.Client;
import interLayer.entities.UpdateParticipant;

import java.util.Collection;
import java.util.List;

import simulation.participants.interfaces.IParticipant;

/**
 * interface for the client view validator
 */
public interface IClientViewValidator {
	
	public List<UpdateParticipant> validate(Client client, Collection<IParticipant> participants);
}
