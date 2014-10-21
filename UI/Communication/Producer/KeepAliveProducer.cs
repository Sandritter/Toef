using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Exceptions;
using UI.Communication.Interfaces;

namespace UI.Communication.NMS.Producer
{
    /// <summary>
    /// Producer sends a signal to the server that the client is still alive
    /// </summary>
    public class KeepAliveProducer: BaseProducer
    {
        ITextMessage txtMessage;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public KeepAliveProducer(IClientInfo connectionData, NetworkAction action) 
            :base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._keepAlive_queue.ToString("g");
            Init();
        }

        /// <summary>
        /// Sending keep alive signal to server
        /// </summary>
        /// <param name="action"></param>
        /// <param name="content"></param>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                txtMessage = session.CreateTextMessage();
                txtMessage.Properties.SetString(MessageProperties.IPADDRESS.ToString(), connectionData.Ip);
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
