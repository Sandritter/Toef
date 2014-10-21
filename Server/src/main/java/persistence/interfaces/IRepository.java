package persistence.interfaces;

import java.util.Properties;

import simulation.control.map.interfaces.IMapController;
import simulation.participants.enums.ParticipantType;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.enums.TileType;
import simulation.traffic.TrafficSystem;

public interface IRepository {


	public void saveMap(IMapController world, String path);
	public IMapController loadMap(String path);
	public Properties readProperties(String path);
	public IParticipant readParticipant(String path, ParticipantType type);
	public Properties getProperties(String name);
	public TrafficSystem readTrafficSystem(String path, TileType type, int size, int unit);
	public TrafficSystem getTrafficSystem(TileType type);
	public IParticipant buildParticipant(ParticipantType type);

}