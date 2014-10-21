using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Ioc;
using Microsoft.Practices.ServiceLocation;
using UI;
using UI.Communication;
using UI.Controller;
using UI.Controller.Interface;

namespace UI.ViewModel
{
    /// <summary>
    /// This class contains static references to all the view models in the
    /// application and provides an entry point for the bindings.
    /// </summary>
    public class ViewModelLocator
    {
        private MapViewModel _map;
        private ZoomViewModel _zoom;
        private ServerViewModel _server;
        private ChatViewModel _chat;
        private MenuViewModel _menu;

        private ISoundController soundcontroller = new SoundController();
        private Network network = new Network();

        /// <summary>
        /// Initializes a new instance of the ViewModelLocator class.
        /// </summary>
        public ViewModelLocator()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);
            SimpleIoc.Default.Register<MapViewModel>();
        }

        /// <summary>
        /// returns the mapviewmodel
        /// </summary>
        public MapViewModel Map
        {
            get {
                if (_map == null) {
                    _map = new MapViewModel(Menu, network, soundcontroller, Zoom);
                }
                return _map;
            }
        }

        /// <summary>
        /// return the zoomviewmodel
        /// </summary>
        public ZoomViewModel Zoom
        {
            get {
                if (_zoom == null) {
                    _zoom = new ZoomViewModel(network);
                }
                return _zoom;
            }
        }

        /// <summary>
        /// return the serverviewmodel
        /// </summary>
        public ServerViewModel Server {
            get {
                if (_server == null) {
                    _server = new ServerViewModel(network, soundcontroller);
                }
                return _server;
            }
        }

        /// <summary>
        /// returns the chatviewmodel
        /// </summary>
        public ChatViewModel Chat {
            get {
                if (_chat == null) {
                    _chat = new ChatViewModel(network,soundcontroller, Menu);
                }
                return _chat;
            }
        }

        /// <summary>
        /// returns the menuviewmodel
        /// </summary>
        public MenuViewModel Menu {
            get {
                if (_menu == null) {
                    _menu = new MenuViewModel(network, Zoom, soundcontroller);
                }
                return _menu;
            }
        }

        public static void Cleanup()
        {

        }

    }
}