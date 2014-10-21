package persistence.interfaces;

import java.util.Properties;

public interface IPropertyReader {
	public Properties readProperties(String path);
}
