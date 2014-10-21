package simulation.control.interfaces;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Callable;

import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.interfaces.IParticipantController;

public interface ISimulation {


	public void start();
	public void start(UncaughtExceptionHandler exceptionHandler);
	public void registerUpdatable(IUpdatable updatable);
	public void registerLateTimedUpdatable(IUpdatable updatable);
	public void enqueue(Runnable r);
	public <T> T enqueue(Callable<T> callable);
	public void enqueueLate(Runnable r);
	public void enqueueLateTimed(Runnable r);
	public IMapController getMapController();
	public IParticipantController getTrafficParticipantController();

}