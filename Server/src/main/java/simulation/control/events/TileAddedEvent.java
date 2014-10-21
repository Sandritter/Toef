package simulation.control.events;

import java.util.EventObject;

import simulation.tiles.interfaces.ITile;

/**
 * Event that gets fired when a tile gets added
 */
public class TileAddedEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	private ITile tile;
	
	public TileAddedEvent(Object source, ITile tile)
	{
		super(source);
		this.tile = tile;
	}

	public ITile getTile() {
		return tile;
	}
}
