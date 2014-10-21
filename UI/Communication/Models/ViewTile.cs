using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;
using UI.Enums;

namespace UI.Communication.Models
{
    /// <summary>
    /// ViewTile Class for the Frontend
    /// </summary>
    public class ViewTile : ISend
    {
        public TileType type; //Type of tile
        public int posX;// x-position in map
        public int posY; //y-position in map
        public float rot; //rotated angle

        public ViewTile(TileType type, int posX, int posY, float rot)
        {
            this.type = type;
            this.posX = posX;
            this.posY = posY;
            this.rot = rot;
        }
    }
}
