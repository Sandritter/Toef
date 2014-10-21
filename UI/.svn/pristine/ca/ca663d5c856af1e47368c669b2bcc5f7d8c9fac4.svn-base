using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using System.Windows.Media.Imaging;
using UI.Commands;
using UI.Communication;
using UI.Communication.Interfaces;
using UI.Model;
using GalaSoft.MvvmLight.Command;
using System.Windows;
using UI.Communication.Helper;
using UI.Communication.Enums;
using System.Windows.Media;
using UI.Communication.Events;
using System.Windows.Threading;
using UI.Helper;
using UI.ViewModel;
using UI.Controller.Interface;
using UI.Enums;
using UI.Repositories;
using UI.Communication.Exceptions;


namespace UI
{
    /// <summary>
    /// Manages the interaction of the user with the world's map
    /// </summary>
    public class MapViewModel : ObservableModelBase
    {
        private ObservableCollection<Tile> _tiles;
        private ObservableCollection<Participant> _participant;

        private int _MapHeightInPixel;
        private int _MapWidthInPixel;

        private bool isMouseDown = false;

        private Tile _newTile = null;
        private Tile _errorTile = null;

        private Network net;
        private ISoundController soundcontroller;
        private Map map;
        private ParticipantManager pManager;
        
        private Repository imageRep;
        private CursorRepository cursorRep;

        private TileType standardTileType = TileType.Grass;
        private ParticipantType actParticipantType;
        private MenuState state = MenuState.None;

        private MenuViewModel _menu;
        private ZoomViewModel _zoom;


        private bool _hitTestVisible = true;
        private bool _isNotSettable = true;
        private bool _menuVisibility = false;
        private bool _loadScreenVisibility = true;

      
        public MapViewModel(MenuViewModel menu, Network network, ISoundController sound, ZoomViewModel zoom)
        {
            _zoom = zoom;

            _menu = menu;
            _menu.newParticipantChangedHandler += onNewParticipantChanged;
            _menu.newTileChangedHandler += onNewTileChanged;
            _menu.stateChangedHandler += onStateChanged;

            soundcontroller = sound;
            soundcontroller.StartInGameSound();

            map = new Map(network);
            map.mapChangedHandler += RenewMap;


            pManager = new ParticipantManager(network, map);
            pManager.participantAddedHandler += AddParticipant;
            pManager.participantDeletedHandler += DeleteParticipant;

            _tiles = new ObservableCollection<Tile>();
            _participant = new ObservableCollection<Participant>();

            net = network;
            net.HostDownReceiver.Handler += onHostDown;

            _MapHeightInPixel = map.HeightInTiles * Properties.Settings.Default.TileSize;
            _MapWidthInPixel = map.WidthInTiles * Properties.Settings.Default.TileSize;

            imageRep = Repository.instance;
            cursorRep = new CursorRepository();

            _newTile = new Tile(0, 0, 0, TileType.None);
            _errorTile = new Tile(0, 0, 0, (TileType) 0);
        }


        #region Properties

        /// <summary>
        /// Binding for the errormask visibility
        /// </summary>
        public bool IsNotSettable {
            get { return _isNotSettable; }
            set {
                _isNotSettable = value;
                OnPropertyChanged("IsNotSettable");
            }
        }

        /// <summary>
        /// currently chosen tile
        /// </summary>
        public Tile NewTile {
            get { return _newTile; }
            set {
                _newTile = value;
                OnPropertyChanged("NewTile");
            }
        }

        /// <summary>
        /// turn "clicklistener" off and on of object
        /// </summary>
        public bool HitTestVisible {
            get { return _hitTestVisible; }
            set {
                _hitTestVisible = value;
                OnPropertyChanged("HitTestVisible");
            }
        }

        /// <summary>
        /// Mapheight in pixel
        /// </summary>
        public int MAP_HEIGHT {
            get { return _MapHeightInPixel; }
            set {
                _MapHeightInPixel = value;
                OnPropertyChanged("MAP_HEIGHT");
            }
        }

        /// <summary>
        /// Mapwidth in pixel
        /// </summary>
        public int MAP_WIDTH {
            get { return _MapWidthInPixel; }
            set {
                _MapWidthInPixel = value;
                OnPropertyChanged("MAP_WIDTH");
            }
        }

        /// <summary>
        /// collection of tiles on map
        /// </summary>
        public ObservableCollection<Tile> Tiles {
            get { return _tiles; }
            set {
                _tiles = value;
                OnPropertyChanged("Tiles");
            }
        }

        /// <summary>
        /// all current participants 
        /// </summary>
        public ObservableCollection<Participant> Participants {
            get { return _participant; }
            set {
                _participant = value;
                OnPropertyChanged("Participant");
            }
        }

        /// <summary>
        /// Menu fades out or in if variable is set
        /// </summary>
        public bool MenuVisibility {
            get { return _menuVisibility; }
            set {
                _menuVisibility = value;
                OnPropertyChanged("MenuVisibility");
            }
        }

        /// <summary>
        /// Shows Loadingscreen and turns it off
        /// </summary>
        public bool LoadScreenVisibility
        {
            get { return _loadScreenVisibility; }
            set {
                _loadScreenVisibility = value;
                OnPropertyChanged("LoadScreenVisibility");
            }
        }


        //END MENU
        #endregion

        #region Commands
        /// <summary>
        /// ActionCommand for click on tile
        /// </summary>
        private ViewableCommand<Tile> _TileDownCommand;
        public ViewableCommand<Tile> TileDownCommand
        {
            get
            {
                return _TileDownCommand ?? (_TileDownCommand = new ViewableCommand<Tile>(OnTileDown));
            }
        }

        /// <summary>
        /// ActionCommand for right-click on tile
        /// </summary>
        private ViewableCommand<Tile> _TileDownCommandR;
        public ViewableCommand<Tile> TileDownCommandR
        {
            get
            {
                return _TileDownCommandR ?? (_TileDownCommandR = new ViewableCommand<Tile>(OnTileDownR));
            }
        }

        /// <summary>
        /// ActionCommand for mouse up on tile
        /// </summary>
        private ViewableCommand<Tile> _TileUpCommand;
        public ViewableCommand<Tile> TileUpCommand
        {
            get
            {
                return _TileUpCommand ?? (_TileUpCommand = new ViewableCommand<Tile>(OnTileUp));
            }
        }

        /// <summary>
        /// ActionCommand for mouse enter on tile
        /// </summary>
        private ViewableCommand<Tile> _TileEnterCommand;
        public ViewableCommand<Tile> TileEnterCommand
        {
            get
            {
                return _TileEnterCommand ?? (_TileEnterCommand = new ViewableCommand<Tile>(OnTileEnter));
            }
        }

        /// <summary>
        /// ActionCommand for mouse wheel
        /// </summary>
        private RelayCommand<MouseWheelEventArgs> _TileRotateCommand;
        public RelayCommand<MouseWheelEventArgs> TileRotateCommand
        {
            get
            {
                return _TileRotateCommand ?? (_TileRotateCommand = new RelayCommand<MouseWheelEventArgs>(OnTileRotate));
            }
        }

        /// <summary>
        /// ActionCommand for car click
        /// </summary>
        private ViewableCommand<Object> _carClickCommand;
        public ViewableCommand<Object> CarClickCommand
        {
            get
            {
                return _carClickCommand ?? (_carClickCommand = new ViewableCommand<Object>(OnCarClick));
            }
        }


        /// <summary>
        /// ActionCommand for click on rotatebutton
        /// </summary>
        private ViewableCommand<Object> _rotateCommand;
        public ViewableCommand<Object> RotateCommand
        {
            get
            {
                return _rotateCommand ?? (_rotateCommand = new ViewableCommand<Object>(OnRotateClick));
            }
        }

        /// <summary>
        /// ActionCommand for click on logout button
        /// </summary>
        private ICommand _logoutCommand;
        public ICommand LogoutCommand
        {
            get
            {
                return _logoutCommand ?? (_logoutCommand = new ActionCommand(dummy => logout(), dummy => true));
            }
        }


        /// <summary>
        /// ActionCommand for network exception, toef should be shut down
        /// </summary>
        private ICommand _shutdownCommand;
        public ICommand ShutdownCommand
        {
            get
            {
                return _shutdownCommand ?? (_shutdownCommand = new ActionCommand(dummy => shutdown(), dummy => true));
            }
        }

        #endregion

        #region Map Methods

        /// <summary>
        /// Callback, when map has changed
        /// called by the map
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="mapArgs">map arguments</param>
        private void RenewMap(object sender, MapChanged mapArgs)
        {   
            //sets the map
            Application.Current.Dispatcher.Invoke(new Action(() =>  
                Tiles = mapArgs.Map
            ), System.Windows.Threading.DispatcherPriority.Send);

            // sets the visibility of the loadingscreen to false, when system in idle
            Application.Current.Dispatcher.Invoke(new Action(() =>
                LoadScreenVisibility = false
            ), System.Windows.Threading.DispatcherPriority.SystemIdle);

            // sets the visibility of the gui to true, when system in idle
            Application.Current.Dispatcher.Invoke(new Action(() =>
                MenuVisibility = true
            ), System.Windows.Threading.DispatcherPriority.SystemIdle);

            MAP_HEIGHT = mapArgs.Height * Properties.Settings.Default.TileSize;
            MAP_WIDTH = mapArgs.Width * Properties.Settings.Default.TileSize;
        }

        /// <summary>
        /// Adds a new participant, called by the ParticipantManager
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="participantArgs">participant arguments</param>

        private void AddParticipant(object sender, ParticipantArgs participantArgs) {
            Application.Current.Dispatcher.Invoke(new Action(() => 
                Participants.Add(participantArgs.participant)
            ));
        }
        
        /// <summary>
        /// Deletes a Participant, called by the ParticipantManager
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="participantArgs">participant arguments</param>
        private void DeleteParticipant(object sender, ParticipantArgs participantsArgs)
        {
            Application.Current.Dispatcher.Invoke(new Action(() =>
                Participants.Remove(participantsArgs.participant)
            ));
        }

        /// <summary>
        /// handle left mouse click
        /// sets tile to current selected tile
        /// </summary>
        /// <param name="t">Tile</param>
        private void OnTileDown(Tile t)
        {
            switch (state) {
                case MenuState.None:
                    break;
                case MenuState.TowTruck:
                    break;
                case MenuState.Bulldozer:
                    isMouseDown = true;
                    soundcontroller.PlayEffectSound(SoundEffect.DeleteTile);
                    try {
                        map.UpdateTile(_newTile.Copy);
                    } catch (EndpointNotFoundException e) {
                        this._menu.ExceptionVisibility = true;
                    }
                    
                    break;
                case MenuState.ParticipantPlace:
                    Participant p = new Participant(actParticipantType, t.X, t.Y, _newTile.Rotation);
                    if (ParticipantEnumHelper.GetUpperParticipantType(actParticipantType) == UpperParticipantType.Car) {
                        soundcontroller.PlayEffectSound(SoundEffect.PlaceCar); // only for sound
                    } 
                    if (ParticipantEnumHelper.GetUpperParticipantType(actParticipantType) == UpperParticipantType.Ship) {
                        soundcontroller.PlayEffectSound(SoundEffect.PlaceShip); // only for sound
                    }
                    try {
                        net.PlaceParticipant(p);
                    } catch (EndpointNotFoundException e) {
                        this._menu.ExceptionVisibility = true;
                    }     
                    break;
                case MenuState.TilePlace:
                    isMouseDown = true;
                    soundcontroller.PlayEffectSound(SoundEffect.PlaceTile);
                    try {
                        map.UpdateTile(_newTile.Copy);
                    }
                    catch (EndpointNotFoundException e)
                    {
                        this._menu.ExceptionVisibility = true;
                    }
                    break;
            }
        }

        /// <summary>
        /// handle right mouse click
        /// deselects selected item
        /// </summary>
        /// <param name="t"></param>
        private void OnTileDownR(Tile t) {
            if (!isMouseDown) {
                setState(MenuState.None);
                _menu.ParticipantInfoExpanded = false;
            }
        }

        /// <summary>
        /// sets mouse down to false if click is released
        /// </summary>
        private void OnTileUp(Tile t) {
            isMouseDown = false;
        }

        /// <summary>
        /// shows current tile at mouseposition on mouse-over
        /// </summary>
        private void OnTileEnter(Tile t) {
            // sets the cursor-tile
            _newTile.X = t.X;
            _newTile.Y = t.Y;

            IsNotSettable = false;

            // checks if participant is placable
            if (state == MenuState.ParticipantPlace) {
                //ships only on water
                if (ParticipantEnumHelper.GetUpperParticipantType(actParticipantType) == UpperParticipantType.Ship) {
                    if (TileEnumHelper.GetUpperTileType(t.State) != UpperTileType.Water) {
                        IsNotSettable = true;
                    }        
                }

                //cars only on streets
                if (ParticipantEnumHelper.GetUpperParticipantType(actParticipantType) == UpperParticipantType.Car) {
                    if (TileEnumHelper.GetUpperTileType(t.State) != UpperTileType.Street) {
                        IsNotSettable = true;
                    }
                }
            }

            if (isMouseDown && state != MenuState.Bulldozer) { 
                //Update Model
                try {
                    map.UpdateTile(_newTile.Copy);
                }
                catch (EndpointNotFoundException e) {
                    this._menu.ExceptionVisibility = true;
                }
                
            }
            if (isMouseDown && state == MenuState.Bulldozer) {
                
                //newTile.BmImage = TileImageRepo.getTileImage(this.standardTileType);
                _newTile.State = this.standardTileType;
                try
                {
                    map.UpdateTile(_newTile.Copy);
                }
                catch (EndpointNotFoundException e)
                {
                    this._menu.ExceptionVisibility = true;
                }
            }
        }

        /// <summary>
        /// is called when simulation server is shut down
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onHostDown(object sender, EventArgs e) {
            System.Diagnostics.Debug.WriteLine("HOST DOWN!!!!!!!");
            try
            {
                Application.Current.Shutdown();
            }
            catch (InvalidOperationException i)
            {
                System.Diagnostics.Debug.WriteLine(i);
            }
        }

        /// <summary>
        /// sets rotation value to 0,90,180 or 270 if mousewheel is rotated and object is selected
        /// </summary>
        private void OnTileRotate(MouseWheelEventArgs e) {
            // zoom if menustate is none
            if (state == MenuState.None) {
                _zoom.Scale += (e.Delta / 1000f);
            } else {
                if (e.Delta < 0) {
                    _newTile.Rotation += 90;
                } else {
                    _newTile.Rotation -= 90;
                }
            }
            e.Handled = true;
        }


        /// <summary>
        /// clockwise rotates the selected tile, when the rotation button is clicked
        /// </summary>
        /// <param name="t"></param>
        private void OnRotateClick(Object t) {
            _newTile.Rotation += 90;
        }

        /// <summary>
        /// car click
        /// </summary>
        /// <param name="t"></param>
        private void OnCarClick(Object t) {
            if (state == MenuState.TowTruck) {
                soundcontroller.PlayEffectSound(SoundEffect.DeleteParticipant); // play deletesound
                try {
                    net.DeleteParticipant((Participant)t); // send to network
                }
                catch (EndpointNotFoundException e) {
                    this._menu.ExceptionVisibility = true;
                }
            }
            else {
                _menu.SelectedParticipant = (Participant)t; // sets the selected participant
                _menu.ParticipantInfoExpanded = true; //  shows participant info


                //play sound
                UpperParticipantType uType = ParticipantEnumHelper.GetUpperParticipantType(_menu.SelectedParticipant.Type);
                if(uType == UpperParticipantType.Car) {
                    soundcontroller.PlayEffectSound(SoundEffect.SelectCar);
                } 
                else if(uType == UpperParticipantType.Ship) {
                    soundcontroller.PlayEffectSound(SoundEffect.SelectShip);
                }     
            }
        }

        /// <summary>
        /// closes current window and opens serverwindow and disconnects from server
        /// </summary>
        private void logout() {
            try {
                net.Logout();
            } catch (EndpointNotFoundException e) {
                this._menu.ExceptionVisibility = true;
            }
            Application.Current.Shutdown();
        }

        /// <summary>
        /// closes current window and opens serverwindow, e.g. exception is thrown and toef crashes
        /// </summary>
        private void shutdown() {
            Application.Current.Shutdown();
        }

        /// <summary>
        /// sets the new Cursortile. Called from the menuviewmodel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onNewTileChanged(object sender, NewTileChanged e) {
            Application.Current.Dispatcher.Invoke(new Action(() =>
                NewTile = e.newTile
            ));
            actParticipantType = ParticipantType.None;
        }

        /// <summary>
        /// sets the chosen participant. called from the menuviewmodel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onNewParticipantChanged(object sender, NewParticipantChanged e) {
            actParticipantType = e.participantType;
        }

        /// <summary>
        /// sets the new menustate. called from the menuviewmodel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onStateChanged(object sender, StateChanged e) {
            setState(e.newState);    
        }

        /// <summary>
        /// sets the menustate
        /// </summary>
        /// <param name="s"></param>
        private void setState(MenuState s) {
            state = s;
   
            switch (s) {
                case MenuState.None:
                    HitTestVisible = true;
                    NewTile.State = TileType.None;
                    resetCursor();
                    break;
                case MenuState.TowTruck:
                    HitTestVisible = true;
                    NewTile.State = TileType.None;
                    setCursor(CursorEnum.tow_truck);
                    break;
                case MenuState.Bulldozer:
                    HitTestVisible = false;
                    NewTile.State = standardTileType;
                    setCursor(CursorEnum.bulldozer);
                    break;
                case MenuState.ParticipantPlace:
                    HitTestVisible = false;
                    resetCursor();
                    break;
                case MenuState.TilePlace:
                    HitTestVisible = false;
                    resetCursor();
                    break;
            }
        }

        /// <summary>
        /// sets the cursor
        /// </summary>
        /// <param name="ce">Cursorenum</param>
        private void setCursor(CursorEnum ce) {
            var str = Application.GetResourceStream(new Uri(cursorRep.GetCursorPath(ce)));
            Cursor c = new Cursor(str.Stream);

            Mouse.SetCursor(c);
            Mouse.OverrideCursor = c;
        }

        /// <summary>
        /// resets the cursor
        /// </summary>
        private void resetCursor() {
            Mouse.SetCursor(null);
            Mouse.OverrideCursor = null;
        }

        #endregion
    }
}
