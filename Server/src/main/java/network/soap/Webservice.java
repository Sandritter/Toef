package network.soap;

import javax.xml.ws.Endpoint;
/**
 * Serves a webservice for SOAP-Connections
 * 
 * @author
 * 
 */
public class Webservice {

	/**
	 * Constructor
	 * @param SERVICEURL
	 * @param functions
	 */
	public Webservice(final String SERVICEURL, Object functions) {
		Endpoint.publish(SERVICEURL, functions);
		System.out.println("Service started: " + SERVICEURL + "?wsdl");
	}
}
