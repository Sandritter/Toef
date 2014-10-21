using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Enums
{

    /// <summary>
    /// uppertypes for the tiletypes
    /// </summary>
    public enum UpperTileType
    {
        None,
        Landscape,
        Street,
        Water
    }

    /// <summary>
    /// types of the tiles
    /// </summary>
    public enum TileType
    {
        None,
        // landscape
        Soil,
        Grass,
        Forest1,
        Forest2,
        House,
        // street
        StreetStraight,
        StreetCurve,
        StreetCrossing,
        StreetT,
        StreetZebra,
        StreetBridge,
        WaterStraight,
        WaterCurve,
        WaterCrossing,
        WaterT
    }


    /// <summary>
    /// helperclass to get the uppertype from a tiletype
    /// </summary>
    public class TileEnumHelper
    {

        /// <summary>
        /// return an uppertype of a tiletype
        /// </summary>
        /// <param name="type">tiletype</param>
        /// <returns>uppertype</returns>
        public static UpperTileType GetUpperTileType(TileType type)
        {
            if (type == TileType.None)
            {
                return UpperTileType.None;
            }
            else if (type == TileType.Forest1 || type == TileType.Forest2 || type == TileType.Grass ||
                type == TileType.House || type == TileType.Soil)
            {
                return UpperTileType.Landscape;
            }
            else if (type == TileType.WaterCrossing || type == TileType.WaterCurve || type == TileType.WaterStraight || type == TileType.WaterT)
            {
                return UpperTileType.Water;
            }
            else {
                return UpperTileType.Street;
            }
        }
    }
}
