using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.NMS;
using UI.Communication.Enums;
using UI.Model;
using UI.Communication.Interfaces;
using UI.Communication.Converter;
using UI.Communication.Events;
using UI.Communication.NMS.Producer;
using UI.Communication.NMS.Consumer;
using UI.Communication.Helper;
using UI.Communication.Models;
using UI.Communication.Exceptions;
using UI.LobbyFunctions;
using System.ServiceModel.Channels;
using System.ServiceModel;
using UI.Communication.SOAP;
using UI.Controller;
using UI.Repositories;
using UI.Communication.Controller;
using UI.Communication.Controller.Interfaces;
using UI.Communication.Models.Interfaces;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Commands;
namespace UI.Communication
{
    /// <summary>
    /// General Network Class for Communication Purposes
    /// Provides all functions concerning the communication between client and server
    /// </summary>
    public class Network
    {
        # region Variables

        /// <summary>
        /// Receiver
        /// </summary>
        public IReceiver ActiveClientReceiver;
        public IReceiver ActiveSimulationReceiver;
        public IReceiver MapReceiver;
        public IReceiver ChatReceiver;
        public IReceiver UpdateTileReceiver;
        public IReceiver HostDownReceiver;
        public IReceiver UpdateParticipantReceiver;
        public IReceiver KeepAliveReceiver;

        /// <summary>
        /// Controller
        /// </summary>
        IRequestorController requestorController;
        IProducerController producerController;
        IConsumerController consumerController;

        /// <summary>
        /// SOAP variables
        /// </summary>
        SOAPFunctionsClient soapService;
        Binding hhtpBinding;

        /// <summary>
        /// other variables
        /// </summary>
        IClientInfo connectionData; //Contains client-information for server
        IConverter converter;

        # endregion 

        /// <summary>
        /// constructor
        /// </summary>
        public Network()
        {
            converter = new NetworkConverter();
            CreateReceiver();
            
        }

        # region Public methods

        /// <summary>
        /// Starts a connection to the given lobby server.
        /// </summary>
        /// <param name="serverAddress">LobbyServer to connect with</param>
        public void Connect(string serverAddress)
        {
            connectionData = new ConnectionData(serverAddress);
            hhtpBinding = new BasicHttpBinding();
            soapService = new SOAPFunctionsClient(hhtpBinding, new EndpointAddress(connectionData.SOAPurl));
            requestorController = new RequestorController(connectionData, converter, soapService);
            CreateRequestorCommands();
        }

        /// <summary>
        /// Refreshes the simulationname with given name (for UI purposes)
        /// </summary>
        /// <param name="name">name of simulation</param>
        public void RefreshSimulationName(string name)
        {
            connectionData.SimulationName = name;
        }

        /// <summary>
        /// Refreshes the username with given name (for UI purposes)
        /// </summary>
        /// <param name="name">username</param>
        public void RefreshUsername(string name)
        {
            connectionData.Username = name;
        }

        /// <summary>
        /// Creates a simulation with given name
        /// </summary>
        /// <param name="name">simulation name</param>
        public void CreateSimulation(string name)
        {
            this.RefreshSimulationName(name);
            requestorController.FirstSuccessor().Send(NetworkAction.createSimulation, null);
        }

        /// <summary>
        /// Deletes a simulation with given name 
        /// </summary>
        /// <param name="name">simulation name</param>
        public void DeleteSimulation(string name)
        {
            this.RefreshSimulationName(name);
            requestorController.FirstSuccessor().Send(NetworkAction.deleteSimulation, null);
        }

        /// <summary>
        /// Request lists of active simulations as strings
        /// </summary>
        /// <returns>List of simulations</returns>
        public void ActiveSimulations()
        {
            requestorController.FirstSuccessor().Send(NetworkAction.getActiveSimulations, null);
        }

        /// <summary>
        /// Request lists of active clients as strings
        /// </summary>
        /// <returns>List of clientnames</returns>
        public void ActiveClients()
        {
            requestorController.FirstSuccessor().Send(NetworkAction.getActiveClients, null);
        }

        /// <summary>
        /// Starts Communicationprocess if client wants to join a simulation
        /// </summary>
        public void JoinSimulation()
        {
            try
            {
                producerController = new ProducerController(connectionData, converter);
                consumerController = new ConsumerController(connectionData, converter);
                CreateConsumerCommands();
                ChainObserver();
            }
            catch (Exception e)
            {
                Console.WriteLine("JMS EndpointNotFoundException: " + e.Message);
                throw new UI.Communication.Exceptions.EndpointNotFoundException("Lobby Server '" + connectionData.ServerAddress + "' nicht erreichbar.");
            }
            requestorController.FirstSuccessor().Send(NetworkAction.login, null);
        }

        /// <summary>
        /// Request map from server
        /// </summary>
        public void RequestMap()
        {
            requestorController.FirstSuccessor().Send(NetworkAction.getMap, null);
        }

        /// <summary>
        /// Send Update information about a tile on the server
        /// </summary>
        /// <param name="tile"></param>
        public void UpdateTile(Tile tile)
        {
            ViewTile vt = converter.TileToViewTile(tile);
            producerController.FirstSuccessor().Send(NetworkAction.placeTile, vt);
        }

        /// <summary>
        /// Send the act client position on the server
        /// </summary>
        /// <param name="x1"></param>
        /// <param name="y1"></param>
        /// <param name="x2"></param>
        /// <param name="y2"></param>
        public void sendClientPosition(int x1, int y1, int x2, int y2)
        {
            ClientPosition cp = new ClientPosition(x1,y1,x2,y2);
            producerController.FirstSuccessor().Send(NetworkAction.updatePosition, cp);
        }

        /// <summary>
        /// Logouts the client from a simulation
        /// </summary>
        public void Logout()
        {
            producerController.FirstSuccessor().Send(NetworkAction.logout, null);
            producerController.Close();
            requestorController.Close();
        }

        /// <summary>
        /// Send a participant to server
        /// </summary>
        /// <param name="car"></param>
        public void PlaceParticipant(Participant participant)
        {
            NewParticipant vp = new NewParticipant(participant.Type, participant.TilePosX, participant.TilePosY, participant.Rot);
            producerController.FirstSuccessor().Send(NetworkAction.placeParticipant, vp);
        }

        /// <summary>
        /// Delete a participant on a server
        /// </summary>
        /// <param name="participant"></param>
        public void DeleteParticipant(Participant participant)
        {
            UpdateParticipant up = new UpdateParticipant(participant.Type, participant.ID, participant.PosX, participant.PosY, participant.Rot, participant.OptInfos);
            producerController.FirstSuccessor().Send(NetworkAction.deleteParticipant, up);
        }

        /// <summary>
        /// Send chat message to server
        /// </summary>
        /// <param name="text"></param>
        public void SendChatMessage(string text)
        {
            ChatMessage w = new ChatMessage(text);
            producerController.FirstSuccessor().Send(NetworkAction.sendChatMessage, w);
        }

        /// <summary>
        /// Fill up a participant on server
        /// </summary>
        /// <param name="participant"></param>
        public void FillUpParticipant(Participant participant)
        {
            UpdateParticipant up = new UpdateParticipant(participant.Type, participant.ID, participant.PosX, participant.PosY, participant.Rot, participant.OptInfos);
            producerController.FirstSuccessor().Send(NetworkAction.refuelParticipant, up);
        }

        # endregion 

        # region private methods

        private void CreateReceiver()
        {
            ActiveClientReceiver = new Receiver();
            ActiveSimulationReceiver = new Receiver();
            MapReceiver = new Receiver();
            ChatReceiver = new Receiver();
            UpdateTileReceiver = new Receiver();
            HostDownReceiver = new Receiver();
            UpdateParticipantReceiver = new Receiver();
            KeepAliveReceiver = new Receiver();
        }

        private void CreateConsumerCommands()
        {
            consumerController.SetCommand(new NetworkCommand(ActiveClientReceiver), NetworkAction.incomingActiveClients);
            consumerController.SetCommand(new NetworkCommand(ChatReceiver), NetworkAction.incomingChatMessage);
            consumerController.SetCommand(new NetworkCommand(UpdateParticipantReceiver), NetworkAction.incomingParticipants);
            consumerController.SetCommand(new NetworkCommand(HostDownReceiver), NetworkAction.incomingHostDown);
            consumerController.SetCommand(new NetworkCommand(KeepAliveReceiver), NetworkAction.incomingKeepAlive);
            consumerController.SetCommand(new NetworkCommand(UpdateTileReceiver), NetworkAction.incomingTile);
        }

        private void CreateRequestorCommands()
        {
            requestorController.SetCommand(new NetworkCommand(MapReceiver), NetworkAction.getMap);
            requestorController.SetCommand(new NetworkCommand(ActiveSimulationReceiver), NetworkAction.getActiveSimulations);
            requestorController.SetCommand(new NetworkCommand(ActiveClientReceiver), NetworkAction.getActiveClients);
        }

        private void ChainObserver()
        {
            MapReceiver.Handler += ((IBuffer)((ConsumerController)consumerController).GetConsumer(NetworkAction.incomingTile)).ProcessBuffer;
            KeepAliveReceiver.Handler += HandleKeepAliveEvent;
        }

        private void HandleKeepAliveEvent(object sender, EventArgs args)
        {
            producerController.FirstSuccessor().Send(NetworkAction.sendKeepAlive, null);
        }

        #endregion
    }
}
