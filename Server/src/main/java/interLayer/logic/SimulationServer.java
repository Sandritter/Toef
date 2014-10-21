package interLayer.logic;

import interLayer.converters.EntitiyParser;
import interLayer.converters.JSONConverter;
import interLayer.converters.interfaces.IConverter;
import interLayer.converters.interfaces.IParser;
import interLayer.entities.NewParticipant;
import interLayer.entities.ViewTile;
import interLayer.logic.interfaces.IClientManager;
import interLayer.logic.interfaces.ISimulationServer;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import network.enums.PropertyNames;
import network.jms.consumer.CarFuelConsumer;
import network.jms.consumer.ChatMessageConsumer;
import network.jms.consumer.ClientDisconnectConsumer;
import network.jms.consumer.ClientPositionConsumer;
import network.jms.consumer.DeleteParticipantConsumer;
import network.jms.consumer.KeepAliveConsumer;
import network.jms.consumer.ParticipantConsumer;
import network.jms.consumer.ViewTileConsumer;
import network.jms.interfaces.IConsumer;
import network.jms.interfaces.IProducer;
import network.jms.producer.ActiveClientsProducer;
import network.jms.producer.ChatMessageProducer;
import network.jms.producer.KeepAliveProducer;
import network.jms.producer.ParticipantProducer;
import network.jms.producer.ViewTileProducer;
import network.jms.requestor.MapRequestor;
import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.control.Simulation;
import simulation.control.interfaces.ISimulation;
import simulation.control.map.interfaces.IMapController;
import simulation.control.participants.interfaces.IParticipantController;
import simulation.math.Angle;
import simulation.math.Vector;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.ITile;

/**
 * holds the simulation as well as consumers and producers to send and receive
 * messages the simulation is listing all clients using a ClientManager the
 * SimulationServer has a unique serverName
 */
public class SimulationServer implements Observer, ISimulationServer {

	private String creator;
	private String serverName;
	private ISimulation simulation;
	private IRepository repository;
	private IParser entityParser;
	private IConverter jsonConverter;
	private IClientManager clientManager;
	private static final Logger log = Logger.getLogger(SimulationServer.class.getName());
	private Timer timer;
	private CheckClientTask checkClient;
	private final int SECONDS = 5; // In Properties auslagern
	private boolean firstRun;
	private MapRequestor mapRequestor;

	private IProducer viewTileProducer;
	private IProducer clientViewUpdateProducer;
	private IProducer chatMessageProducer;
	private IProducer activeClientsProducer;
	private IProducer keepAliveProducer;

	private IConsumer viewTileConsumer;
	private IConsumer clientPositionConsumer;
	private IConsumer clientDisconnectComsumer;
	private IConsumer participantConsumer;
	private IConsumer deleteParticipantConsumer;
	private IConsumer chatMessageConsumer;
	private IConsumer keepAliveConsumer;
	private IConsumer carFuelConsumer;

	/**
	 * constructor
	 * 
	 * @param creator
	 *            - IP address
	 * @param serverName
	 */
	public SimulationServer(String creator, String serverName) {
		this.creator = creator;
		this.serverName = serverName;
		init();
		simulation.start();
	}
	
	public void close() {
		mapRequestor.close();
		viewTileProducer.close();
		clientViewUpdateProducer.close();
		chatMessageProducer.close();
		
		viewTileConsumer.close();
		clientPositionConsumer.close();
		clientDisconnectComsumer.close();
		participantConsumer.close();
		deleteParticipantConsumer.close();
		chatMessageConsumer.close();
		carFuelConsumer.close();
	}

	/**
	 * 
	 */
	private void init() {
		repository = Repository.getInstance();
		simulation = new Simulation();
		clientManager = new ClientManager();
		entityParser = new EntitiyParser();
		jsonConverter = new JSONConverter();

		// Combines Consumer and Producer
		mapRequestor = new MapRequestor(serverName, simulation);

		initConsumer();
		initProducer();
		initEventhandling();

		// Checks if clients are alive in certain intervall
		timer = new Timer();
		checkClient = new CheckClientTask();
		firstRun = true;
		timer.scheduleAtFixedRate(checkClient, SECONDS * 1000, SECONDS * 1000);

	}

	/**
	 * Initialises all Consumers for Messaging
	 */
	private void initConsumer() {
		viewTileConsumer = new ViewTileConsumer(serverName);
		chatMessageConsumer = new ChatMessageConsumer(serverName);
		clientPositionConsumer = new ClientPositionConsumer(serverName,clientManager);
		clientDisconnectComsumer = new ClientDisconnectConsumer(serverName,clientManager);
		participantConsumer = new ParticipantConsumer(serverName);
		deleteParticipantConsumer = new DeleteParticipantConsumer(serverName);
		keepAliveConsumer = new KeepAliveConsumer(serverName, clientManager);
		carFuelConsumer = new CarFuelConsumer(serverName);
	}

	/**
	 * Initialises all Producers for Messaging
	 */
	private void initProducer() {
		chatMessageProducer = new ChatMessageProducer(serverName);
		viewTileProducer = new ViewTileProducer(serverName);
		clientViewUpdateProducer = new ParticipantProducer(clientManager,
				serverName);
		activeClientsProducer = new ActiveClientsProducer(serverName);
		keepAliveProducer = new KeepAliveProducer(serverName);
	}

	/**
	 * Initialises Eventhandling / Observing between Messaging entities
	 */
	private void initEventhandling() {
		if (chatMessageConsumer instanceof Observable) {
			((Observable) chatMessageConsumer).addObserver((Observer) chatMessageProducer);
		}
		if (clientManager instanceof Observable) {
			((Observable) clientManager).addObserver((Observer) activeClientsProducer);
		}
		if (simulation.getMapController() instanceof IMapController) {
			((IMapController) simulation.getMapController()).addListener((EventListener) viewTileProducer);
		}
		if (simulation.getTrafficParticipantController() instanceof IParticipantController) {
			((IParticipantController) simulation.getTrafficParticipantController()).addListener((EventListener) clientViewUpdateProducer);
		}
		if (participantConsumer instanceof Observable) {
			((Observable) participantConsumer).addObserver((Observer) this);
		}
		if (deleteParticipantConsumer instanceof Observable) {
			((Observable) deleteParticipantConsumer).addObserver((Observer) this);
		}
		if (viewTileConsumer instanceof Observable) {
			((Observable) viewTileConsumer).addObserver((Observer) this);
		}
		if (carFuelConsumer instanceof Observable) {
			((Observable) carFuelConsumer).addObserver((Observer) this);
		}
	}

	/**
	 * 
	 * @return creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * 
	 * @return serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * 
	 * @return
	 */
	public IClientManager getClientManager() {
		return this.clientManager;
	}

	/**
	 * this method is called from seperate consumers
	 */
	public void update(Observable observ, Object o) {
		if (observ instanceof ViewTileConsumer) {
			if (o instanceof TextMessage) {
				final IMapController mapController = simulation.getMapController();
				TextMessage message = (TextMessage) o;
				String jsonString = null;
				try {
					// get the json-stringmessage
					jsonString = message.getText();
				} catch (JMSException e) {
					System.err
							.println("MESSAGE-FEHLER: ViewTile konnte nicht aus Message ausgelesen werden.");
					e.printStackTrace();
				}

				ViewTile tile = jsonConverter.convertStringToViewTile(jsonString);
				final ITile t = entityParser.viewTileToTile(tile);

				simulation.enqueue(new Runnable() {
					public void run() {
						mapController.addTile(t);
					}
				});
			}
		}
		if (observ instanceof ParticipantConsumer) {
			if (o instanceof TextMessage) {
				final IParticipantController trafficParticipantController = simulation
						.getTrafficParticipantController();
				TextMessage message = (TextMessage) o;
				String jsonString = null;
				try {
					jsonString = message.getText();
				} catch (JMSException e) {
					System.err
							.println("MESSAGE-FEHLER: Participant konnte nicht aus Message ausgelesen werden.");
					e.printStackTrace();
				}

				final NewParticipant newParticipant = jsonConverter
						.convertStringToNewParticipant(jsonString);
				final IParticipant participant = entityParser
						.newParticipantToParticipant(newParticipant);
				
				simulation.enqueue(new Runnable() {
					public void run() {
						trafficParticipantController.addParticipant(participant,
								new Vector(newParticipant.getTilePosX(), newParticipant.getTilePosY()), 
								new Angle(newParticipant.getRot()));
					}
				});
			}
		}

		if (observ instanceof DeleteParticipantConsumer) {
			if (o instanceof TextMessage) {
				final IParticipantController trafficParticipantController = simulation.getTrafficParticipantController();
				TextMessage message = (TextMessage) o;

				try {
					// fetch the id of the participant from the message
					final int participantId = message.getIntProperty(PropertyNames.PARTICIPANTID.toString());
					simulation.enqueue(new Runnable() {
						public void run() {
							// remove participant from the participant map of
							// the trafficParticipantController
							trafficParticipantController.removeParticipant(participantId);
						}
					});
				} catch (JMSException e) {
					e.printStackTrace();
				}

			}
		}
		

		if(observ instanceof CarFuelConsumer){
			TextMessage message = (TextMessage) o;
			final IParticipantController trafficParticipantController = simulation.getTrafficParticipantController();
			try {
				// fetch the id of the participant from the message
				final int participantId = message.getIntProperty(PropertyNames.PARTICIPANTID.toString());
				simulation.enqueue(new Runnable() {
					public void run() {
						IParticipant p = trafficParticipantController.getParticipantByID(participantId);
						p.refill();
					}
				});
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}

	public class CheckClientTask extends TimerTask {

		@Override
		public void run() {
			if (!firstRun) {
				((KeepAliveConsumer) keepAliveConsumer).checkClients();
			}
			firstRun = false;
			keepAliveProducer.send(null);
		}

	}

}
