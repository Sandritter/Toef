using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Threading;
using UI.Communication;

namespace UI.Model
{


    /// <summary>
    /// Calculates the actual viewport and sends it to the network
    /// the viewport is based on the bottem-left corner (x1, y1) and the top-right-corner (x2,y2)
    /// </summary>
    class ViewportUpdater
    {
        
        private Network net;
        private int x1, x2, y1, y2;
        private double tileSize;
        private double screenWidth;
        private double screenHeight;
        private bool hasChanged = false;

        /// <summary>
        /// instantiates a new ViewPortUpdater
        /// </summary>
        /// <param name="tileSize">size of a tile in pixel</param>
        /// <param name="screenWidth">screenWidth in pixel</param>
        /// <param name="screenHeight">screenHeight in pixel</param>
        public ViewportUpdater(int tileSize, double screenWidth, double screenHeight, Network network) {
            
            this.tileSize = tileSize;
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;


            this.x1 = 0;
            this.y1 = (int)(this.screenHeight / tileSize);
            
            this.x2 = (int)(this.screenWidth / tileSize);
            this.y2 = Properties.Settings.Default.MapHeight;

            net = network;
            net.sendClientPosition(x1, y1, x2, y2);

            // Instantiates a new timer 
            DispatcherTimer timer = new DispatcherTimer();
            // calls the updateViewPort methode every 200ms
            timer.Tick += new EventHandler(updateViewPort);
            timer.Interval = new TimeSpan(0, 0, 0, 0, 200);
            timer.Start();
        }

        /// <summary>
        /// sends the actual viewport to the network
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void updateViewPort(object sender, EventArgs e)
        {
            // only sends if the viewport has changed
            if (hasChanged) {
                System.Diagnostics.Debug.WriteLine("Update: " + x1 + " " + y1 + " " + x2 + " " + y2 + " ");
                try {
                    net.sendClientPosition(x1, y1, x2, y2);
                } catch (Exception ex){
                    System.Diagnostics.Debug.WriteLine(ex.Message);
                }
                hasChanged = false;
            }
        }

        /// <summary>
        /// recalculates the new viewport
        /// </summary>
        private void recalculate() {
            this.x1 = (int)(_horizontalOffset / tileSize);
            this.x2 = (int)((this.screenWidth + _horizontalOffset) / tileSize);

            this.y1 = Properties.Settings.Default.MapHeight - (int)((this.screenHeight + _verticalOffset) / tileSize);
            this.y2 = Properties.Settings.Default.MapHeight - (int)(_verticalOffset / tileSize);

            hasChanged = false;
        }

        /// <summary>
        /// max amout of vertical tiles
        /// </summary>
        /// <returns>amount of vertical tiles</returns>
        public int maxVerticalTiles()
        {
            return (int) (this.screenHeight / this.tileSize);
        }

        /// <summary>
        /// horizontal Scrolloffset of the viewports scrollviewer
        /// </summary>
        private int _horizontalOffset;
        public int HorizontalOffset {
            // sets the new x coords of the viewport
            set { 
                _horizontalOffset = value;
                this.x1 = (int)(_horizontalOffset / tileSize);
                this.x2 = (int)((this.screenWidth + _horizontalOffset) / tileSize);
                hasChanged = true;
            }
        }
        
        /// <summary>
        /// vertival Scrolloffset of the viewports scrollviewer
        /// </summary>
        private int _verticalOffset;
        public int VerticalOffset{
            // sets the new y coords of the viewport
            set { 
                _verticalOffset = value;
                this.y1 = Properties.Settings.Default.MapHeight - (int)((this.screenHeight + _verticalOffset) / tileSize);
                this.y2 = Properties.Settings.Default.MapHeight - (int)(_verticalOffset / tileSize);
                hasChanged = true;
            }
        }

        /// <summary>
        /// actual size of a tile
        /// </summary>
        public double TileSize {
            set {
                tileSize = value;
                hasChanged = true;
                // when tilesize has changed, every coordinate has to be recalculated
                recalculate();
            }
        }
    }
}
