package simulation.participants.interfaces;

import simulation.participants.enums.DriverType;
import simulation.participants.enums.Mood;

public interface IDriver {

	public String getName();
	public void setName(String name);
	public Mood getMood();
	public void setMood(Mood mood);
	public int getSkill();
	public void setSkill(int skill);
	public DriverType getType();

}
