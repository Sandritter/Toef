package interLayer.converters.interfaces;

import interLayer.entities.NewParticipant;
import interLayer.entities.ViewTile;

/**
 * interface for converter
 */
public interface IConverter {
	
	public String convertViewTileArrayToString(ViewTile[][] o);
	
	public ViewTile convertStringToViewTile(String s);
	
	public String convertViewTileToString(ViewTile o);
	
	public NewParticipant convertStringToNewParticipant(String jsonString);

}
