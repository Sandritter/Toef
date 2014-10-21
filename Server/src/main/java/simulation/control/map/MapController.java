package simulation.control.map;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.events.TileAddedEvent;
import simulation.control.events.interfaces.TileAddedListener;
import simulation.control.interfaces.ISimulation;
import simulation.control.map.enums.CardinalDirection;
import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.interfaces.IParticipantController;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;
import simulation.tiles.Landscape;
import simulation.tiles.Negotiable;
import simulation.tiles.enums.TileType;
import simulation.tiles.interfaces.INegotiableTile;
import simulation.tiles.interfaces.ITile;
import simulation.traffic.TrafficSystem;

/**
 * Represents the world composed of tiles
 */
public class MapController implements IMapController{
	private static final Logger log = Logger.getLogger(MapController.class.getName());
	
	//Map size
	private int sizeX = 50;
	private int sizeY = 50;

	//Map
	private ITile[][] map = new ITile[sizeX][sizeY];
	private TileType standardType = TileType.GRASS;
	
	//Repository
	private IRepository repository;
	private ISimulation simulation;
	private IParticipantController participantController;
	
	//Listener 
	private EventListenerList listeners = new EventListenerList();
	
	public MapController(ISimulation simulation){
		this.simulation = simulation;
		repository = Repository.getInstance();
		Properties properties = repository.getProperties("simulation");
		
		sizeX = Integer.parseInt(properties.getProperty("mapSizeX"));
		sizeY = Integer.parseInt(properties.getProperty("mapSizeY"));
		standardType = TileType.valueOf(properties.getProperty("standardType"));
		
		log.log(Level.INFO, "Map initiated with size X = " + sizeX + ", size Y = " + sizeY + 
				", standard type = " + standardType.getText());
		reset();
	}
	
	/**
	 * Fills the world with grass
	 */
	public void reset(){
		for (int x = 0; x < sizeX; x++){
			for(int y = 0; y <sizeY; y++){
				map[x][y] = new Landscape(standardType, new Vector(x, y));
			}
		}
	}
	
	/**
	 * Adds a tile to the world on a specific position
	 * @param tile Tile
	 * @param posX X position of the tile
	 * @param posY Y position of the tile
	 */
	public void addTile(final ITile tile, int posX, int posY){
		addTile(tile, new Vector(posX, posY));
	}
	
	/**
	 * Adds a tile to the world
	 * @param tile Tile
	 */
	public void addTile(final ITile tile){
		addTile(tile, tile.getPosition());
	}
	
	/**
	 * Adds a tile to the world on a specific position
	 * @param tile Tile
	 * @param pos Position of the tile
	 */
	public void addTile(final ITile tile, IVector pos){
		tile.setPosition(pos);
		ITile oldTile = map[pos.getIntX()][pos.getIntY()];
		
		map[pos.getIntX()][pos.getIntY()] = tile;
		
		if(oldTile instanceof INegotiableTile){
			INegotiableTile oldTileNegotiable = (INegotiableTile) oldTile;
			participantController.removeParticipants(oldTileNegotiable.getTrafficSystem().getParticipants());
			undockStreets((INegotiableTile) oldTileNegotiable);
		}
		
		if(tile instanceof INegotiableTile){
			dockStreets((INegotiableTile) tile);
		}
		
		//Notify listener
		simulation.enqueueLate(new Runnable() {		
			public void run() {
				notifyEvent(new TileAddedEvent(this, tile));				
			}
		});
	}
	
	/**
	 * Removes a tile on a specific position
	 * @param posX X position of the tile
	 * @param posY Y position of the tile
	 */
	public void removeTile(int posX, int posY){
		removeTile(new Vector(posX, posY));
	}
			
	/**
	 * Removes a tile on a specific position
	 * @param pos Position of the tile
	 */
	public void removeTile(IVector pos){
		ITile oldTile = map[pos.getIntX()][pos.getIntY()];
		
		if(oldTile instanceof INegotiableTile){
			INegotiableTile oldTileNegotiable = (INegotiableTile) oldTile;
			participantController.removeParticipants(oldTileNegotiable.getTrafficSystem().getParticipants());
			undockStreets((INegotiableTile) oldTileNegotiable);
		}
		
		final ITile newTile = new Landscape(standardType, pos);
		map[pos.getIntX()][pos.getIntY()] = newTile;
		
		//Notify listener
		simulation.enqueueLate(new Runnable() {		
			public void run() {
				notifyEvent(new TileAddedEvent(this, newTile));				
			}
		});
	}
	
	/**
	 * Connects the street to its neighboor streets
	 * @param street Street
	 */
	private void dockStreets(INegotiableTile street){
		INegotiableTile neighborStreet;
		TrafficSystem streetSystem =  street.getTrafficSystem();
		ITile[] tiles = getNeighborTiles(street);
		
		for (int i = 0; i < tiles.length; i++){
			ITile tile = tiles[i];
			
			if(tile instanceof INegotiableTile){
				neighborStreet = (INegotiableTile) tile;
				streetSystem.dock(neighborStreet.getTrafficSystem());
			}
		}
	}
	
	/**
	 * Unconnects the street from its neighboor streets
	 * @param street Street
	 */
	private void undockStreets(INegotiableTile street){
		INegotiableTile neighborStreet;
		TrafficSystem streetSystem =  street.getTrafficSystem();
		ITile[] tiles = getNeighborTiles(street);
		
		for (int i = 0; i < tiles.length; i++){
			ITile tile = tiles[i];
			
			if(tile instanceof Negotiable){
				neighborStreet = (Negotiable) tile;
				streetSystem.undock(neighborStreet.getTrafficSystem());
			}
		}
	}
	
	/**
	 * Returns the neighbor tiles of the passed tile 
	 * @param tile Tile
	 * @return Array of neighbor tiles
	 */
	public ITile[] getNeighborTiles(ITile tile){
		ITile[] tiles = new ITile[4];
		tiles[0] = getTile(tile, CardinalDirection.NORTH); // North
		tiles[1] = getTile(tile, CardinalDirection.EAST); // East
		tiles[2] = getTile(tile, CardinalDirection.SOUTH); // South
		tiles[3] = getTile(tile, CardinalDirection.WEST);  // West
		return tiles;
	}
					
	/**
	 * Returns a tile on a specific position
	 * @param posX X position of the tile
	 * @param posY Y position of the tile
	 */
	public ITile getTile(int x, int y){
		return map[x][y];
	}
	
	/**
	 * Returns a tile on a specific position
	 * @param pos Position
	 */
	public ITile getTile(IVector pos){
		return map[pos.getIntX()][pos.getIntY()];
	}
	
	/**
	 * Returns a tile in a specific direction of an other tile
	 * @param pos Position
	 * @param direc Direction
	 */
	public ITile getTile(ITile tile, CardinalDirection direc){
		return getTile(tile.getPosition(), direc);
	}
	
	/**
	 * Returns a tile in a specific direction of an other tile
	 * @param pos Position
	 * @param direc Direction
	 */
	public ITile getTile(IVector pos, CardinalDirection direc){
		int x = pos.getIntX();
		int y = pos.getIntY();
		
		if (direc == CardinalDirection.NORTH){
			return y+1 < sizeY ? map[x][y+1] : null; 
		} else if (direc == CardinalDirection.EAST){
			return x+1 < sizeX ? map[x+1][y] : null; 
		} else if (direc == CardinalDirection.SOUTH){
			return y-1 >= 0 ? map[x][y-1] : null; 
		} else {
			return x-1 >= 0 ? map[x-1][y] : null; 
		}	
	}
	
	public ITile[][] getMap(){
		return map;
	}
	
	public ITile[][] getMapCopy(){
		ITile[][] copiedMap = new ITile[map.length][map[0].length];
	    for (int i = 0; i < map.length; i++) {
	    	for(int j = 0; j < map[0].length; j++){
	    		ITile t = map[i][j];
	    		copiedMap[i][j] = t.copy();
	    	}
	    }
	    return copiedMap;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}
	
	public void setParticipantController(IParticipantController controller){
		participantController = controller;
	}
	
	//Listener
	
	/**
     * Adds a Listener
     * @param listener Listener
     */
	public void addListener(EventListener listener){
		if(listener instanceof TileAddedListener){
			listeners.add(TileAddedListener.class, (TileAddedListener)listener);
		}
	}

	/**
     * Removes a Listener
     * @param listener Listener
     */
	public void removeListener(EventObject listener){
		if(listener instanceof TileAddedListener){
			listeners.remove(TileAddedListener.class, (TileAddedListener)listener);
		}
  	}

	private void notifyEvent(EventObject event){
		if (event instanceof TileAddedEvent){
		    for (TileAddedListener l : listeners.getListeners(TileAddedListener.class)){
		    	l.listen((TileAddedEvent) event);
		    }
		}
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    oos.writeObject(map);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    map = (ITile[][]) ois.readObject();
	}
}
