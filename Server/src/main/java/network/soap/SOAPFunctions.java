package network.soap;

import interLayer.logic.interfaces.ISimulationServer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import network.enums.NetworkSignal;
import network.exception.ClientNotCreatorException;
import network.exception.ServerNameAlreadyExistsException;
import network.exception.ServerNameNotContainedException;
import network.exception.UsernameAlreadyExistsException;
import network.lobby.interfaces.ILobby;
import network.soap.interfaces.IFunctions;

/**
 * SOAPFunctions offers serverside functions to the clientside
 * A client can create and delete a Server through SOAPFunctions as well as requesting the serverlist of all existing servers
 * In addition SOAPFunctions offers functions to the client to register himself and requesting a list of clients joined on a server
 */
@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class SOAPFunctions implements IFunctions{
	
	private ILobby lobby;
	private static final Logger log = Logger.getLogger(SOAPFunctions.class.getName());
	
	/**
	 * Constructor
	 * @param lobby
	 */
	public SOAPFunctions(ILobby lobby) {
		this.lobby = lobby;
	}
	
	/**
	 * calls the createServer method of the LobbyServer to create a server and to add to the serverMap
	 * @param serverName
	 * @param ip
	 * @return state 
	 */
	public String createServer(String serverName, String ip)
	{
		String state = NetworkSignal.ACTION_SUCCESSFUL.toString();
		try {
			lobby.createServer(serverName, ip);
			log.log(Level.INFO, "Created server: " + serverName);			
		} catch(ServerNameAlreadyExistsException e) {
			state = NetworkSignal.SERVERNAME_ALREADY_EXISTS.toString();
			log.log(Level.WARNING, "Server name " + serverName + " already exists.");
		}
		return state;
	}
	
	/**
	 * calls the deleteServer method of the LobbyServer to delete a server from the serverMap
	 * @param serverName
	 * @param ip
	 * @return state
	 */
	public String deleteServer(String serverName, String ip)
	{
		String state = NetworkSignal.ACTION_SUCCESSFUL.toString();
		try {
			lobby.deleteServer(serverName, ip);
			log.log(Level.INFO, "Deleted server: " + serverName);			
		} catch(ClientNotCreatorException e) {
			state = NetworkSignal.CLIENT_NOT_HOST.toString();
			log.log(Level.WARNING, "Can't delete server " + serverName + ", Client " + ip + " is not Host.");
		}
		return state;
	}
	
	/**
	 * @return list of server
	 */
	public String [] getServerList() {
		return lobby.generateServerList();
	}
	
	/**
	 * registers a client to the clientManager
	 * @param serverName
	 * @param ip
	 * @param username
	 * @return state
	 */
	public String registerClient(String serverName, String ip, String username) {
		String state = NetworkSignal.ACTION_SUCCESSFUL.toString();
		
		try {
			ISimulationServer server = lobby.getServer(serverName);
			server.getClientManager().addClient(ip, username);
			log.log(Level.INFO, username + " joins Server " + serverName);
		}catch(ServerNameNotContainedException e) {
			state = NetworkSignal.SERVERNAME_NOT_EXISTS.toString();
			log.log(Level.WARNING, "Server name " + serverName + " doesn't exist");
		}catch(UsernameAlreadyExistsException e) {
			state = NetworkSignal.USERNAME_ALREADY_EXISTS.toString();
			log.log(Level.WARNING, "Username " + username + " already exists in " + serverName);
		}
		
		return state;
	}
	
	/**
	 * @param serverName
	 * @return list of client usernames
	 */
	public String getClientList(String serverName)
	{
		ISimulationServer server = lobby.getServer(serverName);
		return server.getClientManager().getClientUsernames();
	}
}
