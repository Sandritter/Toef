package persistence;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import persistence.interfaces.IMapReader;
import persistence.interfaces.IMapWriter;
import persistence.interfaces.IParticipantReader;
import persistence.interfaces.IPropertyReader;
import persistence.interfaces.IRepository;
import persistence.interfaces.ITrafficSystemReader;
import simulation.control.map.interfaces.IMapController;
import simulation.participants.ParticipantProxy;
import simulation.participants.enums.DriverType;
import simulation.participants.enums.ParticipantType;
import simulation.participants.enums.ParticipantUpperType;
import simulation.participants.interfaces.IBehaviour;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.enums.TileType;
import simulation.traffic.TrafficSystem;

/**
 * Responsible for saving and loading data from the file system
 */
public class Repository implements IRepository {
	private IMapWriter mapStorer = new BinaryMapWriter();
	private IMapReader mapLoader = new BinaryMapReader();
	private CachedBehaviourReader factory = new CachedBehaviourReader();
	private IPropertyReader propertyReader = new PropertyReader();
	private ITrafficSystemReader trafficSystemReader = new CachedTrafficSystemReader();
	private IParticipantReader participantReader = new CachedParticipantReader();
	
	private HashMap<String, Properties> properties = new HashMap<String, Properties>();
	
	//Static variables
	public static String propertiesPath = "Properties/";
	public static String trafficSystemsPath;
	public static String participantsPath;
	public static String scriptsPath;
	public static boolean arePassed = false;
	
	private static IRepository instance; 

	private Repository(){
		readProperties();
		readAllTrafficSystems();
		readAllParticipants();
		readAllPythonScripts();		
	}
	
	public static synchronized IRepository getInstance(){
		if(instance == null){
			instance = new Repository();
		}
		return instance;
	}
	
	private void readProperties(){
		//Read all .properties files inside the Properties folder
		File propertiesFile = new File(propertiesPath);
		File[] files = propertiesFile.listFiles();
		
		for(File file : files){
			if(file.getName().endsWith(".properties")){
				String name = file.getName().split("\\.")[0];
				properties.put(name.split("\\.")[0], propertyReader.readProperties(propertiesPath + file.getName()));
			}
		}
		
		if(!arePassed){
			Properties repositoryProperties = getProperties("repository");
			trafficSystemsPath = repositoryProperties.getProperty("trafficSystemsPath");
			participantsPath = repositoryProperties.getProperty("participantsPath");
			scriptsPath = repositoryProperties.getProperty("scriptsPath");			
		}
	}
	
	private void readAllTrafficSystems(){
		//Read all .xml files inside the TrafficSystems folder
		File trafficSystemFile = new File(trafficSystemsPath);
		File[] files = trafficSystemFile.listFiles();
		Properties simulationProperties = properties.get("simulation");
		int size = Integer.parseInt(simulationProperties.getProperty("trafficSystemSize"));
		int unit = Integer.parseInt(simulationProperties.getProperty("unit"));
		
		for(File file : files){
			if(file.getName().endsWith(".xml")){
				String name = file.getName().split("\\.")[0];
				TileType type = TileType.valueOf(name.toUpperCase());
				trafficSystemReader.readTrafficSystem(file.getAbsolutePath(), type, size, unit);
			}
		}
	}
	
	private void readAllParticipants(){
		//Read all .xml files inside the Participants folder
		File participantFile = new File(participantsPath);
		File[] files = participantFile.listFiles();
		
		for(File file : files){			
			if(file.getName().endsWith(".xml")){
				String name = file.getName().split("\\.")[0];
				ParticipantUpperType upperType = ParticipantUpperType.valueOf(name.toUpperCase());
				
				for(ParticipantType type : ParticipantType.values()){
					if(type.getUpperType() == upperType){
						participantReader.readParticipant(file.getAbsolutePath(), type);
					}
				}
			}
		}
	}
	
	private void readAllPythonScripts(){
		//Read all python files inside the Scripts folder
		File scriptsFolder = new File(scriptsPath);
		File[] typeFolder = scriptsFolder.listFiles();
		
		for(File folder : typeFolder){			
			String name = folder.getName();
			ParticipantUpperType upperType = ParticipantUpperType.valueOf(name.toUpperCase());

			for(File file : folder.listFiles()){
				if(file.getName().endsWith(".py")){
					name = file.getName().split("\\.")[0];
					DriverType driverType = DriverType.valueOf(name.toUpperCase());
					factory.read(file.getAbsolutePath(), upperType, driverType);
				}
			}
		}
	}
		
	/**
	 * Stores the world in the format currently selected
	 * @param world World
	 * @param path Path where the world should be saved at
	 */
	public synchronized void saveMap(IMapController world, String path){
        mapStorer.writeMap(world, path);
	}
	
	/**
	 * Loads the world in the format currently selected
	 * @param path Path of the saved world
	 */
	public synchronized IMapController loadMap(String path){
        return mapLoader.readMap(path);
	}
	
	/**
	 * Reads the properties file on the given path
	 * @param path Path
	 * @return Properties
	 */
	public synchronized Properties readProperties(String path){
		return propertyReader.readProperties(path);
	}
	
	/**
	 * Reads the Participant file on the given path
	 * @param path Path
	 * @return Participant
	 */
	public synchronized IParticipant readParticipant(String path, ParticipantType type){
		 return participantReader.readParticipant(path, type);
	}
	
	/**
	 * Returns the properties with the given name
	 * @param name Name
	 * @return Properties
	 */
	public Properties getProperties(String name){
		return (Properties) properties.get(name).clone();
	}
	
	/**
	 * Reads the traffic system on the given path
	 * @param path Path
	 * @param size Size of the system
	 * @param unit How many meters are equal to 1 unit
	 * @return Traffic system
	 */
	public synchronized TrafficSystem readTrafficSystem(String path, TileType type, int size, int unit){
		return trafficSystemReader.readTrafficSystem(path, type, size, unit);
	}
			
	/**
	 * Returns the traffic system with the given type
	 * @return Traffic system
	 */
	public TrafficSystem getTrafficSystem(TileType type){
		Properties simulationProperties = properties.get("simulation");
		int size = Integer.parseInt(simulationProperties.getProperty("trafficSystemSize"));
		int unit = Integer.parseInt(simulationProperties.getProperty("unit"));
		String path = trafficSystemsPath + type.toString() + ".xml";
		
		TrafficSystem system = trafficSystemReader.readTrafficSystem(path, type, size, unit);
		return system;
	}
	
	/**
	 * Returns the participant with the given type
	 * @return Participant
	 */
	public IParticipant buildParticipant(ParticipantType type){
		String path = participantsPath + type.getUpperType().toString() + ".xml";
		IParticipant participant = participantReader.readParticipant(path, type);
		
		//Add Behaviour
		String behaviourPath = scriptsPath + type.getUpperType().toString() + "/" + participant.getDriver().getType() + ".py";
		IBehaviour behaviour = factory.read(behaviourPath, type.getUpperType(), participant.getDriver().getType());
		behaviour.setParticipant(new ParticipantProxy(participant));
		participant.setBehaviour(behaviour);
		
		return participant;
	}
	
}	
	