package interLayer;

import static org.junit.Assert.assertEquals;
import interLayer.entities.Client;
import interLayer.logic.ClientManager;
import interLayer.logic.interfaces.IClientManager;
import network.exception.IPAlreadyExistsException;
import network.exception.UsernameAlreadyExistsException;

import org.junit.Test;

public class ClientManagerTest {
	
	IClientManager manager = new ClientManager();
	
	/**
	 * Test checks, that you are able to add several clients
	 */
	@Test
	public void addClient() {
		manager.addClient("1", "test1");
		manager.addClient("2", "test2");
		manager.addClient("3", "test3");
		manager.addClient("4", "test4");
		
		assertEquals(4, manager.getSize());
	}
	
	/**
	 * Test checks, that the ip have to be unique
	 */
	@Test(expected=IPAlreadyExistsException.class)
	public void clientIpExists() {
		Client c1 = new Client("1", "test1");
		Client c2 = new Client("1", "test2");
		manager.addClient(c1);
		manager.addClient(c2);
	}
	
	/**
	 * Test checks, that the username have to be unique
	 */
	@Test(expected=UsernameAlreadyExistsException.class)
	public void clientUsernameExists() {
		Client c1 = new Client("1", "test1");
		Client c2 = new Client("2", "test1");
		manager.addClient(c1);
		manager.addClient(c2);
	}
	
	/**
	 * Test shows, that you are able to remove a client by his ip address
	 */
	@Test
	public void removeClient() {
		manager.addClient("1", "test1");
		manager.addClient("2", "test2");
		manager.addClient("3", "test3");
		
		manager.removeClient("3");
		manager.removeClient("2");
		
		assertEquals(1, manager.getSize());
	}
	
	/**
	 * Test shows, that you are able to get a client by his ip address
	 */
	@Test
	public void getClientByIP() {
		Client c = new Client("1", "test");
		manager.addClient(c);
		
		assertEquals(c, manager.getClient("1"));
	}
	
}
