package interLayer.logic;

import interLayer.entities.Client;
import interLayer.logic.interfaces.IClientManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.exception.UsernameAlreadyExistsException;
import simulation.math.interfaces.IVector;

import com.google.common.base.Joiner;

/**
 * Manages the clients already registered to the server
 */
public class ClientManager extends Observable implements IClientManager{

	/**
	 * String -> ip address
	 * Client
	 */
	private Map<String, Client> clientMap;
	private static final Logger log = Logger.getLogger(ClientManager.class.getName());
	
	public ClientManager(){
		clientMap = new HashMap<String, Client>();
	}
	
	public synchronized void setClientMap(Map<String, Client> clientMap){
		this.clientMap = clientMap;
	}
	
	/**
	 * Registers a client
	 * @param ip IP address of client
	 * @Param name name of client
	 */
	public synchronized void addClient(String ip, String name) {
		canAdd(ip, name);
		Client c = new Client(ip, name);
		clientMap.put(c.getIp(), c);
		setChanged();
		notifyObservers(getClientUsernames());
	}
	
	/**
	 * Registers a client
	 * @param client Client
	 */
	public synchronized void addClient(Client client) {
		canAdd(client.getIp(), client.getName());
		clientMap.put(client.getIp(), client);
		setChanged();
		notifyObservers(getClientUsernames());
	}
	
	/**
	 * Updates the position of a client
	 * @param ip IP address of client
	 * @param posX X position of client
	 * @param posY Y position of client
	 */
	public synchronized void updatePosition(String ip, IVector bottomLeftPosition, IVector upperRightPosition){
		Client c = clientMap.get(ip);		
		c.setBottomLeftPosition(bottomLeftPosition);
		c.setUpperRightPosition(upperRightPosition);
	}
	
	/**
	 * Checks if a client can be added.
	 * This is only possible if the client list does not already contain the name or ip
	 * of the client which should be added
	 * @param ip IP address of client
	 * @param name Name of client
	 */
	private synchronized void canAdd(String ip, String name) {
		Collection<Client> clients = (Collection<Client>) clientMap.values();
		for(Client cl : clients){
			if (cl.getName().compareTo(name) == 0){
				throw new UsernameAlreadyExistsException("Username ist bereits vorhanden!");
			}
		}
	}
	
	/**
	 * Removes a client
	 * @param client Client
	 */
	public synchronized boolean removeClient(Client client){
		return clientMap.remove(client.getIp()) != null;
	}
	
	/**
	 * Removes a client with specified IP
	 * @param ip IP address
	 */
	public synchronized void removeClient(String ip){
		clientMap.remove(ip);
		log.log(Level.INFO, "Client disconnected: " + ip);
		setChanged();
		notifyObservers(getClientUsernames());
	}
	
	/**
	 * Return an array of all client usernames
	 * @return usernames
	 */
	public synchronized String getClientUsernames() {
		List<String> usernames = new ArrayList<String>();
		for(Map.Entry<String, Client> entry: this.clientMap.entrySet())
		{
			usernames.add(entry.getValue().getName());
		}
		return Joiner.on(",").join(usernames);
	}
	
	public synchronized int getSize(){
		return clientMap.size();
	}
	
	public synchronized void clearClients(){
		clientMap.clear();
	}
	
	public synchronized Client getClient(String ip){
		return clientMap.get(ip);
	}
	
	public synchronized boolean hasClientIp(String ip){
		return clientMap.containsKey(ip);
	}
	
	public synchronized Map<String, Client> getClients(){
		return this.clientMap;
	}
	
	public synchronized Set<String> getKeySet(){
		return this.clientMap.keySet();
	}
}
