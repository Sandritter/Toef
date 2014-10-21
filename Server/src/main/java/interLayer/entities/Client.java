package interLayer.entities;

import simulation.math.Vector;
import simulation.math.interfaces.IVector;

/**
 * Representation of a client
 */
public class Client {
	
	private String ip;
	private String name;
	private IVector bottomLeftPosition;
	private IVector upperRightPosition;
	private int tileCountX;
	private int tileCountY;
	
	public Client(String ip, String name){
		this.ip = ip;
		this.name= name;
		this.bottomLeftPosition = new Vector(0,0);
		this.upperRightPosition = new Vector(0,0);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IVector getBottomLeftPosition() {
		return bottomLeftPosition;
	}

	public void setBottomLeftPosition(IVector pos) {
		this.bottomLeftPosition = pos;
	}
	
	public IVector getUpperRightPosition() {
		return upperRightPosition;
	}

	public void setUpperRightPosition(IVector pos) {
		this.upperRightPosition = pos;
	}

	public int getTileCountX() {
		return tileCountX;
	}

	public void setTileCountX(int tileCountX) {
		this.tileCountX = tileCountX;
	}

	public int getTileCountY() {
		return tileCountY;
	}

	public void setTileCountY(int tileCountY) {
		this.tileCountY = tileCountY;
	}

}
