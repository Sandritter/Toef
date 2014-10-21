package network;


import java.io.File;
import network.jms.MessageBroker;
import network.lobby.LobbyServer;
import network.lobby.interfaces.ILobby;
import network.soap.SOAPFunctions;
import network.soap.Webservice;
import network.soap.interfaces.IFunctions;
import persistence.Repository;

/**
 * Start, is the initial starter of the serverside application
 * the Start class is establishing a serverside embedded broker,
 * is creating the LobbyServer and is creating a instance of SOAPFunctions
 */
public class Start {
	
	/**
	 * service url for clients to create or delete server
	 */
	
	private static String LOBBYSERVICE = Repository.getInstance().getProperties("network").getProperty("lobbyService-Url");
	// declaration need for JUnit Tests
	private static IFunctions functions;
	private static ILobby lobbyServer;
	
	public static void main(String[] args) {
		
		//Arguments must be in order: Propertiesfile, TrafficSystemsfile, Participantsfile, Scriptsfile
		if(args != null && args.length == 4){ //If arguments are passed, there need to be 4
			System.out.println("Arguments are processed");
			initPaths(args);
		}
	
		@SuppressWarnings("unused")
		MessageBroker messageBroker = new MessageBroker();
		// service for clients to create or delete server
		lobbyServer = new LobbyServer();
		functions = new SOAPFunctions(lobbyServer);
        @SuppressWarnings("unused")
		Webservice lobbyService = new Webservice(LOBBYSERVICE, functions);
	}
	
	/**
	 * Method needs for JUnit tests
	 * @return
	 */
	public static IFunctions getFunctions() {
		return functions;
	}
	
	/**
	 * Method needs for JUnit tests
	 * @return
	 */
	public static ILobby getLobbyServer() {
		return lobbyServer;
	}
	
	/**
	 * Method for initialising Repository paths if they are passed 
	 */
	public static void initPaths(String [] args){
		
		File file;
		
		//Pass Propertiepath to Repository (general .properties files)
		file = new File(args[0]);
		Repository.propertiesPath = file.getAbsolutePath() + "/";
		
		//Pass Trafficsystempath to Repository (xml)
		file = new File(args[1]);
		Repository.trafficSystemsPath= file.getAbsolutePath() + "/";
		
		//Pass Participantpath to Repository (xml)
		file = new File(args[2]);
		Repository.participantsPath = file.getAbsolutePath() + "/";
		
		//Pass Scriptfilepaths to Repository (.py files)
		file = new File(args[3]);
		Repository.scriptsPath = file.getAbsolutePath() + "/";
		
		Repository.arePassed = true;
	}
}
