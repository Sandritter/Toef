package persistence.interfaces;
import simulation.control.map.interfaces.IMapController;


public interface IMapWriter {
	
	public void writeMap(IMapController world, String path);

}
