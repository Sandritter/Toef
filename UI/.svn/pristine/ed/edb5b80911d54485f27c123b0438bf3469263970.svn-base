using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication;
using UI.Model;


namespace UI.ViewModel
{
    /// <summary>
    /// Manages all informationen required to zoom in and out of the world's map
    /// </summary>
    public class ZoomViewModel : ObservableModelBase
    {
        private static int tileSize = Properties.Settings.Default.TileSize;
        private static int mapHeightInTiles = Properties.Settings.Default.MapWidth;
        private static int mapWidthInTiles = Properties.Settings.Default.MapWidth;
        private static int mapWidthInPixel = mapHeightInTiles * tileSize;

        private int _verticalScrollOffset;
        private int _horizontalScrollOffset;

        private static double screenWidth = System.Windows.SystemParameters.PrimaryScreenWidth;
        private static double screenHeight = System.Windows.SystemParameters.PrimaryScreenHeight;

        private double _scale = 1.0;
        private double _minScale = (Math.Round((screenWidth / mapWidthInPixel) * 5) / 5) + 0.2; // round to the next 0.2
        private double _maxScale = 2.0;

        private Network net;

        private ViewportUpdater vpu;

        public ZoomViewModel(Network network) {
            net = network;
            vpu = new ViewportUpdater(tileSize, screenWidth, screenHeight, network);
        }
        
        /// <summary>
        /// Vertical Scrolloffset of the scrollviewer
        /// </summary>
        public int VerticalScrollOffset
        {
            get { return _verticalScrollOffset; }
            set
            {
                _verticalScrollOffset = value;
                vpu.VerticalOffset = value;
                OnPropertyChanged("VerticalScrollOffset");
            }
        }

        /// <summary>
        /// Horizontal Scrolloffset of the scrollviewer
        /// </summary>
        public int HorizontalScrollOffset
        {
            get { return _horizontalScrollOffset; }
            set
            {
                _horizontalScrollOffset = value;
                vpu.HorizontalOffset = value;
                //zoom to scrollCenterX and scrollCenterY
                OnPropertyChanged("HorizontalScrollOffset");
            }
        }

        /// <summary>
        /// scale factor of the whole canvas
        /// </summary>
        public double Scale
        {
            get {
                return _scale;
            }
            set {
                
                    //zoom to center, stores the relative position on the map
                    var relPosX = (HorizontalScrollOffset + screenWidth / 2);
                    relPosX = relPosX / (mapWidthInPixel * _scale);
                    var relPosY = (VerticalScrollOffset + screenHeight / 2);
                    relPosY = relPosY / (mapWidthInPixel * _scale);
                    
                    if (value < MinScale) { _scale = MinScale; }
                    else if (value > MaxScale) { _scale = MaxScale; }
                    else { _scale = value; }
                    
                    //scroll to the relative position of the map
                    ScrollToPosition((relPosX * mapWidthInPixel * _scale), (relPosY * mapWidthInPixel * _scale));
                    vpu.TileSize = tileSize * _scale;
                    OnPropertyChanged("Scale");
            }
        }

        /// <summary>
        /// minimal scale factor
        /// </summary>
        public double MinScale {
            get {
                return _minScale;
            }
            set {
                _minScale = value;
                OnPropertyChanged("MinScale");
            }
        }

        /// <summary>
        /// maximum scale factor
        /// </summary>
        public double MaxScale
        {
            get {
                return _maxScale;
            }
            set {
                _maxScale = value;
                OnPropertyChanged("MaxScale");
            }
        }

        /// <summary>
        /// scrolls to a specific tile
        /// </summary>
        /// <param name="t">Tile</param>
        public void ScrollToTile(Tile t) {
            var newTileSize = tileSize * _scale;

            var posX = t.X * newTileSize;
            var posY = (mapHeightInTiles * newTileSize) - t.Y * newTileSize;

            ScrollToPosition(posX, posY);
        }

        /// <summary>
        /// scrolls to a position in the map
        /// </summary>
        /// <param name="posX">relative x position</param>
        /// <param name="posY">relative y position</param>
        public void ScrollToPosition(double posX, double posY) {
            HorizontalScrollOffset = (int)(posX - (screenWidth / 2));
            VerticalScrollOffset = (int)(posY - (screenHeight / 2));
        }
    }
}
