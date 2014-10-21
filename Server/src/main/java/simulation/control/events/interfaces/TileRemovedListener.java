package simulation.control.events.interfaces;

import java.util.EventListener;

import simulation.control.events.TileRemovedEvent;

/**
 * Listener that gets called when a tile gets removed
 */
public interface TileRemovedListener extends EventListener{
	void listen(TileRemovedEvent e);
}