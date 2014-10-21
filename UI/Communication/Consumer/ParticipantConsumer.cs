using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Events;
using UI.Communication.Interfaces;
using UI.Communication.Models;
using UI.Model;

namespace UI.Communication.NMS.Consumer
{
    /// <summary>
    /// Received a list of participants from the server
    /// </summary>
    public class ParticipantConsumer : BaseConsumer
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ParticipantConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action)
            : base(connectionData, converter, action)
        {
            this.queueTopic = connectionData.Ip + "_" + connectionData.SimulationName + MessageTopics._viewCarUpdate_queue.ToString("g");
            Init();
        }

        /// <summary>
        /// Override init method from BaseConsumer, because the destination must be a topic 
        /// </summary>
        protected new void Init()
        {
            connectionFactory = new NMSConnectionFactory(info.Brokerurl);
            connection = connectionFactory.CreateConnection();
            connection.Start();
            session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            destination = session.GetQueue(queueTopic);
            consumer = session.CreateConsumer(destination);
            consumer.Listener += new MessageListener(OnMessage); //Add Callbackmethod
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        protected override void OnMessage(IMessage mes)
        {

            ITextMessage tmsg = mes as ITextMessage;
            string text = tmsg.Text;
            UpdateParticipant[] list = converter.StringToUpdateParticipantArray(text);
            Dictionary<int, Participant> p = converter.DictToParticipantArray(list);
            command.Execute(new ParticipantEventArgs(p));
        }
    }
}
