package simulation.traffic.enums;

import java.io.Serializable;

/**
 * Participant types
 */
public enum PathType implements Serializable{
	STREET("STREET"),
	SIDEWAY("SIDEWAY"),
	SEAROUTE("SEAROUTE");

	private String text;
	
	private PathType(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
}
