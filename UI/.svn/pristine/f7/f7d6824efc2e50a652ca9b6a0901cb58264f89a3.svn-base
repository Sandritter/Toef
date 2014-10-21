using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Events;
using UI.Communication.Interfaces;

namespace UI.Communication.NMS.Consumer
{
    /// <summary>
    /// Received chat message from other clients
    /// </summary>
    public class ChatMessageConsumer : BaseConsumer
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ChatMessageConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action)
            : base(connectionData, converter, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._chatMessage_topic.ToString("g");
            Init();
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        protected override void OnMessage(IMessage mes)
        {

            ITextMessage tmsg = mes as ITextMessage;
            string text = tmsg.Text;
            string name = tmsg.Properties.GetString(MessageProperties.USERNAME.ToString("g"));
            command.Execute(new ChatEventArgs(name, text));
        }
    }
}
