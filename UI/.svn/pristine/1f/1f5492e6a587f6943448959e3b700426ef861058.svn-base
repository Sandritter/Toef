using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using UI.Communication;
using UI.Communication.Events;
using UI.Communication.Interfaces;
using System.Windows.Threading;
using UI.Controller.Interface;

namespace UI
{

    /// <summary>
    /// Represents the ViewModel of the ServerView
    /// </summary>
    public class ServerViewModel : ObservableModelBase
    {

        string _newServer = "";
        string _selectedServer;
        string _userName = "";
        string _lobbyName = "";
        int _selectedIndex = -1;

        DispatcherTimer dispatcherTimer;
        int waitSeconds = 3;
        string _deleteError = "";
        string _usernameError = "";


        //Colors for the "connected-light"
        const string colorGreen1 = "#FF0A9724";
        const string colorGreen2 = "#FF4AD363";
        const string colorRed1 = "#FFD34A4A";
        const string colorRed2 = "#FF970A0A";

        string _color1;
        string _color2;

        bool connected = false;

        ObservableCollection<String> serverList;

        Network net;
		ISoundController soundcontroller;
		private int _musicVolume;

        public ServerViewModel(Network network, ISoundController soundcontroller)
        {
            _color1 = colorRed1;
            _color2 = colorRed2;

            net = network;
            this.soundcontroller = soundcontroller;
			_musicVolume = 50;
            soundcontroller.StartMenuSound();
            net.ActiveSimulationReceiver.Handler += OnServerListUpdate;
        }

        #region Properties


        /// <summary>
        /// List of simulationserver
        /// </summary>
        public ObservableCollection<String> ServerList
        {
            get
            {
                return serverList;
            }
            set
            {
                serverList = value;
                OnPropertyChanged("ServerList");
            }
        }

        /// <summary>
        /// name of the server which will be created
        /// </summary>
        public String NewServer
        {
            get
            {
                return _newServer;
            }
            set
            {
                _newServer = value;
                OnPropertyChanged("NewServer");
            }
        }

        /// <summary>
        /// represents the name of the selected server
        /// </summary>
        public String SelectedServer
        {
            get
            {
                return _selectedServer;
            }
            set
            {
                _selectedServer = value;
                OnPropertyChanged("SelectedServer");
            }
        }

        /// <summary>
        /// represents the listindex of the selected server 
        /// </summary>
        public int SelectedIndex
        {
            get
            {
                return _selectedIndex;
            }
            set
            {
                _selectedIndex = value;
                OnPropertyChanged("SelectedIndex");
            }
        }

        /// <summary>
        /// represents the name of the user
        /// </summary>
        public String UserName
        {
            get
            {
                return _userName;
            }
            set
            {
                _userName = value;
            }
        }

        /// <summary>
        /// represents the lobby name
        /// </summary>
        public String LobbyName
        {
            get
            {
                return _lobbyName;
            }
            set
            {
                _lobbyName = value;
            }
        }

        /// <summary>
        /// represents the acutal text that is shown for an UserNameError
        /// </summary>
        public String UserNameError
        {
            get
            {
                return _usernameError;
            }
            set
            {
                _usernameError = value;
                OnPropertyChanged("UserNameError");
                if (_usernameError != "")
                {
                    deleteMessage();
                }
            }
        }

        /// <summary>
        /// represents the acutal text that is shown for an DeleteError
        /// </summary>
        public String DeleteError
        {
            get
            {
                return _deleteError;
            }
            set
            {
                _deleteError = value;
                OnPropertyChanged("DeleteError");
                if (_deleteError != "")
                {
                    deleteMessage();
                }
            }
        }

        /// <summary>
        /// Musicvolume
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
				soundcontroller.AdjustMenuVolume(_musicVolume);
				OnPropertyChanged("MusicVolume");
			}
		}

        /// <summary>
        /// Color1 of the "connect-light" gradient
        /// </summary>
        public string Color1 {
            get {
                return this._color1;
            }
            set {
                this._color1 = value;
                OnPropertyChanged("Color1");
            }
        }

        /// <summary>
        /// Color2 of the "connect-light" gradient
        /// </summary>
        public string Color2
        {
            get
            {
                return this._color2;
            }
            set
            {
                this._color2 = value;
                OnPropertyChanged("Color2");
            }
        }

        #endregion

        #region commands

        /// <summary>
        /// Actioncommand for adding a new server
        /// </summary>
        private ICommand _addServerCommand;
        public ICommand AddServerCommand
        {
            get
            {
                // canExecute if NewServer is not null
                return _addServerCommand ?? (_addServerCommand = new ActionCommand(dummy => this.addServer(NewServer), dummy => NewServer.Trim() != ""));
            }
        }

        /// <summary>
        /// Actioncommand for refreshing the serverlist
        /// </summary>
        private ICommand _refreshServerCommand;
        public ICommand RefreshServerCommand
        {
            get
            {
                //canExecute if connected
                return _refreshServerCommand ?? (_refreshServerCommand = new ActionCommand(dummy => this.refreshServer(), dummy => connected));
            }
        }

        /// <summary>
        /// Actioncommand for deleting a server
        /// </summary>
        private ICommand _deleteServerCommand;
        public ICommand DeleteServerCommand
        {
            get
            {
                //canExecue if a server is selected
                return _deleteServerCommand ?? (_deleteServerCommand = new ActionCommand(dummy => this.deleteServer(SelectedServer), dummy => SelectedIndex >= 0));
            }
        }

        /// <summary>
        /// Actioncommand for joining a server
        /// </summary>
        private ICommand _joinCommand;
        public ICommand JoinCommand
        {
            get
            {
                //canExecute if a server is selected and username is not empty
                return _joinCommand ?? (_joinCommand = new ActionCommand(dummy => this.Join(UserName), dummy => SelectedIndex >= 0 && UserName.Trim() != ""));
            }
        }

        /// <summary>
        /// Actioncommand for changing the lobbyserver
        /// </summary>
        private ICommand _changeLobby;
        public ICommand ChangeLobby
        {
            get
            {
                // canExecute if NewServer is not null
                return _changeLobby ?? (_changeLobby = new ActionCommand(dummy => this.changeLobby(LobbyName), dummy => LobbyName.Trim() != ""));
            }
        }

        /// <summary>
        /// Actioncommand for exit the application
        /// </summary>
        private ICommand _exitCommand;
        public ICommand ExitCommand
        {
            get
            {
                return _exitCommand ?? (_exitCommand = new ActionCommand(dummy => this.exit(), dummy => true));
            }
        }

        /// <summary>
        /// Actioncommand for skipping a song
        /// </summary>
        private ActionCommand _skipSongCommand;
        public ActionCommand SkipSongCommand
        {
            get
            {
                return _skipSongCommand ?? (_skipSongCommand = new ActionCommand(SkipSong));
            }
        }

        #endregion

        #region methods


        /// <summary>
        /// deletes an errormessage after some time
        /// </summary>
        private void deleteMessage()
        {
            this.dispatcherTimer = new DispatcherTimer();
            this.dispatcherTimer.Tick += new EventHandler(this.dispatcherTimer_Tick);
            this.dispatcherTimer.Interval = new TimeSpan(0, 0, this.waitSeconds);
            this.dispatcherTimer.Start();
        }

        /// <summary>
        /// timer tick for delete error message
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="e">event arguments</param>
        private void dispatcherTimer_Tick(object sender, EventArgs e)
        {
            DeleteError = "";
            UserNameError = "";
            this.dispatcherTimer.Stop();
        }
        
        
        /// <summary>
        /// Add a new Server in the Network and refrehs the list
        /// </summary>
        /// <param name="server">Name of the Server</param>
        private void addServer(String server)
        {
            try
            {
                net.CreateSimulation(server);
                //refreshs the list
                net.ActiveSimulations();
                NewServer = "";
            }
            catch (Exception e)
            {
                DeleteError = e.Message;
            }
        }


        /// <summary>
        /// refresh the serverlist
        /// </summary>
        private void refreshServer()
        {
            try {
                net.ActiveSimulations();
            } catch (Exception e)
            {
                DeleteError = e.Message;
            }
        }

        

        /// <summary>
        /// deletes a server
        /// </summary>
        /// <param name="server">Name of the server</param>
        private void deleteServer(String server)
        {
            try {
                net.DeleteSimulation(server);
                net.ActiveSimulations();
                SelectedIndex = 0;
                if (ServerList.Count == 0) {
                    SelectedIndex = -1;
                }
            } catch (Exception e) {
                DeleteError = e.Message;
            }
        }

        

        /// <summary>
        /// joins a server 
        /// </summary>
        /// <param name="user">server to join</param>
        private void Join(String user)
        {
            try {
                //refresh the username in the network
                net.RefreshUsername(user);
                //refresh the selected server in the network
                net.RefreshSimulationName(SelectedServer);
                //join the server
                net.JoinSimulation();

                //changes the visible window to the mapview
                new MainWindow().Show();
                Application.Current.Windows[0].Close();
            } catch (Exception e) {
                DeleteError = e.Message;
            }
        }

        

        /// <summary>
        /// change the lobbyserver 
        /// </summary>
		/// <param name="lobbyName">name of the lobby</param>
        private void changeLobby(String lobbyName)
        {
            try {
                System.Diagnostics.Debug.WriteLine("Lobby changed hanged to" + lobbyName);
                net.Connect(lobbyName);
                net.ActiveSimulations();
                connected = true;
                SetLight(true);
            }
            catch (Exception e) {
                ServerList = new ObservableCollection<string>();
                connected = false;
                SetLight(false);

            }
        }

        /// <summary>
        /// on serverlist update
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="args">event arguments</param>
        private void OnServerListUpdate(object sender, EventArgs args)
        {
            ServerEventArgs se = (ServerEventArgs)args;
            ServerList = new ObservableCollection<string>(se.list);
        }

        
        /// <summary>
        /// closes the application
        /// </summary>
        private void exit()
        {
            Application.Current.Shutdown();
        }

        /// <summary>
        /// skip song
        /// </summary>
        /// <param name="t"></param>
		private void SkipSong(Object t)
		{
			soundcontroller.SkipMenuSound();
		}

        /// <summary>
        /// sets the "connect-light"
        /// </summary>
        /// <param name="state">state</param>
        private void SetLight(bool state) {
            if (state)
            {
                Color1 = colorGreen1;
                Color2 = colorGreen2;
            }
            else {
                Color1 = colorRed1;
                Color2 = colorRed2;
            }
        }
        #endregion
    }
}
