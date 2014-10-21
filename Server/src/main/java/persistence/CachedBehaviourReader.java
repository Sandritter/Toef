package persistence;

import java.util.HashMap;
import java.util.Map;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import persistence.interfaces.IBehaviourReader;
import simulation.participants.enums.DriverType;
import simulation.participants.enums.ParticipantUpperType;
import simulation.participants.interfaces.IBehaviour;

/**
 * Object Factory that is used to create python Behaviour scripts for
 * participants
 */
public class CachedBehaviourReader implements IBehaviourReader {
	
	private PythonInterpreter interpreter;
	private Map<ParticipantUpperType, Map<DriverType, PyObject>> scripts;
	
	public CachedBehaviourReader() {
		// creates a python behaviour object and fetches it from the interpreter
		scripts = new HashMap<ParticipantUpperType, Map<DriverType, PyObject>>();
		interpreter = new PythonInterpreter();

	}
	
	/**
	 * Returns a behaviour which matches the passed driverType
	 * @param path Path
	 * @param type Participant upper type
	 * @param driverType Driver type
	 * @return Behaviour
	 */
	public IBehaviour read(String path, ParticipantUpperType type, DriverType driverType){
		
		if(!scripts.containsKey(type)){
			scripts.put(type, new HashMap<DriverType, PyObject>());
		}
	
		Map<DriverType, PyObject> driverTypeScripts = scripts.get(type);
	
		if(!driverTypeScripts.containsKey(driverType)){
			interpreter.execfile(path);
			interpreter.exec("behaviour = Behaviour");
			driverTypeScripts.put(driverType,  interpreter.get("behaviour"));
		}
		
		return create(type, driverType);
	}

	/**
	 * creates a new IBehaviour object with the given DriverType
	 * @param type Participant upper type
	 * @param driverType Driver type
	 * @return Behaviour
	 */
	private IBehaviour create(ParticipantUpperType type, DriverType driverType){	
		PyObject pythonClass = scripts.get(type).get(driverType);
		
		PyObject buildingObject = pythonClass.__call__();
		return (IBehaviour) buildingObject.__tojava__(IBehaviour.class);
	}
}