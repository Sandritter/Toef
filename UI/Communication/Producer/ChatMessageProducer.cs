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
    /// Producer for sending a chat message 
    /// </summary>
    public class ChatMessageProducer: BaseProducer
    {
        ITextMessage txtMessage;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ChatMessageProducer(IClientInfo connectionData, NetworkAction action)
            : base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._chatMessage_queue.ToString("g");
            Init();
        }

        /// <summary>
        /// Sends a Message containing chat information to server
        /// </summary>
        /// <param name="content"></param>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                txtMessage = session.CreateTextMessage();
                ChatMessage w = (ChatMessage)content;
                string text = w.Message;
                txtMessage.Text = text;
                txtMessage.Properties.SetString(MessageProperties.USERNAME.ToString("g"), connectionData.Username);
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
