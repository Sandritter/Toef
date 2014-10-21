package network;

import static org.junit.Assert.assertEquals;
import network.enums.NetworkSignal;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author bchri001
 *
 */
public class LobbyFunctionsTest {
	
	static Start lobby;
	
	/**
	 * This method instantiates the server connection and
	 * creates some test server.
	 */
	@BeforeClass
	public static void startServer(){
		lobby = new Start();
		lobby.main(null);
		
		lobby.getFunctions().createServer("test1", "1");
		lobby.getFunctions().createServer("test2", "2");
		lobby.getFunctions().createServer("test3", "3");
		lobby.getFunctions().registerClient("test3", "6", "Mr.T");
	}
	
	/**
	 * Test checks the amount of the created server.
	 */
	@Test
	public void amountServer() {
		int amount = lobby.getFunctions().getServerList().length;
		assertEquals(amount, 3);
	}
	
	/**
	 * Test checks, that it is not possible to have server with same names.
	 */
	@Test
	public void createServerException() {
		String state = lobby.getFunctions().createServer("test1", "4");
		assertEquals(NetworkSignal.SERVERNAME_ALREADY_EXISTS.toString(), state);
	}
	
	/**
	 * Test shows, that you are able to create a server.
	 */
	@Test
	public void createServer() {
		String state = lobby.getFunctions().createServer("test4", "1");
		assertEquals(NetworkSignal.ACTION_SUCCESSFUL.toString(), state);
		// delete created server to guarantee the validity of the test-method amountServer()
		lobby.getFunctions().deleteServer("test4", "1");
	}
	
	/**
	 * Test checks, that just the host of a server is able to delete them.
	 */
	@Test
	public void creatorDeleteServerException() {
		String state = lobby.getFunctions().deleteServer("test1", "2");
		assertEquals(NetworkSignal.CLIENT_NOT_HOST.toString(), state);
	}
	
	/**
	 * Test shows, that a server is able to delete from the host.
	 */
	@Test
	public void creatorDeleteServer() {
		String state = lobby.getFunctions().deleteServer("test1", "1");
		assertEquals(NetworkSignal.ACTION_SUCCESSFUL.toString(), state);
		// recover deleted server to guarantee the validity of the test-method amountServer()
		lobby.getFunctions().createServer("test1", "1");
	}
	
	/**
	 * Test shows, that a client is able to join to a server.
	 */
	@Test
	public void registerClientSuccessful() {
		String state = lobby.getFunctions().registerClient("test2", "5", "Peter");
		assertEquals(NetworkSignal.ACTION_SUCCESSFUL.toString(), state);
	}
	
	/**
	 * Test checks, that on each server the username is unique.
	 */
	@Test
	public void usernameAlreadyExists() {
		String state = lobby.getFunctions().registerClient("test3", "7", "Mr.T");
		assertEquals(NetworkSignal.USERNAME_ALREADY_EXISTS.toString(), state);
	}
	
	/**
	 * Test shows, that it is not possible to join a server which doesn't exist.
	 */
	@Test
	public void serverNameNotExists() {
		String state = lobby.getFunctions().registerClient("test9", "6", "Mr.T");
		assertEquals(NetworkSignal.SERVERNAME_NOT_EXISTS.toString(), state);
	}
	
}
