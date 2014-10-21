package simulation.control.events;

import java.util.EventObject;

import simulation.math.interfaces.IVector;

/**
 * Event that gets fired when a tile gets removed
 */
public class TileRemovedEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	private IVector pos;
	
	public TileRemovedEvent(Object source, IVector pos)
	{
		super(source);
		this.pos = pos;
	}

	public IVector getPosition() {
		return pos;
	}
}
