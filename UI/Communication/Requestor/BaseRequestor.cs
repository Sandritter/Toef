using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Interfaces;
using UI.LobbyFunctions;

namespace UI.Communication.Requestor
{
    /// <summary>
    /// Operates the consumer and producer functions
    /// </summary>
    public abstract class BaseRequestor: IRequestor
    {
        protected IProducer successor;
        protected ICommand command;
        
        protected SOAPFunctionsClient service; // SOAP service
        protected IClientInfo info;
        protected NetworkAction action;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="action">action that is responsible for</param>
        public BaseRequestor(NetworkAction action) 
        {
            this.action = action;
        }

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="service">soap service</param>
        public BaseRequestor(IClientInfo info, SOAPFunctionsClient service, NetworkAction action)
        {
            this.info = info;
            this.service = service;
            this.action = action;
        }

        /// <summary>
        /// Set successor 
        /// </summary>
        /// <param name="successor"></param>
        public void SetSuccessor(IProducer successor)
        {
            this.successor = successor;
        }

        /// <summary>
        /// Set command to specific requestor
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
        /// Send a request to th server and expected a answer
        /// </summary>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="content">messaging content</param>
        public abstract void Send(NetworkAction action, ISend content);

        /// <summary>
        /// Close all connections
        /// </summary>
        public abstract void Close();

    }
}
