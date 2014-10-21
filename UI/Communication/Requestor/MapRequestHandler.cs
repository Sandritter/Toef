using System;
using System.Collections.Generic;
using Apache.NMS;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.NMS;
using UI.Communication.Enums;
using System.Collections.ObjectModel;
using UI.Model;
using UI.Communication.Models;
using UI.Communication.Interfaces;
using UI.Communication.Events;
using UI.Communication.NMS.Consumer;
using UI.Communication.Helper;
using UI.Communication.Requestor;
using UI.Communication.Exceptions;

namespace UI.Communication.NMS
{
    /// <summary>
    /// Recieves messages from server concerning the map
    /// </summary>
    public class MapRequestHandler: BaseRequestor
    {
        NMSConnectionFactory connectionFactory;
        IConnection connection;
        ISession session;
        IDestination destination;
        IMessageConsumer responseConsumer;
        string queueTopic;
        IMessageProducer producer;
        ITextMessage message;
        IConverter converter;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public MapRequestHandler(IClientInfo connectionData, IConverter converter, NetworkAction action)
            :base(action)
        {
            this.converter = converter;
            this.info = connectionData;
        }

        public override void Send(NetworkAction action, ISend content) 
        {
            if (action == this.action)
            {
                this.queueTopic = info.SimulationName + MessageTopics._map_queue.ToString("g");
                Init();
                IDestination tmpDestination = session.CreateTemporaryQueue();
                responseConsumer = session.CreateConsumer(tmpDestination);
                responseConsumer.Listener += new MessageListener(OnMessage);//Add Callback method if message is recieved
                message = session.CreateTextMessage();
                message.NMSReplyTo = tmpDestination; //Temporary message destination for callback message from server
                message.NMSCorrelationID = Utils.CreateRandomString(); //CorrelationID for callback message from server
                try
                {
                    producer.Send(message);
                }
                catch (EndpointNotFoundException e)
                {
                    Console.WriteLine("NMS EndpointNotFoundException: " + e.Message);
                    throw new EndpointNotFoundException("Lobby Server '" + info.ServerAddress + "' nicht erreichbar.");
                }            }
            else if (successor != null)
            {
                successor.Send(action, content);
            }
        }

        /// <summary>
        /// Closes Connection to Broker
        /// </summary>
        public override void Close()
        {
            producer.Close();
            session.Close();
            connection.Close();

            producer = null;
            session = null;
            connection = null;
        }

        /// <summary>
        /// Initialises Components needed for Communication
       /// </summary>
        private void Init()
        {
            connectionFactory = new NMSConnectionFactory(info.Brokerurl);
            connection = connectionFactory.CreateConnection();
            connection.Start();
            session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            destination = session.GetQueue(queueTopic);

            producer = session.CreateProducer(destination);
            producer.DeliveryMode = MsgDeliveryMode.NonPersistent;
        }

        /// <summary>
        /// Creates random identification string for callback message
        /// </summary>
        /// <returns></returns>
        private string CreateRandomString()
        {
            Random random = new Random();
            long tmp = random.Next(DateTime.Now.Millisecond);
            return tmp.ToString("X4");
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        private void OnMessage(IMessage mes)
        {
            ITextMessage tmsg = mes as ITextMessage;
            string text = tmsg.Text;
            ViewTile[][] view = converter.StringToViewTileArray(text);
            Tile[,] tiles = converter.ViewTileArrayToTileArray(view);
            command.Execute(new MapEventArgs(tiles));
        }
    }
}
