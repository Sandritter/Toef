using Apache.NMS;
using System;
using System.Collections;
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
    /// Received a list of all active clients on a map
    /// </summary>
    public class ActiveClientsConsumer : BaseConsumer
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ActiveClientsConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action) :
            base(connectionData, converter, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._clientList_topic.ToString("g");
            Init();
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        protected override void OnMessage(IMessage mes)
        {

            ITextMessage tmsg = mes as ITextMessage;
            string list = tmsg.Properties.GetString(MessageProperties.CLIENTLIST.ToString("g"));
            command.Execute(new ClientEventArgs(list.Split(',')));
        }
    }
}
