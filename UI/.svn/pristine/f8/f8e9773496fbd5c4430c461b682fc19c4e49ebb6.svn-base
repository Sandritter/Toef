using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Models;
using UI.Model;

namespace UI.Communication.Interfaces
{
    /// <summary>
    /// Interface: Responsible for converting messages to different data structures
    /// </summary>
    public interface IConverter
    {
        /// <summary>
        /// Converts a ViewTileArray to TileArrays
        /// </summary>
        /// <param name="vt"></param>
        /// <returns></returns>
        Tile[,] ViewTileArrayToTileArray(ViewTile[][] vt);

        /// <summary>
        /// Converts the CurrentPosition object to a string for messaging
        /// </summary>
        /// <param name="cp"></param>
        /// <returns></returns>
        string ClientPositionToString(ClientPosition cp);

        /// <summary>
        /// Converts the CurrentPosition object to a string for messaging
        /// </summary>
        /// <param name="tile"></param>
        /// <returns></returns>
        Tile Convert(ViewTile tile);

        /// <summary>
        /// Converts a string to an UpdaeParticipant array
        /// </summary>
        /// <param name="tile"></param>
        /// <returns></returns>
       UpdateParticipant[] StringToUpdateParticipantArray(string participant);

        /// <summary>
        /// Converts the CurrentPosition object to a string for messaging
        /// </summary>
        /// <param name="viewTile"></param>
        /// <returns></returns>
        ViewTile StringToViewTile(string viewTile);

        /// <summary>
        /// Converts a messagestring to a ViewTile Array
        /// </summary>
        /// <param name="map"></param>
        /// <returns></returns>
        ViewTile[][] StringToViewTileArray(string map);

        /// <summary>
        /// Converts a single Tile to a ViewTile
        /// </summary>
        /// <param name="t"></param>
        /// <returns></returns>
        ViewTile TileToViewTile(Tile t);

        /// <summary>
        /// Converts a ViewTile to a string for messaging
        /// </summary>
        /// <param name="vt"></param>
        /// <returns></returns>
        string ViewTileToString(ViewTile vt);

        /// <summary>
        /// Converts a NewParticipant to String
        /// </summary>
        /// <param name="vc"></param>
        /// <returns></returns>
        string NewParticipantToString(NewParticipant np);

        /// <summary>
        /// Converts a string to a UpdateParticipant
        /// </summary>
        /// <param name="vc"></param>
        /// <returns></returns>
        UpdateParticipant StringToUpdateParticipant(string s); 

        /// <summary>
        /// Converts a dict of participants to an array
        /// </summary>
        /// <param name="vc"></param>
        /// <returns></returns>
        Dictionary<int, Participant> DictToParticipantArray(UpdateParticipant[] vc);
    }
}
