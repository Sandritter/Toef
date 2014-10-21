package simulation.control.events.interfaces;

import java.util.EventListener;

import simulation.control.events.TileAddedEvent;

/**
 * Listener that gets called when a tile gets added
 */
public interface TileAddedListener extends EventListener{
	void listen(TileAddedEvent e);
}