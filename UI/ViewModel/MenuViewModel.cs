using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using UI.Commands;
using UI.Communication;
using UI.Controller.Interface;
using UI.Communication.Exceptions;
using UI.Enums;
using UI.Helper;
using UI.Model;

namespace UI.ViewModel
{
    /// <summary>
    /// Manages all information required by the tile menu
    /// </summary>
    public class MenuViewModel : ObservableModelBase
    {
        public event EventHandler<NewTileChanged> newTileChangedHandler;
        public event EventHandler<NewParticipantChanged> newParticipantChangedHandler;
        public event EventHandler<StateChanged> stateChangedHandler;

        private MenuState state = MenuState.None;

        ObservableCollection<MenuIcon> _menuIcons;
        Repository TileImageRepo;

        int _menu_length = 5;
        private Brush _bgBrush = SystemColors.WindowBrush;
        
        private Menu menu = new Menu();
        private bool _menuExpanded = true;
        private MenuCategories selectedCategory = MenuCategories.Landscapes;
        
		private string _topMenuExpanded = null;

        private Tile _newTile = null; // cursor Tile
		private Tile _errorTile = null;
        private ParticipantType actParticipantType = ParticipantType.CabrioPink;

        private Participant _selectedParticipant;
        
        private bool _exceptionVisibility = false;
		private bool _participantInfoExpanded = false;

        private ZoomViewModel _zoom;

        private Network net;

		private ISoundController soundcontroller;
		private int _musicVolume;

		public MenuViewModel(Network network, ZoomViewModel zoom, ISoundController soundcontroller)
		{
            _menuIcons = new ObservableCollection<MenuIcon>();
            changeCategory(MenuCategories.Landscapes);
            TileImageRepo = Repository.instance;
            net = network;
            _zoom = zoom;
			this.soundcontroller = soundcontroller;
			_musicVolume = 50;
        }


        #region methods

        /// <summary>
        /// fill the tank of selected car
        /// </summary>
        /// <param name="t"></param>
        private void OnFillTank(Object t)
        {
            try
            {
            net.FillUpParticipant(SelectedParticipant);
        }
            catch (EndpointNotFoundException e)
            {
                //if network access fails, a screen should pop up
                //where you can close the window
                ExceptionVisibility = true;
            }
        }

        /// <summary>
        /// activates the bulldozer functionality when the corresponding icon is clicked
        /// </summary>
        /// <param name="t"></param>
        private void OnBulldozerClick(Object t)
        {
            if (state == MenuState.Bulldozer)
            {
                //turn bulldozer off if already on
                setState(MenuState.None);
            }
			else
			{
                //turns bulldozer on if bulldozer is not selected
                setState(MenuState.Bulldozer);
            }
        }

        /// <summary>
        /// removes a car, when clicked, by using the tow truck icon
        /// </summary>
        /// <param name="t">unused</param>
        private void OnTowTruckClick(Object t)
        {
            if (state == MenuState.TowTruck)
            {
                setState(MenuState.None);
            }
			else
			{
                setState(MenuState.TowTruck);
            }
        }

        /// <summary>
        /// sets NewTile to the Tile of the clicked MenuIcon
        /// and sets a MenuState depending on TilePlace or ParticipantPlace
        /// </summary>
        /// <param name="mi">the clicked MenuIcon</param>
        private void OnMenuIconClick(MenuIcon mi)
        {
            Tile t;
            switch (selectedCategory)
            {
                case MenuCategories.Landscapes: //landscape
                    t = (Tile)mi.V;
                    NewTile = t.Copy;
                    setNewTile(NewTile);
                    setState(MenuState.TilePlace);
                    break;
                case MenuCategories.Streets: //street
                    t = (Tile)mi.V;
                    NewTile = t.Copy;
                    setNewTile(NewTile);
                    setState(MenuState.TilePlace);
                    break;
                case MenuCategories.Participants: //car
                    Participant p = (Participant)mi.V;
                    NewTile = new Tile();
                    NewTile.BmImage = p.Bitmap;
                    setNewTile(NewTile);
                    actParticipantType = p.Type;
                    setNewParticipant(actParticipantType);
                    setState(MenuState.ParticipantPlace);
                    break;
                default:
                    break;
            }
        }

        /// <summary>
        /// fill current menu with selected tiles from category L,S,I
        /// </summary>
        public void changeCategory(MenuCategories category)
        {
            MenuIcons.Clear();
            foreach (MenuIcon mi in menu.Icons)
            {
                if (mi.Category == category)
                {
                    //loops through all MenuIcons stored and only adds the ones
                    //that are classified as the selected category
                    _menuIcons.Add(mi);
                }
            }
            selectedCategory = category;
        }

        
        /// <summary>
        /// sets the category of tiles shown in the tile menu
        /// </summary>
        /// <param name="t"></param>
        private void OnCategoryClick(Object t)
        {
            //Setting MenuState to None until MenuIcon is clicked
            //to make sure that MenuState is the correct one
            MenuCategories newCategory = selectedCategory;
            setState(MenuState.None);

            switch (t.ToString())
            {
                case "L":
                    newCategory = MenuCategories.Landscapes;
                    break;
                case "S":
                    newCategory = MenuCategories.Streets;
                    break;
                case "C":
                    newCategory = MenuCategories.Participants;
                    break;
                default:
                    newCategory = MenuCategories.Landscapes; // really should not happen
                    break;
            }
            if (selectedCategory != newCategory)
            {
                changeCategory(newCategory);
                // expand menu if collapsed
                if (!_menuExpanded)
                {
                    _menuExpanded = true;
                    OnPropertyChanged("MenuExpanded");
                }

                // set tile length in menu for horizontal scrollbar 
                MENU_LENGTH = _menuIcons.Count;
            }
            else
            {
                // expand/collapse menu
                _menuExpanded = !_menuExpanded;
                OnPropertyChanged("MenuExpanded");
            }
        }


        /// <summary>
        ///  expands the top left menu
        /// </summary>
        /// <param name="t"></param>
		private void ShowSettings(Object t)
		{
			string str = (String)t;
			if (str.Equals(_topMenuExpanded))
			{
				_topMenuExpanded = null;
			}
			else
			{
				_topMenuExpanded = str;
			}

			OnPropertyChanged("TopMenuExpanded");
		}

        /// <summary>
        /// skips a song
        /// </summary>
        /// <param name="t"></param>
		private void SkipSong(Object t)
		{
			soundcontroller.SkipInGameSound();
		}


        /// <summary>
        /// Choose an item from menu (set newtile)
        /// </summary>
        /// <param name="x"></param>
        private void setNewTile(Tile x)
        {
            NewTile = x;
            NewTileChanged t = new NewTileChanged();
            t.newTile = x;
            newTileChangedHandler(this, t);
        }

        /// <summary>
        /// sets the ParticipantType of the chose Participant an notifys the map
        /// </summary>
        /// <param name="x">ParticipantType</param>
        private void setNewParticipant(ParticipantType x)
        {
            actParticipantType = x;
            NewParticipantChanged p = new NewParticipantChanged();
            p.participantType = x;
            System.Diagnostics.Debug.WriteLine("ParticipantType chosen: " + x);
            newParticipantChangedHandler(this, p);
        }

        /// <summary>
        /// sets the chosen menuState an notifys the map
        /// </summary>
        /// <param name="state"></param>
        private void setState(MenuState state)
        {
            this.state = state;
            StateChanged s = new StateChanged();
            s.newState = state;
            stateChangedHandler(this, s);
        }

        /// <summary>
        /// Click on minimap to scroll to position of mouseclick
        /// </summary>
        /// <param name="t"></param>
        private void OnMiniMapClick(Object t) {
            Tile tile = (Tile)t;
            _zoom.ScrollToTile(tile);
        }

        #endregion

        #region commands
        /// <summary>
		/// ActionCommand for click on menu to select an object
		/// </summary>
		private ViewableCommand<Object> _showSettingsCommand;

		public ViewableCommand<Object> ShowSettingsCommand
		{
			get
			{
				if (_showSettingsCommand == null)
				{
					_showSettingsCommand = new ViewableCommand<Object>(ShowSettings);
				}
				return _showSettingsCommand;
			}
		}

        /// <summary>
        /// ActionCommand for click on menu to select an object
        /// </summary>
		private ViewableCommand<Object> _skipSongCommand;

		public ViewableCommand<Object> SkipSongCommand
		{
			get
			{
				if (_skipSongCommand == null)
				{
					_skipSongCommand = new ViewableCommand<Object>(SkipSong);
				}
				return _skipSongCommand;
			}
		}

		/// <summary>
		/// ActionCommand for click on menu to select an object
		/// </summary>
		private ViewableCommand<MenuIcon> _clickTileMenuCommand;
        public ViewableCommand<MenuIcon> ClickTileMenuCommand
        {
            get
            {
                if (_clickTileMenuCommand == null)
                {
                    _clickTileMenuCommand = new ViewableCommand<MenuIcon>(OnMenuIconClick);
                }
                return _clickTileMenuCommand;
            }
        }

        /// <summary>
        /// ActionCommand for click on a category
        /// </summary>
        private ViewableCommand<Object> _selectCategoryCommand;

        public ViewableCommand<Object> SelectCategoryCommand
        {
            get
            {
                if (_selectCategoryCommand == null)
                {
                    _selectCategoryCommand = new ViewableCommand<Object>(OnCategoryClick);
                }
                return _selectCategoryCommand;
            }
        }

        /// <summary>
        /// ActionCommand for click on bulldozer button
        /// </summary>
        private ViewableCommand<Object> _bulldozerCommand;

        public ViewableCommand<Object> BulldozerCommand
        {
            get
            {
                if (_bulldozerCommand == null)
                {
                    _bulldozerCommand = new ViewableCommand<Object>(OnBulldozerClick);
                }
                return _bulldozerCommand;
            }
        }

        /// <summary>
        /// Actioncommand for click on towtruck button
        /// </summary>
        private ViewableCommand<Object> _towTruckCommand;

        public ViewableCommand<Object> TowTruckCommand
        {
            get
            {
                if (_towTruckCommand == null)
                {
                    _towTruckCommand = new ViewableCommand<Object>(OnTowTruckClick);
                }
                return _towTruckCommand;
            }
        }

        /// <summary>
        /// Actioncommand for click on fill tank button
        /// </summary>
        private ViewableCommand<Object> _fillTankCommand;

        public ViewableCommand<Object> FillTankCommand
        {
            get
            {
                if (_fillTankCommand == null)
                {
                    _fillTankCommand = new ViewableCommand<Object>(OnFillTank);
                }
                return _fillTankCommand;
            }
        }

        /// <summary>
        /// Actioncommand for click on the minimap
        /// </summary>
        private ViewableCommand<Object> _miniMapClickCommand;

        public ViewableCommand<Object> MiniMapClickCommand
        {
            get
            {
                if (_miniMapClickCommand == null)
                {
                    _miniMapClickCommand = new ViewableCommand<Object>(OnMiniMapClick);
                }
                return _miniMapClickCommand;
        }
        }

        #endregion

        #region properties

        /// <summary>
        /// Menuicons on the bottom left
        /// </summary>
        public ObservableCollection<MenuIcon> MenuIcons
        {
            get
            {
                return _menuIcons;
            }
        }

        /// <summary>
        /// Menu expanded or not
        /// </summary>
        public bool MenuExpanded
        {
            get
            {
                return _menuExpanded;
            }
        }

        /// <summary>
        /// length of one category in menu (to determine if horizontal scrollbar is needed)
        /// </summary>
        public int MENU_LENGTH
        {
            get
            {
                return _menu_length;
            }
            set
            {
                _menu_length = value;
                OnPropertyChanged("MENU_LENGTH");
            }
        }


        /// <summary>
        /// selected participanttype of the menu
        /// </summary>
		public Participant SelectedParticipant
		{
			get
			{
				return _selectedParticipant;
			}
			set
			{
				_selectedParticipant = value;
				OnPropertyChanged("SelectedParticipant");
			}
		}

        /// <summary>
        /// Participantinfo expanded or not
        /// </summary>
		public bool ParticipantInfoExpanded
		{
			get
			{
				return _participantInfoExpanded;
			}
			set
			{
				_participantInfoExpanded = value;
				OnPropertyChanged("ParticipantInfoExpanded");
            }
        }

        /// <summary>
        /// top menu expanded
        /// </summary>
		public string TopMenuExpanded
		{
			get
			{
				return _topMenuExpanded;
            }
			set
			{
				_topMenuExpanded = value;
				OnPropertyChanged("TopMenuExpanded");
            }
        }

        /// <summary>
        /// shows exception window and button to close if exception happens (set _exceptionVisibility to true)
        /// </summary>
        public bool ExceptionVisibility
        {
            get
            {
                return _exceptionVisibility;
            }
            set
            {
                _exceptionVisibility = value;
                OnPropertyChanged("ExceptionVisibility");
            }
        }

        /// <summary>
        /// selected tile from menu
        /// </summary>
        public Tile NewTile
        {
            get
            {
                return _newTile;
            }
            set
            {
                _newTile = value;
				OnPropertyChanged("NewTile");
			}
		}

        /// <summary>
        /// error tile visualizes, if a participant is settable
        /// </summary>
		public Tile ErrorTile
		{
			get
			{
				return _errorTile;
			}
			set
			{
				_errorTile = value;
				OnPropertyChanged("ErrorTile");
            }
        }

        /// <summary>
        /// Control sound volume
        /// </summary>
		public int MusicVolume
		{
			get
			{
				return _musicVolume;
			}
			set
			{
				_musicVolume = value;
				soundcontroller.AdjustInGameVolume(_musicVolume);
				OnPropertyChanged("MusicVolume");
			}
		}

#endregion
    }
}
