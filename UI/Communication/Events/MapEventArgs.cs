using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Model;

namespace UI.Communication.Events
{
    /// <summary>
    /// Event arguments that are used for an event after the map is recieved
    /// </summary>
    public class MapEventArgs: System.EventArgs
    {
        public readonly Tile[,] map;

        public MapEventArgs(Tile[,] map)
        {
            this.map = map;
        }
    }
}
