package simulation.items.enums;

import java.io.Serializable;

/**
 * Tile types
 */
public enum ItemType implements Serializable{
	NONE("NONE"),
	SPEEDLIMIT("SPEEDLIMIT"),
	STOP("STOP"),
	TRAFFICLIGHT("TRAFFICLIGHT");
	
	private String text;
	
	private ItemType(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}
}
