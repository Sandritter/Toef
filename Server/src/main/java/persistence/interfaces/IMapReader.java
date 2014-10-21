package persistence.interfaces;
import simulation.control.map.interfaces.IMapController;


public interface IMapReader {
	
	public IMapController readMap(String path);

}
