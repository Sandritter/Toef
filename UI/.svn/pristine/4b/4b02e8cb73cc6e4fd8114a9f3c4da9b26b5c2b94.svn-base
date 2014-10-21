using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Interfaces;

namespace UI.Communication.NMS.Producer
{
    /// <summary>
    /// Abstract class for sending messages to the server
    /// </summary>
    public abstract class BaseProducer: IProducer
    {
        protected IProducer successor;
        protected NetworkAction action;

        protected NMSConnectionFactory connectionFactory;
        protected IConnection connection;
        protected ISession session;
        protected IDestination destination;
        protected string queueTopic;
        protected IMessageProducer producer;
        protected IClientInfo connectionData;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public BaseProducer(IClientInfo connectionData, NetworkAction action)
        {
            this.connectionData = connectionData;
            this.action = action;
        }

        /// <summary>
        /// Returns the first successor of the chain of responsibility
        /// </summary>
        /// <param name="successor"></param>
        public void SetSuccessor(IProducer successor)
        {
            this.successor = successor;
        }

        /*
         * Send message to server with given content 
         */
        public abstract void Send(NetworkAction action, ISend content);

        /// <summary>
        /// Closes Connection to Broker
        /// </summary>
        public void Close()
        {
            producer.Close();
            session.Close();
            connection.Close();

            producer = null;
            session = null;
            connection = null;
        }

        /// <summary>
        /// Initialises Components needed for Communication
        /// </summary>
        protected void Init()
        {
            connectionFactory = new NMSConnectionFactory(connectionData.Brokerurl);
            connection = connectionFactory.CreateConnection();
            connection.Start();
            session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            destination = session.GetQueue(queueTopic);

            producer = session.CreateProducer(destination);
            producer.DeliveryMode = MsgDeliveryMode.NonPersistent;
        }
    }
}
