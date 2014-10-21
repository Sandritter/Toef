package simulation.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;

import simulation.participants.interfaces.IParticipant;
import simulation.traffic.Link;
import simulation.traffic.Waypoint;
import simulation.vision.interfaces.IExtendedVisionCalculator;

/**
 * Determines the next properties on the route of one participant
 */
public class NextPropertiesCalculator implements IExtendedVisionCalculator{

	private int limit;
	private boolean enabled;
	private float distance;
	private Map<Map<String, String>, Float> distances;
	private List<Map<String,String>> properties;
	private int size = 0;
	
	public void start(IParticipant participant, Link first) {
		limit = 3;
		size = 0;
		distance = 0;
		distances = new HashMap<Map<String,String>, Float>();
		properties = new ArrayList<Map<String, String>>();
		distance += first.getLength() - participant.getTransform().getDistance();
		
		if(first.getPropertiesCount() > 0){
			properties.add(first.getProperties());
			distances.put(first.getProperties(), 0f);
			size += 1;
		}
		
		Waypoint p = first.getEndPoint();
		if(p.getPropertiesCount() > 0){
			properties.add(p.getProperties());
			distances.put(p.getProperties(), distance);
			size += 1;
		}
	}

	public void calculate(Link next) {
		if(size < limit){			
			if(size < limit && next.getPropertiesCount() > 0){
				properties.add(next.getProperties());
				distances.put(next.getProperties(), distance);
				size += 1;
			}
			
			Waypoint p = next.getEndPoint();
			if(p.getPropertiesCount() > 0){
				properties.add(p.getProperties());
				distances.put(p.getProperties(), distance + next.getLength());
				size += 1;
			}
			
			distance += next.getLength();
		}		
	}

	public PyObject getResult() {
		PyList list = new PyList();
		
		for(Map<String, String> ps : properties){
			PyDictionary dict = new PyDictionary();
			PyDictionary propertiesDict = new PyDictionary();
			for(String key : ps.keySet()){
				String value = ps.get(key);
				propertiesDict.put(key, value);
			}
			dict.put("Properties", propertiesDict);
			dict.put("Distance", distances.get(ps));
			list.add(dict);
		}
		return list;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
