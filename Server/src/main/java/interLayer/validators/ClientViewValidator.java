package interLayer.validators;

import interLayer.converters.EntitiyParser;
import interLayer.entities.Client;
import interLayer.entities.UpdateParticipant;
import interLayer.validators.interfaces.IClientViewValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import persistence.Repository;
import persistence.interfaces.IRepository;
import simulation.math.Vector;
import simulation.math.interfaces.IVector;
import simulation.participants.interfaces.IParticipant;

/**
 * ClientViewValidator checks which Participants are in a clients view
 */
public class ClientViewValidator implements IClientViewValidator{
	
	private EntitiyParser entityParser;
	private IRepository repository;
	private Properties prop;
	private Properties prop2;
	private int trafficSystemSize;
	private int tolerance;
	
	public ClientViewValidator(){
		this.entityParser = new EntitiyParser();
		this.repository = Repository.getInstance();
		this.prop = repository.getProperties("simulation");
		this.prop2 = repository.getProperties("interlayer");
		this.trafficSystemSize = Integer.parseInt(prop.getProperty("trafficSystemSize"));
		this.tolerance = Integer.parseInt(prop2.getProperty("tileCalculationTolerance")) * trafficSystemSize;
	}
	
	/**
	 * returns a list of all participants 
	 * @param Client client
	 * @param Collection<IParticipant> participants
	 * @return List<UpdateParticipant>
	 */
	public List<UpdateParticipant> validate(Client client, Collection<IParticipant> participants){
		
		List<UpdateParticipant> ret = new ArrayList<UpdateParticipant>();
		IVector upperRight = client.getUpperRightPosition();
		IVector bottomLeft = client.getBottomLeftPosition();
		
		IVector upperLeft = new Vector(bottomLeft.getX(), upperRight.getY());
		IVector bottomRight = new Vector(upperRight.getX(), bottomLeft.getY());
		
		
		upperRight = upperRight.mult(trafficSystemSize).add(new Vector(tolerance, tolerance));
		bottomLeft = bottomLeft.mult(trafficSystemSize).sub(new Vector(tolerance, tolerance));
		upperLeft = upperLeft.mult(trafficSystemSize).add(new Vector(-tolerance, tolerance));
		bottomRight = bottomRight.mult(trafficSystemSize).add(new Vector(tolerance, -tolerance));
		
		int urX = upperRight.getIntX();
		int urY = upperRight.getIntY();
		int blX = bottomLeft.getIntX();
		int blY = bottomLeft.getIntY();
		int ulX = upperLeft.getIntX();
		int ulY = upperLeft.getIntY();
		int brX = bottomRight.getIntX();
		int brY = bottomRight.getIntY();
		for (IParticipant p : participants){
			IVector pPos = p.getTransform().getPosition();
			int pX = pPos.getIntX();
			int pY = pPos.getIntY();
			if (pX <= urX && pY <= urY && pX <= brX && pY >= brY && pX >= blX && pY >= blY && pX >= ulX && pY <= ulY){
				ret.add(entityParser.iParticipantToViewParticipant(p));
			}
		}
		
		return ret;
	}
}
