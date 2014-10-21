package network.soap.interfaces;

public interface IFunctions {
	public String createServer(String serverName, String ip);
	public String deleteServer(String serverName, String ip);
	public String [] getServerList();
	public String registerClient(String serverName, String ip, String username);
	public String getClientList(String serverName);
}
