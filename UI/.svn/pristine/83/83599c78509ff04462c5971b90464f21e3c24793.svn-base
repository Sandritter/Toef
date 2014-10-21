using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using UI.Enums;

namespace UI.Model
{
    /// <summary>
    /// Respository is Singleton!
    /// contains all images for visual objects
    /// </summary>
    public sealed class Repository
    {
        #region attributes
        private static Repository _instance = null;
        public static Repository instance
        {
            get
            {
                if (_instance == null)
                {
                    _instance = new Repository();
                }
                return _instance;
            }
        }

        Dictionary<TileType, BitmapImage> _tileImages;
        public Dictionary<TileType, BitmapImage> TileImages
        {
            get
            {
                return _tileImages;
            }
        }

        Dictionary<ParticipantType, BitmapImage> _participantImages;
        public Dictionary<ParticipantType, BitmapImage> ParticipantImages
        {
            get
            {
                return _participantImages;
            }
        }


        Dictionary<ParticipantType, BitmapImage> _damagedParticipantImages;
        public Dictionary<ParticipantType, BitmapImage> DamagedParticipantImages
        {
            get
            {
                return _damagedParticipantImages;
            }
        }
        #endregion

        /// <summary>
        ///  init of image Dictionaries
        /// </summary>
        private Repository()
        {
            _tileImages = new Dictionary<TileType, BitmapImage>();
            _participantImages = new Dictionary<ParticipantType, BitmapImage>();
            _damagedParticipantImages = new Dictionary<ParticipantType, BitmapImage>();

            // image names need to match enum strings
            foreach(TileType tt in Enum.GetValues(typeof(TileType)))
            {
                _tileImages.Add(tt, createBitmapImage("pack://application:,,,/Images/TileImgs/" + tt.ToString() + ".png"));
            }
            //int imageOffset = TileImages.Count;
            foreach(ParticipantType pt in Enum.GetValues(typeof(ParticipantType)))
            {
                if (pt == ParticipantType.None)
                {
                    _participantImages.Add(pt, createBitmapImage("pack://application:,,,/Images/ParticipantImgs/" + pt.ToString() + ".png"));
                }
                else {
                    string upperType = ParticipantEnumHelper.GetUpperParticipantType(pt).ToString("g");
                    //_tileImages.Add((TileType)imageOffset, createBitmapImage("pack://application:,,,/Images/ParticipantImgs/" + pt.ToString() + ".png"));
                    _participantImages.Add(pt, createBitmapImage("pack://application:,,,/Images/ParticipantImgs/" + upperType + "/Undamaged/" + pt.ToString() + ".png"));
                    _damagedParticipantImages.Add(pt, createBitmapImage("pack://application:,,,/Images/ParticipantImgs/" + upperType + "/Damaged/" + pt.ToString() + ".png"));
                }   
            }
        }

        #region functions
        /// <summary>
        /// returns the image of a given TileType
        /// </summary>
        /// <param name="tt"></param>
        /// <returns></returns>
        public BitmapImage getTileImage(TileType tt)
        {
            //System.Diagnostics.Debug.WriteLine(tt);
            return _tileImages[tt];
        }

        /// <summary>
        /// returns the image of a given ParticipantType
        /// </summary>
        /// <param name="pt"></param>
        /// <returns></returns>
        public BitmapImage getParticipantImage(ParticipantType pt)
        {
            return _participantImages[pt];
        }

        public BitmapImage getDamagedParticipantImage(ParticipantType pt) 
        {
            return _damagedParticipantImages[pt];
        }

        /// <summary>
        /// returns corresponding index to bitmapimage in Participant (for menu)
        /// </summary>
        /// <param name="bi"></param>
        /// <returns></returns>
        public ParticipantType getBitmapParticipantNr(BitmapImage bi)
        {
            // TODO: find faster solution, maybe inverse list?
            foreach (KeyValuePair<ParticipantType, BitmapImage> pair in _participantImages)
            {
                if (pair.Value.ToString().Equals(bi.ToString(), StringComparison.Ordinal))
                {
                    return pair.Key;
                }
            }
            return ParticipantType.None; // really must not happen
        }

        /// <summary>
        /// creates a BitmapImage from file path
        /// </summary>
        /// <param name="path">String path</param>
        /// <returns></returns>
        public BitmapImage createBitmapImage(String path)
        {
            BitmapImage _bi = new BitmapImage();
            _bi.BeginInit();
            _bi.UriSource = new Uri(path, UriKind.Absolute);
            _bi.EndInit();
            return _bi;
        }
        #endregion
    }
}

