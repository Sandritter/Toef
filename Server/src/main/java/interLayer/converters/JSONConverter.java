package interLayer.converters;

import interLayer.converters.interfaces.IConverter;
import interLayer.entities.ClientPosition;
import interLayer.entities.NewParticipant;
import interLayer.entities.UpdateParticipant;
import interLayer.entities.ViewTile;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * The JSONConverter converts objects to a json-string and reads values of a json-string to object
 */
public class JSONConverter implements IConverter {

	/**
	 * converts a json String to ViewTile
	 * @param json String
	 * @return ViewTile
	 */
	public ViewTile convertStringToViewTile(String s) {
		ViewTile tile = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			tile = mapper.readValue(s, ViewTile.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tile;
	}

	/**
	 * converts a json String to NewParticipant
	 * @param json String
	 * @return NewParticipant
	 */
	public NewParticipant convertStringToNewParticipant(String s) {
		NewParticipant participant = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			participant = mapper.readValue(s, NewParticipant.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return participant;
	}

	/**
	 * converts a ViewTile[][] to json String
	 * @param ViewTile [][] vt
	 * @return json String
	 */
	public String convertViewTileArrayToString(ViewTile[][] vt) {
		String ret = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			ret = mapper.writeValueAsString(vt);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * converts UpdateParticipant[] to json String
	 * @param UpdateParticipant[] list
	 * @return json String
	 */
	public String convertViewCarArraylistToString(UpdateParticipant[] list) {
		String ret = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			ret = mapper.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * converts a ViewTile to json String
	 * @param ViewTile v
	 * @return json String
	 */
	public String convertViewTileToString(ViewTile v) {
		String ret = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			ret = mapper.writeValueAsString(v);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * converts a json String to Clientposition
	 * @param json String
	 * @return ClientPosition
	 */
	public ClientPosition convertStringToClientPosition(String s) {
		ClientPosition clientPosition = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			clientPosition = mapper.readValue(s, ClientPosition.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return clientPosition;
	}

}
