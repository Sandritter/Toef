package simulation.participants;
import simulation.participants.enums.DriverType;
import simulation.participants.enums.Mood;
import simulation.participants.interfaces.IDriver;

/**
 * Represents a vehicles driver
 */
public class Driver implements IDriver{
	private String name;
	private Mood mood;
	private int skill; // maybe scale from 1 to 10
	private DriverType type;
	
	public Driver(DriverType type) {
		this.name = "Random name from list maybe";
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Mood getMood() {
		return mood;
	}

	public void setMood(Mood mood) {
		this.mood = mood;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public DriverType getType() {
		return type;
	}
	
}
