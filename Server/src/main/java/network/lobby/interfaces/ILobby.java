package network.lobby.interfaces;

import interLayer.logic.interfaces.ISimulationServer;

public interface ILobby {
	String [] generateServerList();
	void createServer(String serverName, String ip);
	void deleteServer(String serverName, String ip);
	ISimulationServer getServer(String serverName);
}
