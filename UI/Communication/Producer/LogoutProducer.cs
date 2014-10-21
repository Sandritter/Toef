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
    /// Producer sending to the server that the client wants to quit the game
    /// </summary>
    public class LogoutProducer : BaseProducer
    {
        ITextMessage txtMessage;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public LogoutProducer(IClientInfo connectionData, NetworkAction action) 
            :base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._logout_queue.ToString("g");
            Init();
        }

        /// <summary>
        /// Send method
        /// </summary>
        /// <param name="action"></param>
        /// <param name="content"></param>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                txtMessage = session.CreateTextMessage();
                txtMessage.Properties.SetString(MessageProperties.IPADDRESS.ToString("g"), connectionData.Ip);
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
