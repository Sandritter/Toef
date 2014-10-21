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
    /// Producer sending information about a placed participant
    /// </summary>
    public class ParticipantProducer: BaseProducer
    {
        ITextMessage txtMessage;
        IConverter converter;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public ParticipantProducer(IClientInfo connectionData, IConverter converter, NetworkAction action)
            : base(connectionData, action)
        {
            this.queueTopic = connectionData.SimulationName + MessageTopics._placeViewCar_queue.ToString("g");
            this.converter = converter;
            Init();
        }

        /// <summary>
        /// Sends a Message containing participant information to server
        /// </summary>
        /// <param name="content"></param>
        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                txtMessage = session.CreateTextMessage();
                NewParticipant vc = (NewParticipant)content;
                string text = converter.NewParticipantToString(vc);
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
