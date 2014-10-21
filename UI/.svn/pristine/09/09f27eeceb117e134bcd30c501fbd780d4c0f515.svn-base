using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Enums;

namespace UI.Communication.Controller.Interfaces
{
    /// <summary>
    /// Interface: Received messages from the server
    /// </summary>
    public interface IConsumerController: IController
    {
        /// <summary>
        /// Set a command to a specific consumer
        /// </summary>
        /// <param name="command"></param>
        /// <param name="action">action that the consumer is responsible for</param>
        void SetCommand(ICommand command, NetworkAction action);
    }
}
