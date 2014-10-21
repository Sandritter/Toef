package interLayer.converters.interfaces;

import interLayer.entities.NewParticipant;
import interLayer.entities.UpdateParticipant;
import interLayer.entities.ViewTile;
import simulation.participants.interfaces.IParticipant;
import simulation.tiles.interfaces.ITile;

/**
 * interface for parser
 */
public interface IParser {
	public ViewTile[][] tileToView(ITile[][] tileset);
	public ViewTile tileToViewTile(ITile tile);
	public ITile viewTileToTile(ViewTile viewTile);
	public IParticipant newParticipantToParticipant(NewParticipant newParticipant);
	public UpdateParticipant iParticipantToViewParticipant(IParticipant p);
}
