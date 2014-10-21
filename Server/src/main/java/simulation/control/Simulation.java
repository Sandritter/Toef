package simulation.control;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.interfaces.ISimulation;
import simulation.control.interfaces.IUpdatable;
import simulation.control.map.MapController;
import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.ParticipantController;
import simulation.control.participants.ParticipantUpdater;
import simulation.control.participants.interfaces.IParticipantController;

/**
 * Manages the update loop
 */
public class Simulation implements ISimulation {
	private final long TIME = TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS); //1 Second in Nanoseconds
	private final long TIMEMIL = TimeUnit.NANOSECONDS.convert(1, TimeUnit.MILLISECONDS); //1 Millisecond in Nanoseconds
	private int target_fps = 30;
	private long opt_time = TIME / target_fps;  
	private boolean running = false;

	private int outputRatePS = 20; //Per Seconds
	private float outputRate = 1f / outputRatePS;
	private float nextUpdate = 0;

	//List of class with an update method which gets called every frame
	private List<IUpdatable> updatables = new ArrayList<IUpdatable>();
	private List<IUpdatable> lateTimedUpdatable = new ArrayList<IUpdatable>();

	//Input
	Queue<Runnable> runnables = new ConcurrentLinkedQueue<Runnable>();
	Queue<FutureTask<?>> callables = new ConcurrentLinkedQueue<FutureTask<?>>();

	//Output
	Queue<Runnable> runnablesLate = new ConcurrentLinkedQueue<Runnable>();	
	Queue<Runnable> runnablesLateTimed = new ConcurrentLinkedQueue<Runnable>();

	//Classes used by the network interface
	private IMapController mapController;
	private IParticipantController participantController;
	private ParticipantUpdater updater;
	IRepository repository;

	public Simulation(){	
		//Create main classes necessary for the simulation
		repository = Repository.getInstance();
		target_fps = Integer.parseInt(repository.getProperties("simulation").getProperty("updateRate"));
		outputRatePS = Integer.parseInt(repository.getProperties("simulation").getProperty("outputRate"));
		opt_time = TIME / target_fps; 
		outputRate = 1f / outputRatePS;

		mapController = new MapController(this);
		participantController = new ParticipantController(mapController);	
		mapController.setParticipantController(participantController);

		//Add Updatable classes
		updater = new ParticipantUpdater(participantController, mapController);
		registerUpdatable(updater);
		registerLateTimedUpdatable((ParticipantController)participantController);
	}

	/**
	 * Starts the updateLoop and therefore the whole simulation
	 */
	public void start(){
		running = true;
		new Thread()
		{
			public void run() {
				updateLoop();
			}
		}.start();
	}

	/**
	 * Starts the updateLoop and therefore the whole simulation
	 * @param exceptionHandler ExceptionHandler which handles the uncaught exceptions
	 */
	public void start(UncaughtExceptionHandler exceptionHandler){
		running = true;
		Thread thread = new Thread()
		{
			public void run() {
				updateLoop();
			}
		};
		thread.setUncaughtExceptionHandler(exceptionHandler);
		thread.start();
	}

	/**
	 * Registers a class which should get called every frame
	 * @param updatable Class which implements the IUpdatable Interface
	 */
	public void registerUpdatable(IUpdatable updatable){
		updatables.add(updatable);
	}
	
	/**
	 * Registers a class which should get called every frame
	 * @param updatable Class which implements the IUpdatable Interface
	 */
	public void registerLateTimedUpdatable(IUpdatable updatable){
		lateTimedUpdatable.add(updatable);
	}

	/**
	 * Adds a task to the queue which is processed after the simulation(Thread save)
	 * @param runnable Class which implements the Runnable Interface
	 */
	public void enqueue(Runnable r){
		//thread save
		runnables.add(r);
	}

	/**
	 * Adds a value returning task to the queue which is processed before the simulation(Thread save)
	 * The calling thread is blocked until the task finished execution
	 * Slower then enqueue a Runnable
	 * @param callable Class which implements the callable Interface
	 * @return Return value of the callable, null if the callable failed to execute
	 */
	public <T> T enqueue(Callable<T> callable){
		//thread save 
		FutureTask<T> task = new FutureTask<T>(callable);  
		runnables.add(task);

		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Adds a task to the queue which is processed after the simulation(Thread save)
	 * @param runnable Class which implements the Runnable Interface
	 */
	public void enqueueLate(Runnable r){
		//thread save
		runnablesLate.add(r);
	}

	/**
	 * Adds a task to the queue which is processed after the simulation(Thread save) every outputRate step
	 * @param runnable Class which implements the Runnable Interface
	 */
	public void enqueueLateTimed(Runnable r){
		//thread save
		runnablesLateTimed.add(r);
	}

	/**
	 * Runs all tasks(Runnables) which get executed before the simulation
	 */
	private void runRunnableTasks(){
		Runnable t;
		while ((t = runnables.poll()) != null){
			t.run();
		}
	}

	/**
	 * Runs all tasks(Callables) which get executed before the simulation
	 */
	private void runCallableTasks(){
		FutureTask<?> t;
		while ((t = callables.poll()) != null){
			t.run();
		}
	}

	/**
	 * Runs all tasks which get executed after the simulation
	 */
	private void runLateTasks(){
		Runnable r;
		while ((r = runnablesLate.poll()) != null){
			r.run();
		}
	}

	/**
	 * Runs all timed tasks which get executed after the simulation
	 */
	private void runLateTimedTasks(float deltaTime){
		nextUpdate += deltaTime;
		
		if(nextUpdate >= outputRate){			
			Runnable r;
			while ((r = runnablesLateTimed.poll()) != null){
				r.run();
			}
			
			for(IUpdatable u : lateTimedUpdatable){
				u.update(deltaTime);
			}
			
			nextUpdate = 0;
		} else {
			runnablesLateTimed.clear();
		}
	}

	/**
	 * Update loop which is called every frame
	 */
	private void updateLoop(){
		long lastLoopTime = System.nanoTime();
		
		while (running){
			long now = System.nanoTime(); // Current Time in Nanoseconds
			long updateLength = now - lastLoopTime; // Time between Updates
			lastLoopTime = System.nanoTime();
			float deltaTime = updateLength / (float)TIME; // Delta time in seconds

			updateGame(deltaTime);

			//Sleep some time to reach the targeted frame rate
			long t = (lastLoopTime - System.nanoTime() + opt_time) / TIMEMIL;
			
			if (t >= 0){
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Framerate niedrig");
			}
		}
	}
	
	private void updateGame(float deltaTime){
		//INPUT
		runRunnableTasks();
		runCallableTasks();

		//PROCESSING
		// Call all registered update methods(do simulation)
		for(IUpdatable u : updatables){
			u.update(deltaTime);
		}

		//OUTPUT
		// Send Data to Clients here
		// UNTIMED

		runLateTasks();

		// TIMED
		runLateTimedTasks(deltaTime);
	}


	public IMapController getMapController(){
		return mapController;
	}

	public IParticipantController getTrafficParticipantController(){
		return participantController;
	}
}
