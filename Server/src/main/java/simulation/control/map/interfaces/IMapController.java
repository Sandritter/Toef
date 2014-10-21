package simulation.control.map.interfaces;

import java.util.EventListener;
import java.util.EventObject;

import simulation.control.map.enums.CardinalDirection;
import simulation.control.participants.interfaces.IParticipantController;
import simulation.math.interfaces.IVector;
import simulation.tiles.interfaces.ITile;

public interface IMapController {

	public void reset();
	public void addTile(ITile tile, int posX, int posY);
	public void addTile(ITile tile);
	public void addTile(ITile tile, IVector pos);
	public void removeTile(IVector pos);
	public ITile[] getNeighborTiles(ITile tile);
	public void removeTile(int posX, int posY);
	public ITile getTile(int x, int y);
	public ITile getTile(IVector pos);
	public ITile getTile(ITile tile, CardinalDirection direc);
	public ITile getTile(IVector pos, CardinalDirection direc);
	public ITile[][] getMap();
	public ITile[][] getMapCopy();
	public int getSizeX();
	public int getSizeY();
	public void setParticipantController(IParticipantController controller);
	public void addListener(EventListener listener);
	public void removeListener(EventObject listener);

}