package simulation.control.events.interfaces;

import java.util.EventListener;

import simulation.control.events.ParticipantsUpdatedEvent;

/**
 * Listener that gets called when a traffic participant gets added
 */
public interface ParticipantUpdatedListener extends EventListener{
	void listen(ParticipantsUpdatedEvent e);
}