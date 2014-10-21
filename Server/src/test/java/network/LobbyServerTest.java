package network;

import static org.junit.Assert.assertEquals;
import network.exception.ClientNotCreatorException;
import network.exception.ServerNameAlreadyExistsException;
import network.exception.ServerNameNotContainedException;
import network.lobby.LobbyServer;
import network.lobby.interfaces.ILobby;

import org.junit.BeforeClass;
import org.junit.Test;

public class LobbyServerTest {

	private static ILobby ls;
	
	/**
	 * Instantiates necessary objects
	 */
	@BeforeClass
	public static void init() {
		Start lobby = new Start();
		lobby.main(null);
		ls = lobby.getLobbyServer();
	}
	
	/**
	 * Test checks, that it is able to create a server without problems.
	 */
	@Test
	public void createServer() {
		ls.createServer("test", "1");
	}
	
	/**
	 * Test checks, if the name of the server already exists.
	 */
	@Test(expected=ServerNameAlreadyExistsException.class)
	public void serverNameExists() {
		ls.createServer("test2", "1");
		ls.createServer("test2", "1");
	}
	
	/**
	 * Test checks, if the server name is not contained in the list.
	 */
	@Test(expected=ServerNameNotContainedException.class)
	public void serverNameNotExists() {
		ls.getServer("bla");
	}
	
	/**
	 * Test shows, that you are able to delete a created server.
	 */
	@Test
	public void deleteServer() {
		ls.createServer("test3", "1");
		ls.deleteServer("test3", "1");
	}
	
	/**
	 * Test checks, that it is not possible to delete a server, when you are not the owner. 
	 */
	@Test(expected=ClientNotCreatorException.class)
	public void clientNotCreator() {
		ls.createServer("test4", "1");
		ls.deleteServer("test4", "2");
	}
	
	/**
	 * Test checks, if the server list is valid.
	 */
	@Test
	public void generateServerList() {
		ls.createServer("test5", "1");
		ls.createServer("test6", "1");
		
		assertEquals(2, ls.generateServerList().length);
	}
}
