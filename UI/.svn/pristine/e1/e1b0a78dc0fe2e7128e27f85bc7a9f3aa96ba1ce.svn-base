using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media.Imaging;
using UI.Communication.Interfaces;
using UI.Enums;

namespace UI.Model
{
    /// <summary>
    /// Represents a tiles model
    /// </summary>
    public class Tile : Viewable
    {
        TileType _state;
        private Repository rep = Repository.instance;

        /// <summary>
        /// Instantiates a new Tile
        /// </summary>
        /// <param name="bi">BitmapImage of the Tile</param>
        /// <param name="x">x position in the map</param>
        /// <param name="y">y position in the map</param>
        public Tile(BitmapImage bi, int x, int y) : this(bi, x, y, 0, 0)
        {

        }

        public Tile() : base() { 
        
        }

        /// <summary>
        /// Instantiates a new Tile
        /// </summary>
        /// <param name="x">x position in the map</param>
        /// <param name="y">y position in the map</param>
        /// <param name="rot">Rotation of the Tile</param>
        /// <param name="state">state of the tile (grass, soil, street ...)</param>
        public Tile(int x, int y, float rot, TileType state) : base(null, x, y, rot)
        {
            _rot = rot;
            _posX = x;
            _posY = y;
            _state = state;
            createBitmap();
        }

        /// <summary>
        /// Instantiates a new Tile
        /// </summary>
        /// <param name="bi">BitmapImage of the Tile</param>
        /// <param name="x">x position in the map</param>
        /// <param name="y">y position in the map</param>
        /// <param name="rot">Rotation of the Tile</param>
        /// <param name="state">state of the tile (grass, soil, street ...)</param>
        public Tile(BitmapImage bi, int x, int y, float rot, TileType state) : base(bi, x, y, rot)
        {
            this._bi = bi;
            this._posX = x;
            this._posY = y;
            this._rot = rot;
            this._state = state;
        }

        /*public Tile()
        {
            // TODO: Complete member initialization
        }*/

        private void createBitmap()
        {
            this._bi = rep.getTileImage(_state);
        }

        /// <summary>
        /// State of the Tile (grass, soil, ...)
        /// </summary>
        public TileType State
        {
            get
            {
                return _state;
            }
            set
            {
                _state = value;

                Application.Current.Dispatcher.Invoke(new Action(() =>
                    BmImage = rep.getTileImage(value)
                ));
            }
        }

        /// <summary>
        /// returns a copy of the actual tile
        /// </summary>
        public Tile Copy
        {
            get
            {
                return new Tile(_bi, _posX, _posY, _rot, _state);
            }
        }

        /// <summary>
        /// changes the state of the tile and create the new bitmap
        /// </summary>
        /// <param name="state">state</param>
        public void changeState(int state)
        {
            createBitmap();
        }

        /// <summary>
        /// changes the state of the tile an create the new bitmap
        /// </summary>
        /// <param name="state"></param>
        /// <param name="bi"></param>
        public void ChangeState(TileType state, BitmapImage bi)
        {
            State = state;
            BmImage = bi;

        }
    }
}
