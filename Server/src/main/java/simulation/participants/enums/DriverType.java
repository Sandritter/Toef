package simulation.participants.enums;

import java.util.Random;

/**
 * Contains different kind of driver types used by behaviour scripts
 */
public enum DriverType {
	CALM("CALM"),
	AGGRESSIVE("AGGRESSIVE");
	
	private String text;
	
	private DriverType(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}
	
    public static DriverType getRandomType() {
        Random rand = new Random();
        return values()[rand.nextInt(values().length)];
    }
}
