using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Interfaces;

namespace UI.Communication.NMS.Consumer
{
    /// <summary>
    /// Server asked, if the client is still on
    /// </summary>
    public class KeepAliveConsumer: BaseConsumer
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public KeepAliveConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action) :
            base(connectionData, converter, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._keepAlive_topic.ToString("g");
            Init();
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        protected override void OnMessage(IMessage mes)
        {
            command.Execute(new EventArgs());
        }
    }
}
