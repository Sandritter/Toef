package simulation.traffic.enums;

import java.io.Serializable;
import java.util.Random;

/**
 * Directions
 */
public enum Direction implements Serializable{
	LEFT("LEFT"),
	AHEAD("AHEAD"),
	RIGHT("RIGHT");
	
	private String text;
	
	private Direction(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}
	
    public static Direction getRandomDirection() {
        Random rand = new Random();
        return values()[rand.nextInt(values().length)];
    }
}
