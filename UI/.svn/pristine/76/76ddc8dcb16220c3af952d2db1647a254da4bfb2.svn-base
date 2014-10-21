using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;

namespace UI.Communication.Interfaces
{
    /// <summary>
    /// Received messages from the server
    /// </summary>
    public interface IConsumer
    {
        /// <summary>
        /// set the command, which will be executed after the message received 
        /// </summary>
        /// <param name="command"></param>
        void SetCommand(ICommand command);

        /// <summary>
        /// Close the connection
        /// </summary>
        void Close();
    }
}
