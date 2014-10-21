package persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.Simulation;
import simulation.control.interfaces.ISimulation;
import simulation.control.map.MapController;
import simulation.control.map.interfaces.IMapController;
import simulation.math.Angle;
import simulation.math.Vector;
import simulation.participants.enums.ParticipantType;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.Negotiable;
import simulation.tiles.enums.TileType;
import simulation.traffic.TrafficSystem;
import simulation.traffic.enums.PathType;

public class RepositoryTest {
	IRepository rep = Repository.getInstance();
	ISimulation sim = new Simulation();
	IMapController world = new MapController(sim);
	
	@Test
	public void readTrafficSystem() {
		TrafficSystem trafficSystem = rep.readTrafficSystem("TrafficSystems/StreetStraight.xml", TileType.STREETSTRAIGHT, 100, 1);
		assertEquals(trafficSystem.getPathSystem(PathType.STREET).getStartPoints().size(), 2);
	}
	
	@Test
	public void getTrafficSystem() {
		Negotiable s = new Negotiable(TileType.STREETSTRAIGHT, new Vector(0,0), new Angle(0), rep.getTrafficSystem(TileType.STREETSTRAIGHT));
		assertEquals(s.getTrafficSystem().getPathSystem(PathType.STREET).getStartPoints().size(), 2);
	}
	
	@Test
	public void getParticipant() {
		IParticipant p = rep.buildParticipant(ParticipantType.CABRIOBLUE);
		assertTrue(equalTo(p.getMaxVelocity() * 3.6f, 144));
		assertTrue(equalTo(p.getMaxAcceleration(), 40));
	}
	
	private boolean equalTo(float v1, float v2){
		float epsilon = 0.00000001f;
		return (Math.abs(v1 - v2) < epsilon);
	}
	
	
}
