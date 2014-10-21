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
    /// Producer sending information about a participant that schould be refilled
    /// </summary>
    public class RefuelProducer : BaseProducer
    {
        ITextMessage txtMessage;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public RefuelProducer(IClientInfo connectionData, NetworkAction action)
            : base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._carFuelInfo_queue.ToString("g");
            Init();
        }

        /// <summary>
        /// Sends a Message containing information wether the client
        /// wants to refuel a participant
        /// </summary>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                UpdateParticipant w = (UpdateParticipant)content;
                txtMessage = session.CreateTextMessage();
                txtMessage.Properties.SetInt(MessageProperties.PARTICIPANTID.ToString("g"), w.id);
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
