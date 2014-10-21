using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Exceptions;
using UI.Communication.Interfaces;
using UI.Communication.Models;

namespace UI.Communication.NMS.Producer
{
    /// <summary>
    /// Producer for sending client position informations
    /// </summary>
    public class ClientPositionProducer : BaseProducer
    {
        IConverter converter;
        ITextMessage txtMessage;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="connectionData">converts stuff</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ClientPositionProducer(IClientInfo connectionData, IConverter converter, NetworkAction action)
            : base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._position_queue.ToString("g");
            this.converter = converter;
            Init();
        }

        /// <summary>
        /// Sends a Message containing current point of view of client
        /// </summary>
        /// <param name="content"></param>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                ClientPosition cp = (ClientPosition)content;
                txtMessage = session.CreateTextMessage();
                txtMessage.Properties.SetString(MessageProperties.IPADDRESS.ToString("g"), connectionData.Ip);
                string text = converter.ClientPositionToString(cp);
                txtMessage.Text = text;
                try
                {
                    producer.Send(txtMessage);
                }
                catch (Exception e)
                {
                    Console.WriteLine("NMS EndpointNotFoundException: " + e.Message);
                    throw new EndpointNotFoundException("Lobby Server '" + connectionData.ServerAddress + "' nicht erreichbar.");
                }
            }
            else if (successor != null)
            {
                successor.Send(action, content);
            }
        }
    }
}
