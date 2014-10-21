package simulation.participants;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.participants.enums.ParticipantType;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.IParticipantFactory;

/**
 * Encapsulates the creation of a participant
 */
public class ParticipantFactory implements IParticipantFactory {
	
	IRepository repository;
	
	public ParticipantFactory(){
		repository = Repository.getInstance();
	}
	
	/**
	 * Creates a participant by its type
	 * @param type Participant type
	 * @return Participant
	 */
	public IParticipant create(ParticipantType type){
		IParticipant participant = repository.buildParticipant(type);
		return participant;
	}

}
