package interLayer.logic.interfaces;


public interface ISimulationServer{
	void close();
	String getCreator();
	String getServerName();
	IClientManager getClientManager();
}
