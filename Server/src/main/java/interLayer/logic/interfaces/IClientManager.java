package interLayer.logic.interfaces;

import interLayer.entities.Client;

import java.util.Map;
import java.util.Set;

import simulation.math.interfaces.IVector;

public interface IClientManager {

	public void setClientMap(Map<String, Client> clientMap);

	/**
	 * Registers a client
	 * 
	 * @param ip
	 *            IP address of client
	 * @Param name name of client
	 */
	public void addClient(String ip, String name);

	/**
	 * Registers a client
	 * 
	 * @param client
	 *            Client
	 */
	public void addClient(Client client);

	/**
	 * Updates the position of a client
	 * 
	 * @param ip
	 *            IP address of client
	 * @param posX
	 *            X position of client
	 * @param posY
	 *            Y position of client
	 */
	public void updatePosition(String ip, IVector bottomLeftPosition,
			IVector upperRightPosition);

	/**
	 * Removes a client
	 * 
	 * @param client
	 *            Client
	 */
	public boolean removeClient(Client client);

	/**
	 * Removes a client with specified IP
	 * 
	 * @param ip
	 *            IP address
	 */
	public void removeClient(String ip);

	/**
	 * Return an array of all client usernames
	 * 
	 * @return usernames
	 */
	public String getClientUsernames();

	public int getSize();

	public void clearClients();

	public Client getClient(String ip);

	public boolean hasClientIp(String ip);

	public Map<String, Client> getClients();

	public Set<String> getKeySet();

}