package persistence;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import persistence.interfaces.IMapWriter;
import simulation.control.map.interfaces.IMapController;

/**
 * Saves a world in binary format
 */
public class BinaryMapWriter implements IMapWriter{
	
	/**
	 * Saves a map in binary format
	 * @param path Path where the world should be saved at
	 */
	public void writeMap(IMapController world, String path){
        ObjectOutputStream out;
        
		try {
			out = new ObjectOutputStream(new FileOutputStream(path + ".world"));
			out.writeObject(world);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
