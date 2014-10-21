package simulation.tiles.enums;

import java.io.Serializable;



/**
 * Tile types
 */
public enum TileType implements Serializable{
	NONE("NONE"),
	// Landscape types
	SOIL("SOIL"),
	GRASS("GRASS"),
	FOREST1("FOREST1"),
	FOREST2("FOREST2"),
	HOUSE("HOUSE"),
	// Street types
	STREETSTRAIGHT("STREETSTRAIGHT"),
	STREETCURVE("STREETCURVE"),
	STREETCROSSING("STREETCROSSING"),
	STREETT("STREETT"),
	STREETZEBRA("STREETZEBRA"),
	STREETBRIDGE("STREETBRIDGE"),
	WATERSTRAIGHT("WATERSTRAIGHT"),
	WATERCURVE("WATERCURVE"),
	WATERCROSSING("WATERCROSSING"),
	WATERT("WATERT");
	
	private String text;
	
	private TileType(String text){
		this.text = text;
	}
	
	public TileUpperType getUpperType() {
		if(this == TileType.NONE){
			return TileUpperType.NONE;
		}
		
		if (this == TileType.SOIL || this == TileType.FOREST1 || this == TileType.GRASS ||
				this == TileType.FOREST2 || this == TileType.HOUSE){
			return TileUpperType.LANDSCAPE;
		}
		
		return TileUpperType.NEGOTIABLE;
	}
	
	public String getText(){
		return text;
	}
}
