package persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import persistence.interfaces.IMapReader;
import simulation.control.map.interfaces.IMapController;

/**
 * Loads a world in binary format
 */
public class BinaryMapReader implements IMapReader {
	
	/**
	 * Loads a map in binary format
	 * @param path Path of the saved world
	 */
	public IMapController readMap(String path){
        ObjectInputStream in;
        IMapController world = null;
        
		try {
			in = new ObjectInputStream(new FileInputStream(path  + ".world"));
			world = (IMapController) in.readObject();
			in.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        return world;
	}

}
