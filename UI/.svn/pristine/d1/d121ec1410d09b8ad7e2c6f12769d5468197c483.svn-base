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
using UI.Communication.Exceptions;
using UI.Controller;
using UI.Controller.Interface;
using UI.Enums;

namespace UI.ViewModel
{
    /// <summary>
    /// Manages all information needed to display chat messages and send messages to the server
    /// </summary>
    public class ChatViewModel : ObservableModelBase
    {
        private ISoundController soundController;
        private MenuViewModel _menu; //_menu instance is used to show exception window
        private Network net;

        private ObservableCollection<string> _players;

        private string _chatlog = "Willkommen auf dem Server.\nViel Spass beim Bauen :)\n\n";
        private string _currentChatText = "";
        
        int _verticalScrollOffset;

        public ChatViewModel(Network network,ISoundController soundcontroller, MenuViewModel menu) {
            
            _players = new ObservableCollection<string>();      //list of currently playing users
            soundController = soundcontroller;

            net = network;
            net.ActiveClientReceiver.Handler += onClientList;   // register to network for clientlist
            net.ChatReceiver.Handler += onChatMessage;          // register to network for chatmessage receival
            net.ActiveClients();
            _menu = menu;
        }

        #region Properties

        /// <summary>
        /// list of clients that are connected in current server
        /// </summary>
        public ObservableCollection<string> Players {
            get { return _players; }
            set {
                _players = value;
                OnPropertyChanged("Players");
            }
        }

        /// <summary>
        /// vertical offset of chatwindow
        /// </summary>
        public int VerticalScrollOffset {
            get { return _verticalScrollOffset; }
            set {
                _verticalScrollOffset = value;
                OnPropertyChanged("VerticalScrollOffset");
            }
        }

        /// <summary>
        /// chat history
        /// </summary>
        public string Chatlog {
            get { return _chatlog; }
            set {
                _chatlog = value;
                OnPropertyChanged("Chatlog");
            }
        }

        /// <summary>
        /// text in chattextbox
        /// </summary>
        public string CurrentChatText {
            get { return _currentChatText; }
            set {
                _currentChatText = value;
                OnPropertyChanged("CurrentChatText");
                OnPropertyChanged("LastChatIndex");
            }
        }

        #endregion


        #region Commands

        /// <summary>
        /// ActionCommand for sending a chatmessage to other clients
        /// </summary>
        private ICommand _enterChatCommand;
        public ICommand EnterChatCommand
        {
            get
            {
                return _enterChatCommand ?? (_enterChatCommand = new ActionCommand(dummy => sendChatMessage(), dummy => true));
            }
        }

        #endregion


        #region Methods

        /// <summary>
        /// callback function to get clients on current map
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onClientList(object sender, EventArgs e) {
            //Delete listbox
            while (this._players.Count > 0) {
                Application.Current.Dispatcher.Invoke(new Action(() =>
                     this._players.RemoveAt(0)
                ));
            }
            //fill with current clients
            foreach (String client in ((ClientEventArgs)e).usernames) {
                Application.Current.Dispatcher.Invoke(new Action(() =>
                     this._players.Add(client)
                ));

            }
            OnPropertyChanged("Players");
        }

        /// <summary>
        /// callback function to receive chat messages from clients
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void onChatMessage(object sender, EventArgs e) {
            ChatEventArgs ce = (ChatEventArgs)e;
            Application.Current.Dispatcher.Invoke(new Action(() =>
                Chatlog += ("__________________________________\n" + ce.username + " :\n" + ce.message + "\n")
            ));
            soundController.PlayEffectSound(SoundEffect.GetChatmessage);
            VerticalScrollOffset += 1000;
            OnPropertyChanged("Chatlog");
        }

        /// <summary>
        /// sends chat message to other clients on current map
        /// </summary>
        private void sendChatMessage() {
            try {
                net.SendChatMessage(this._currentChatText);
            } catch (EndpointNotFoundException e) {
                    this._menu.ExceptionVisibility = true;
            }
            CurrentChatText = "";
        }

        #endregion
    }
}
