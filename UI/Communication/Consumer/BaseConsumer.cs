using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Interfaces;

namespace UI.Communication.NMS.Consumer
{
    /// <summary>
    /// Abstract Consumer for receiving messages from the server
    /// </summary>
    public abstract class BaseConsumer: IConsumer
    {
        protected ICommand command;
        protected NetworkAction action;

        protected NMSConnectionFactory connectionFactory;
        protected IConnection connection;
        protected ISession session;
        protected IDestination destination;
        protected IMessageConsumer consumer;
        protected IConverter converter;
        protected string queueTopic;
        protected IClientInfo info;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="connectionData">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="action">action that the consumer is responsible for</param>
        public BaseConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action)
        {
            this.info = connectionData;
            this.converter = converter;
            this.action = action;
        }

        /// <summary>
        /// set the command, which will be executed after the message received 
        /// </summary>
        /// <param name="command"></param>
        public void SetCommand(ICommand command)
        {
            this.command = command;
        }

        public NetworkAction Action
        {
            get { return action; }
            set { action = value; }
        }

        /// <summary>
        /// Closes Connection to Broker
        /// </summary>
        public void Close()
        {
            consumer.Close();
            session.Close();
            connection.Close();

            consumer = null;
            session = null;
            connection = null;
        }

        /// <summary>
        /// Initialises Components needed for Communication
        /// </summary>
        protected void Init()
        {
            connectionFactory = new NMSConnectionFactory(info.Brokerurl);
            connection = connectionFactory.CreateConnection();
            connection.Start();
            session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
            destination = session.GetTopic(queueTopic);
            consumer = session.CreateConsumer(destination);
            consumer.Listener += new MessageListener(OnMessage); //Add Callbackmethod
        }

        /// <summary>
        /// Callbackmethod for incoming message
        /// </summary>
        /// <param name="mes"></param>
        protected abstract void OnMessage(IMessage mes);
    }
}
