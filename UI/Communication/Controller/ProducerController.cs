using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Controller.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Exceptions;
using UI.Communication.Interfaces;
using UI.Communication.NMS.Producer;

namespace UI.Communication.Controller
{
    /// <summary>
    /// Generate and controll all producer
    /// </summary>
    public class ProducerController: IProducerController
    {
        IClientInfo connectionInfos;
        IConverter converter;
        List<IProducer> producer;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        public ProducerController(IClientInfo infos, IConverter converter)
        {
            this.connectionInfos = infos;
            this.converter = converter;
            producer = new List<IProducer>();
            Init();
        }

        /// <summary>
        /// Instantiate all Producers
        /// </summary>
        public void Init() {
            producer.Add(new TileProducer(connectionInfos, converter, NetworkAction.placeTile));
            producer.Add(new ClientPositionProducer(connectionInfos, converter, NetworkAction.updatePosition));
            producer.Add(new LogoutProducer(connectionInfos, NetworkAction.logout));
            producer.Add(new ParticipantProducer(connectionInfos, converter, NetworkAction.placeParticipant));
            producer.Add(new DeleteParticipantProducer(connectionInfos, NetworkAction.deleteParticipant));
            producer.Add(new ChatMessageProducer(connectionInfos, NetworkAction.sendChatMessage));
            producer.Add(new KeepAliveProducer(connectionInfos, NetworkAction.sendKeepAlive));
            producer.Add(new RefuelProducer(connectionInfos, NetworkAction.refuelParticipant));

            // Set up Chain of Responsibility
            int lastIndex = producer.Count-1;
            for (int i = 0; i <= lastIndex; i += 1)
            {
                if (i != lastIndex)
                {
                    producer[i].SetSuccessor(producer[i + 1]);
                }
                
            }
        }

        /// <summary>
        /// Return the first successor of the chain of responsibility
        /// </summary>
        /// <returns></returns>
        public IProducer FirstSuccessor()
        {
            if (producer.Count > 0) {
                return producer[0];
            }
            throw new NoProducerExists("Es wurde kein Producer instanziiert.");
            
        }

        /// <summary>
        /// Close connections of each producer
        /// </summary>
        public void Close()
        {
            foreach (IProducer p in producer)
            {
                p.Close();
            }
        }
    }
}
