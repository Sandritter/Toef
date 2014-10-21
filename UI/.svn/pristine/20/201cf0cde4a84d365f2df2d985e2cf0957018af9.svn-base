using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using UI.Communication;
using UI.Communication.Enums;
using UI.Communication.Events;

namespace UI.Model
{
    /// <summary>
    /// this class manages the states of the tiles in the map and does the communication with the network
    /// </summary>
    class Map
    {
        public int WidthInTiles = Properties.Settings.Default.MapWidth;
        public int HeightInTiles = Properties.Settings.Default.MapHeight;
        public event EventHandler<MapChanged> mapChangedHandler;

        private Tile[,] map;
        private Network network;

        public Map(Network network)
        {
            this.network = network;
            map = new Tile[WidthInTiles, HeightInTiles];
            network.MapReceiver.Handler += GetMapFromServer;
            network.UpdateTileReceiver.Handler += OnUpdateTile;
            network.RequestMap();
        }

        /// <summary>
        /// return the tile of the given position
        /// </summary>
        /// <param name="x">x position in tiles</param>
        /// <param name="y">y position in tiles</param>
        /// <returns>Tile</returns>
        public Tile GetTile(int x, int y)
        {
            if((0 <= x && x < WidthInTiles && (0<=y && y < HeightInTiles)))
            {
                return map[x, y];
            }
            
            return null;
        }

        /// <summary>
        /// Map as observable collection
        /// </summary>
        /// <returns>map</returns>
        public ObservableCollection<Tile> MapAsObservableCollection()
        {
            ObservableCollection<Tile> mapCopy = new ObservableCollection<Tile>();

            for (int i = 0; i < HeightInTiles; i++)
            {
                for (int j = 0; j < WidthInTiles; j++)
                {
                    mapCopy.Add(map[i, j]);
                }
            }
            return mapCopy;
        }

        /// <summary>
        /// updates a tile t and sends it to the network
        /// </summary>
        /// <param name="tile">tile t</param>
        public void UpdateTile(Tile tile)
        {
            network.UpdateTile(tile.Copy);
        }

        /// <summary>
        /// callback for receiving an tile update from the network
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="tileArgs"></param>
        public void OnUpdateTile(object sender, EventArgs tileArgs)
        {
            Tile tile = ((TileEventArgs)tileArgs).tile;
            Tile oldTile = map[tile.X, tile.Y];
            oldTile.Rotation = tile.Rotation;
            oldTile.State = tile.State;
        }

        /// <summary>
        /// renews the whole map
        /// </summary>
        /// <param name="map"></param>
        public void RenewMap(Tile[,] map)
        {
            this.map = map;
            WidthInTiles = map.GetLength(0);
            HeightInTiles = map.GetLength(1);

            ObservableCollection<Tile> mapCopy = new ObservableCollection<Tile>();

            for (int i = 0; i < HeightInTiles; i++)
            {
                for (int j = 0; j < WidthInTiles; j++)
                {
                   mapCopy.Add(map[j, i]);
                }
            }

            MapChanged e = new MapChanged();
            e.Map = mapCopy;
            e.Width = WidthInTiles;
            e.Height = HeightInTiles;
            mapChangedHandler(this, e);
        }

        /// <summary>
        /// callback for receiving the map from the network
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void GetMapFromServer(object sender, EventArgs args){
            System.Diagnostics.Debug.WriteLine("Map vom Server erhalten");
            RenewMap(((MapEventArgs)args).map);
        }
    }

    public class MapChanged : EventArgs
    {
        public ObservableCollection<Tile> Map { get; set; }
        public int Width;
        public int Height;
    }
}
