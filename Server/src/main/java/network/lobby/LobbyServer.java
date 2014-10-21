package network.lobby;

import interLayer.logic.SimulationServer;
import interLayer.logic.interfaces.ISimulationServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import network.exception.ClientNotCreatorException;
import network.exception.ServerNameAlreadyExistsException;
import network.exception.ServerNameNotContainedException;
import network.jms.producer.HostDownProducer;
import network.lobby.interfaces.ILobby;

/**
 * the LobbySever holds all SimulationServer in a hash map
 * and holds methods to create and delete SimulationServer
 */
public class LobbyServer implements ILobby{
	
	/**
	 * Map of all simulation server
	 * Key -> Name of SimulatioServer
	 * Value -> Instance of SimulationServer
	 */
	private HashMap<String, ISimulationServer> serverMap;
	
	/**
	 * MessageProducer to let Clients know that the SimulationServer is done
	 */
	private HostDownProducer hostDownProducer;

	/**
	 * standard-constructor
	 */
	public LobbyServer() {
		hostDownProducer = new HostDownProducer();
		serverMap = new HashMap<String, ISimulationServer>();
	}
	
	public synchronized ISimulationServer getServer(String serverName) {
		for(String key: serverMap.keySet()) {
			if(key.compareTo(serverName)==0){
				return serverMap.get(key);
			}
		}
		throw new ServerNameNotContainedException("Server Name " +  serverName + " ist nicht enthalten.");
	}
	
	/**
	 * Generate an array of the server names
	 * @return
	 */
	public synchronized String [] generateServerList() {
		List<String> list = new ArrayList<String>();
		for(String key: serverMap.keySet()) {
			list.add(key);
		}
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * Method creates a new simulation server
	 * @param serverName - name of simulation server
	 * @param username - name of creator
	 * @param ip - creator IP address
	 */
	public synchronized void createServer(String serverName, String ip) {
		canCreate(serverName);
		serverMap.put(serverName, new SimulationServer(ip, serverName));
	}
	
	/**
	 * Method deletes a simulation server
	 * and sends a message to all Clients joined to the given SimulationServer informing them about the Shutdown of the Server
	 * @param serverName - name of simulation server
	 * @param ip - client IP address
	 */
	public synchronized void deleteServer(String serverName, String ip) {
		canDelete(serverName, ip);
		// benachrichtige alle Clients, dies auf diesem SimulationsServer angemeldet sind
		hostDownProducer.send(serverName);
		serverMap.get(serverName).close();
		serverMap.remove(serverName);
	}
	
	/**
	 * Method checks, if server name is already taken
	 * @param serverName - name of simulation server
	 */
	private synchronized void canCreate(String serverName) {
		boolean nameExists = serverMap.containsKey(serverName);
		if(nameExists){
			throw new ServerNameAlreadyExistsException("Server Name bereits vergeben");
		}
	}
	
	/**
	 * Methods checks, if client is able to delete server
	 * @param serverName - name of simulation server
	 * @param ip - client IP address
	 */
	private synchronized void canDelete(String serverName, String ip) {
		ISimulationServer s = serverMap.get(serverName);
		if(s.getCreator().compareTo(ip)!=0) {
			throw new ClientNotCreatorException("Client nicht Eigentuemer des Servers");
		}
	}

}
