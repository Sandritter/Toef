using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;

namespace UI.Communication.Interfaces
{
    /// <summary>
    /// Send messages to the server
    /// </summary>
    public interface IProducer
    {
        /// <summary>
        /// Send a message
        /// </summary>
        /// <param name="action">action that the prodcer is responsible for</param>
        /// <param name="content">messge content</param>
        void Send(NetworkAction action, ISend content);

        /// <summary>
        /// Close all connections
        /// </summary>
        void Close();

        /// <summary>
        /// Returns the first successor of the chain of responsibility
        /// </summary>
        /// <param name="producer"></param>
        void SetSuccessor(IProducer producer);
    }
}
