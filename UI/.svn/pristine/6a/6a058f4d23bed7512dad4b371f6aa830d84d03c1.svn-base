using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Interfaces;

namespace UI.Communication.Controller.Interfaces
{
    /// <summary>
    /// Interface: Received and send messages from and on the server
    /// </summary>
    public interface IRequestorController: IController
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="command"></param>
        /// <param name="action">action that the requestor is responsible for</param>
        void SetCommand(ICommand command, NetworkAction action);

        /// <summary>
        /// Return the first successor of the chain of responsibility
        /// </summary>
        /// <returns></returns>
        IRequestor FirstSuccessor();
    }
}
