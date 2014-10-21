using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace UI.Model
{
    /// <summary>
    /// abstract mother class of all viewable objects seen on the map
    /// this way all objects, settable by the user, can be used in the menu
    /// </summary>
    abstract public class Viewable : ObservableModelBase
    {
        protected BitmapImage _bi;
        protected float _rot;
        protected int _posX;
        protected int _posY;

        /// <summary>
        /// 
        /// </summary>
        public BitmapImage BmImage
        {
            get
            {
                return _bi;
            }

            set
            {
                _bi = value;
                OnPropertyChanged("BmImage");
            }
        }

        /// <summary>
        /// Rotation on the Screen
        /// </summary>
        public float Rotation
        {
            get
            {
                return _rot;
            }
            set
            {
                float degree = value % 360;
                if (degree < 0)
                {
                    _rot = degree + 360;
                }
                else
                {
                    _rot = degree;
                }
                OnPropertyChanged("Rotation");
            }
        }


        /// <summary>
        /// X Position
        /// </summary>
        public int X
        {
            get
            {
                return _posX;
            }

            set
            {
                _posX = value;
                OnPropertyChanged("X");
            }
        }

        /// <summary>
        /// Y Position
        /// </summary>
        public int Y
        {
            get
            {
                return _posY;
            }

            set
            {
                _posY = value;
                OnPropertyChanged("Y");
            }
        }

        public Viewable(BitmapImage bi, int posX, int posY, float rot)
        {
            this._bi = bi;
            this._posX = posX;
            this._posY = posY;
            this._rot = rot;
        }

        public Viewable() { }
    }
}
