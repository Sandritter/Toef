package interLayer.converters;

import interLayer.converters.interfaces.IParser;
import interLayer.entities.NewParticipant;
import interLayer.entities.UpdateParticipant;
import interLayer.entities.ViewTile;

import java.util.Properties;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.math.Angle;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;
import simulation.participants.ParticipantFactory;
import simulation.participants.interfaces.IParticipant;
import simulation.participants.interfaces.IParticipantFactory;
import simulation.tiles.TileFactory;
import simulation.tiles.interfaces.ITile;
import simulation.tiles.interfaces.ITileFactory;

/**
 * EntityParser is used to parse serverside entities to objects which are send to clients
 */
public class EntitiyParser implements IParser {
	
	private IRepository repository;
	private Properties prop;
	private float mapSizeX;
	private float mapSizeY;
	private float trafficSystemSize;
	private ITileFactory tileFactory;
	private IParticipantFactory participantFactory;
	
	/**
	 * Constructor
	 */
	public EntitiyParser(){
		repository = Repository.getInstance();
		this.prop = repository.getProperties("simulation");
		this.mapSizeY = Integer.parseInt(prop.getProperty("mapSizeY"));
		this.mapSizeX = Integer.parseInt(prop.getProperty("mapSizeX"));
		this.trafficSystemSize = Integer.parseInt(prop.getProperty("trafficSystemSize"));
		
		tileFactory = new TileFactory();
		participantFactory = new ParticipantFactory();
	}
	
	/**
	 * Parses tiles to viewtiles
	 * @param tileset - tiles to be parsed
	 * @return parsed tiles
	 */
	public ViewTile[][] tileToView(ITile[][] tileset) {
		ViewTile[][] view = new ViewTile[tileset.length][tileset[0].length];

		for (int x = 0; x < tileset.length; x++) {
			for (int y = 0; y < tileset[0].length; y++) {
				ITile tile = tileset[x][y];
				view[x][y] = new ViewTile(tile.getType(), tile.getPosition().getIntX(), tile.getPosition().getIntY(), tile.getRotation().getValue());
			}
		}
		
		return view;
	}
	
	/**
	 * Parsess a tile to a ViewTile
	 * @return ViewTile view
	 */
	public ViewTile tileToViewTile(ITile tile){
		ViewTile view = new ViewTile(tile.getType(), tile.getPosition().getIntX(), tile.getPosition().getIntY(), tile.getRotation().getValue());
		return view;
	}
	
	/**
	 * Parses a viewtile to a tile
	 * @param viewTile ViewTile to be parsed
	 * @return Tile
	 */
	public synchronized ITile viewTileToTile(ViewTile viewTile) {
		return tileFactory.create(viewTile.getType(), new Vector(viewTile.getPosX(), viewTile.getPosY()), new Angle(viewTile.getRot()));
	}

	/**
	 * Parses a ViewParticipant to a Participant
	 * @param UpdateParticipant
	 * @return IParticipant
	 */
	public IParticipant newParticipantToParticipant(NewParticipant newParticipant){
		return participantFactory.create(newParticipant.getType());
	}
	
	/**
	 * Parses a IParticipant to a ViewParticipant
	 * @param p Participant
	 * @return ViewParticipant
	 */
	public UpdateParticipant iParticipantToViewParticipant(IParticipant p){
		float sizeX = mapSizeX * trafficSystemSize;
		float sizeY = mapSizeY * trafficSystemSize;
		IVector vector = p.getTransform().getPosition();

		float relPosX = vector.getX() / sizeX;
		float relPosY = vector.getY() / sizeY;

		return new UpdateParticipant(p.getType(), p.getId(), relPosX, relPosY, p.getTransform().getRotation().getValue(), p.getProperties());
	}

}
