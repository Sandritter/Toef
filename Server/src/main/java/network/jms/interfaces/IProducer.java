package network.jms.interfaces;

public interface IProducer {
	
	void send(String message);
	void close();

}
