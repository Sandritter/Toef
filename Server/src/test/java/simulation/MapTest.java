package simulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.Simulation;
import simulation.control.interfaces.ISimulation;
import simulation.control.map.MapController;
import simulation.control.map.enums.CardinalDirection;
import simulation.control.map.interfaces.IMapController;
import simulation.math.Angle;
import simulation.math.Vector;
import simulation.tiles.Landscape;
import simulation.tiles.Negotiable;
import simulation.tiles.enums.TileType;
import simulation.tiles.interfaces.ITile;


public class MapTest {
	ISimulation sim = new Simulation();
	IMapController map = new MapController(sim);
	IRepository rep = Repository.getInstance();
	
	/**
	 * Checks if the map is complete
	 */
	@Test public void mapComplete() {
		for (int x = 0; x < map.getSizeX(); x++) {
			for (int y = 0; y < map.getSizeY(); y++) {
				ITile tile = map.getTile(new Vector(x,y));
				assertEquals(TileType.GRASS, tile.getType());
				assertEquals(x, tile.getPosition().getIntX());
				assertEquals(y, tile.getPosition().getIntY());
			}
		}
	}
	
	/**
	 * Checks if the creation and placement of tiles are working
	 */
	@Test public void add() {
		ITile tile = new Landscape(TileType.FOREST1);
		map.addTile(tile, new Vector(10, 10));
		assertEquals(tile, map.getTile(10, 10));
		
		ITile tile2 = new Landscape(TileType.FOREST2);
		map.addTile(tile2, new Vector(20, 20));
		assertEquals(tile2, map.getTile(20, 20));
		
		ITile tile3 = new Negotiable(TileType.STREETSTRAIGHT, new Vector(0,0), rep.getTrafficSystem(TileType.STREETSTRAIGHT));
		map.addTile(tile3);
		assertEquals(tile3, map.getTile(0, 0));

	}
	
	/**
	 * Checks if the removal of tiles is working
	 */
	@Test public void remove() {
		ITile tile = new Landscape(TileType.FOREST1, new Vector(49,49), new Angle(90));
		map.addTile(tile);
		map.removeTile(49, 49);
		assertEquals(TileType.GRASS, map.getTile(49, 49).getType());
	}
	
	/**
	 * Checks if the getTile Methods are correct
	 */
	@Test public void getTile() {
		ITile tile = new Landscape(TileType.HOUSE, new Vector(30, 30), new Angle(90));
		
		assertEquals(new Vector(29, 30), map.getTile(tile, CardinalDirection.WEST).getPosition());
		assertEquals(new Vector(31, 30), map.getTile(tile, CardinalDirection.EAST).getPosition());
		assertEquals(new Vector(30, 31), map.getTile(tile, CardinalDirection.NORTH).getPosition());
		assertEquals(new Vector(30, 29), map.getTile(tile, CardinalDirection.SOUTH).getPosition());
		
		tile = new Landscape(TileType.HOUSE, new Vector(0, 10), new Angle(90));
		assertEquals(null, map.getTile(tile, CardinalDirection.WEST));
	}
	
	@Test public void mapCopy(){
		
	}
	
}
