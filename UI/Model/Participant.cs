
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using UI.Builder;
using UI.Communication.Interfaces;
using UI.Enums;

namespace UI.Model
{
    /// <summary>
    /// the visual representation of a car, able to drive through the world
    /// </summary>
    public class Participant : ObservableModelBase
    {
        int _id;
        BitmapImage _bi;
        ParticipantType _type;
        float _rot;
        float _posX;
        float _posY;
        int _tilePosX;
        int _tilePosY;
        string _viewInfo = "Test";
		bool _damaged = false;
		String _driverType;
		int maxVelocity = 1;
		int _velocity = 0;
		int maxFuel = 1;
		int _fuel = 0;
        bool _visible = true;
        Dictionary<string, string> _optInfos;
        Repository rep = Repository.instance;

        public Participant(ParticipantType type)
        {   
            _type = type;
            _bi = rep.getParticipantImage(type);
        }

        public Participant(int id, ParticipantType type, float relPosX, float relPosY, float rot, Dictionary<string, string> optInfos)
        {
            _id = id;
            _type = type;
            _bi = rep.getParticipantImage(type);
            _rot = rot;
            _posX = relPosX;
            _posY = relPosY;
            _optInfos = optInfos;
        }

        /// <summary>
        /// Constructor for Participant Placing
        /// </summary>
        /// <param name="type"></param>
        /// <param name="tilePosX"></param>
        /// <param name="tilePosY"></param>
        /// <param name="rot"></param>
        public Participant(ParticipantType type, int tilePosX, int tilePosY, float rot)
        {
            _type = type;
            _tilePosX = tilePosX;
            _tilePosY = tilePosY;
            _rot = rot;
        }

        public Participant(BitmapImage bi, float relPosX, float relPosY, float rot)
        {
            _bi = bi;
            _rot = rot;
            _posX = relPosX;
            _posY = relPosY;
        }

        # region properties

        public bool Visible
        {
            get
            {
                return _visible;
            }
            set
            {
                _visible = value;
                if (value)
                {
                    Bitmap = rep.getParticipantImage(_type);
                }
                else
                {
                    Bitmap = rep.getParticipantImage(ParticipantType.None);
                }
            }
        }

        public int ID
        {
            get
            {
                return _id;
            }
            set
            {
                _id = value;
            }
        }

        public int TilePosX
        {
            get
            {
                return _tilePosX;
            }
            set
            {
                _tilePosX = value;
            }
        }

        public int TilePosY
        {
            get
            {
                return _tilePosY;
            }
            set
            {
                _tilePosY = value;
            }
        }



        public BitmapImage Bitmap
        {
            get
            {
                return _bi;
            }
            set
            {
                _bi = value;
                OnPropertyChanged("Bitmap");
            }
        }

        public ParticipantType Type
        {
            get 
            {
                return _type;
            }
            set 
            {
                _type = value;
                this._bi = rep.getParticipantImage(value);
            }
        }

        public float Rot
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
                OnPropertyChanged("Rot");
            }
        }

        public float PosX
        {
            get
            {
                return _posX;
            }
            set
            {
                _posX = value;
                OnPropertyChanged("PosX");
            }
        }

        public float PosY
        {
            get
            {
                return _posY;
            }
            set
            {
                _posY = value;
                OnPropertyChanged("PosY");
            }
        }

        public Dictionary<string,string> OptInfos
        {
            get
            {
                return _optInfos;
            }
            set
            {
                _optInfos = EditOptionInfos(value);
                ViewInfo = InfoStringBuilder.BuildOptionInfos(_optInfos);
            }
        }
        # endregion

        public Participant Copy { 
            get {
                return new Participant(_id, _type, _posX, _posY, _rot, _optInfos); 
            } 
        }

        public string ViewInfo {
            get {
                return _viewInfo;
            }
            set {
                _viewInfo = value;
                OnPropertyChanged("ViewInfo");
            }
        }

        public bool Damaged
        {
            set
            {
                _damaged = value;
                if (value) {
                    System.Diagnostics.Debug.WriteLine("Damaged gesetzt");
                    Bitmap = rep.getDamagedParticipantImage(_type);
                }
            }
            get {
                return _damaged;
            }
        }

		public string DriverType
		{
			get
			{
				return _driverType;
			}
			set
			{
				_driverType = value;
				OnPropertyChanged("DriverType");
			}
		}

		public int Velocity
		{
			set
			{
				_velocity = value;
				OnPropertyChanged("Velocity");
				OnPropertyChanged("VelocityBar");
			}
			get
			{
				return _velocity;
			}
		}

		public int VelocityBar
		{
			set
			{
				OnPropertyChanged("VelocityBar");
			}
			get
			{
				int _velBar = Convert.ToInt32(((double)Velocity / (double)maxVelocity) * 100);
				return _velBar;
			}
		}

		public int Fuel
		{
			set
			{
				_fuel = value;
				OnPropertyChanged("Fuel");
				OnPropertyChanged("FuelBar");
			}
			get
			{
				return _fuel;
			}
		}

		public int FuelBar
		{
			set
			{
				OnPropertyChanged("FuelBar");
			}
			get
			{
				int _fuelBar = Convert.ToInt32(((double)Fuel / (double)maxFuel) * 100);
				return _fuelBar;
			}
		}

        private Dictionary<string, string> EditOptionInfos(Dictionary<string, string> opt)
		{
            Dictionary<string, string> temp = new Dictionary<string, string>();
			// grab Damaged
            if (opt.ContainsKey(OptInfosKey.Damaged.ToString("g")))
            {
                Damaged = Convert.ToBoolean(opt[OptInfosKey.Damaged.ToString("g")]);   
            }
			// grab Velocity
            if (opt.ContainsKey(OptInfosKey.Velocity.ToString("g")))
            {
                double v = Convert.ToDouble(opt[OptInfosKey.Velocity.ToString("g")].Replace(".", NumberFormatInfo.CurrentInfo.CurrencyDecimalSeparator));
				Velocity = (int)Math.Ceiling(v);
            }
			// grab MaxVelocity
			if (opt.ContainsKey(OptInfosKey.MaxVelocity.ToString("g")))
			{
				int mv = (int)Convert.ToDouble(opt[OptInfosKey.MaxVelocity.ToString("g")].Replace(".", NumberFormatInfo.CurrentInfo.CurrencyDecimalSeparator));
				maxVelocity = mv;
			}
			// grab Fuel
            if (opt.ContainsKey(OptInfosKey.Fuel.ToString("g")))
            {
                double f = Convert.ToDouble(opt[OptInfosKey.Fuel.ToString("g")].Replace(".", NumberFormatInfo.CurrentInfo.CurrencyDecimalSeparator));
				Fuel = (int)Math.Ceiling(f);
            }
			// grab MaxFuel
			if (opt.ContainsKey(OptInfosKey.MaxFuel.ToString("g")))
			{
				int mf = (int)Convert.ToDouble(opt[OptInfosKey.MaxFuel.ToString("g")].Replace(".", NumberFormatInfo.CurrentInfo.CurrencyDecimalSeparator));
				maxFuel = mf;
			}
			// grab DriverType
            if (opt.ContainsKey(OptInfosKey.DriverType.ToString("g"))) {
				_driverType = opt[OptInfosKey.DriverType.ToString("g")].ToLower();
            }
            return temp;       
        }
    }
}
