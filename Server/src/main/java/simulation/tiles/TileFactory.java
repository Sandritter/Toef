package simulation.tiles;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.math.Angle;
import simulation.math.Vector;
import simulation.tiles.enums.TileType;
import simulation.tiles.enums.TileUpperType;
import simulation.tiles.interfaces.ITile;
import simulation.tiles.interfaces.ITileFactory;

/**
 * Encapsulates the creation of a tile
 */
public class TileFactory implements ITileFactory {

	IRepository repository;
	
	public TileFactory(){
		repository = Repository.getInstance();
	}
	
	/**
	 * Creates a tile
	 * @param type Tile type
	 * @param pos Position of tile
	 * @param rot Rotation of tile
	 * @return Tile
	 */
	public ITile create(TileType type, Vector pos, Angle rot){
		ITile tile;
		
		if(type.getUpperType() == TileUpperType.NEGOTIABLE){		
			tile = new Negotiable(type, pos, rot, repository.getTrafficSystem(type));
		} else {
			tile = new Landscape(type, pos, rot);
		}
		
		return tile;
	}
}
