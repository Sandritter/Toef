using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Controller.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Interfaces;
using UI.Communication.NMS.Consumer;

namespace UI.Communication.Controller
{
    /// <summary>
    /// Generate and controll all consumer
    /// </summary>
    public class ConsumerController: IConsumerController
    {
        IClientInfo info;
        IConverter converter;
        List<IConsumer> consumer;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        public ConsumerController(IClientInfo info, IConverter converter)
        {
            this.info = info;
            this.converter = converter;
            consumer = new List<IConsumer>();
            Init();
        }

        /// <summary>
        /// Instantiate all consumer
        /// </summary>
        public void Init()
        {
            consumer.Add(new ViewTileConsumer(info, converter, NetworkAction.incomingTile));
            consumer.Add(new HostDownConsumer(info, converter, NetworkAction.incomingHostDown));
            consumer.Add(new ParticipantConsumer(info, converter, NetworkAction.incomingParticipants));
            consumer.Add(new ChatMessageConsumer(info, converter, NetworkAction.incomingChatMessage));
            consumer.Add(new ActiveClientsConsumer(info, converter, NetworkAction.incomingActiveClients));
            consumer.Add(new KeepAliveConsumer(info, converter, NetworkAction.incomingKeepAlive));
        }

        /// <summary>
        /// Set a command to a specific consumer
        /// </summary>
        /// <param name="command"></param>
        /// <param name="action">action that the consumer is responsible for</param>
        public void SetCommand(ICommand command, NetworkAction action)
        {
            foreach (IConsumer c in consumer)
            {
                if (action == ((BaseConsumer)c).Action)
                {
                    c.SetCommand(command);
                }
            }
        }

        /// <summary>
        /// Close connections of each consumer
        /// </summary>
        public void Close()
        {
            foreach (IConsumer c in consumer)
            {
                c.Close();
            }
        }

        /// <summary>
        /// Return a specific consumer
        /// </summary>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <returns></returns>
        public IConsumer GetConsumer(NetworkAction action)
        {
            foreach (IConsumer c in consumer)
            {
                if (action == ((BaseConsumer)c).Action)
                {
                    return c;
                }
            }
            return null;
        }
    }
}
