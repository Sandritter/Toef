package network;

import interLayer.converters.JSONConverter;
import interLayer.entities.NewParticipant;
import interLayer.entities.ViewTile;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import simulation.participants.enums.ParticipantType;
import simulation.tiles.Landscape;
import simulation.tiles.enums.TileType;
import simulation.tiles.interfaces.ITile;

public class JsonConverterTest {

	JSONConverter converter = new JSONConverter();
	
	@Test
	public void testConvertFromStringToITile(){
		ObjectMapper mapper = new ObjectMapper();
		ITile tile = new Landscape(null);
		ITile tile2 = null;
		try {
			String s = mapper.writeValueAsString(tile);
			tile2 = mapper.readValue(s, Landscape.class); 
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (tile2 instanceof ITile){
			assert(true);
		}
	}
	
	@Test
	public void testConvertStringToViewTile(){
		ObjectMapper mapper = new ObjectMapper();
		ViewTile tile = new ViewTile(TileType.SOIL, 6, 6, 90.0f);
		ViewTile tile2 = null;
		try {
			String s = mapper.writeValueAsString(tile);
			tile2 = mapper.readValue(s, ViewTile.class); 
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (tile2.getType() == tile.getType() && tile2.getPosX() == tile.getPosX() && tile.getPosY() == tile2.getPosY() && tile.getRot() == tile2.getRot()){
			assert(true);
		}
	}
	
	@Test
	public void testConvertStringToNewParticipant(){
		ObjectMapper mapper = new ObjectMapper();
		NewParticipant newP = new NewParticipant(ParticipantType.CABRIOPINK,1,3, 180.0f);
		NewParticipant newP2 = null;
		try {
			String s = mapper.writeValueAsString(newP);
			newP2 = mapper.readValue(s, NewParticipant.class); 
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (newP2.getType() == newP.getType() && newP2.getTilePosX() == newP.getTilePosX() && newP.getTilePosY() == newP2.getTilePosY() && newP.getRot() == newP2.getRot()){
			assert(true);
		}
	}


}
