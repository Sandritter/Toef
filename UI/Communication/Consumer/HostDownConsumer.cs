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
    /// Received a signal, when the host deletes his map 
    /// </summary>
    public class HostDownConsumer : BaseConsumer
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public HostDownConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action) :
            base(connectionData, converter, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._hostDown_topic.ToString("g");
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
