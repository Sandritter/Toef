using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Helper
{
    /// <summary>
    /// Converts relative participant postion on the map to tile position
    /// </summary>
    public static class PositionConverter
    {
        private static int MAP_HEIGHT = Properties.Settings.Default.MapHeight;
        private static int MAP_WIDTH = Properties.Settings.Default.MapWidth;
        private static int TILE_SIZE = Properties.Settings.Default.TileSize;

        public static int TilePosX(float relPosX, float divergence)
        {
            int totalWidth = MAP_WIDTH * TILE_SIZE;
            return ((int)(((totalWidth*relPosX)+divergence)/TILE_SIZE));
        }

        public static int TilePosY(float relPosY, float divergence)
        {
            int totalHeight = MAP_HEIGHT * TILE_SIZE;
            return ((int)(((totalHeight * relPosY) + divergence)/ TILE_SIZE));
        }
    }
}
