package persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import persistence.interfaces.IPropertyReader;

/**
 * Reads a property file
 */
public class PropertyReader implements IPropertyReader{

	/**
	 * Reads the properties on the given path
	 * @param path Path of properties
	 * @return Properties
	 */
	public Properties readProperties(String path) {
    	Properties properties = new Properties();
    	 
    	try {
    		properties.load(new FileInputStream(path)); 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
  	
    	return properties;
	}
}
