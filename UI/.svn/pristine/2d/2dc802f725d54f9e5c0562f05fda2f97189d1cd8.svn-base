using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Models;
using UI.Communication.Models.Interfaces;

namespace UI.Communication.Commands
{
    /// <summary>
    /// Abstract Command, that encapsulate a method call as the object containing all necessary information
    /// </summary>
    public abstract class Command: ICommand
    {
        protected IReceiver receiver;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="re">Receiver object owns a method, that is called by the command's execute method</param>
        public Command(IReceiver re)
        {
            this.receiver = re;
        }

        /// <summary>
        /// Abstract method, that execute the command
        /// </summary>
        /// <param name="e"></param>
        public abstract void Execute(EventArgs e);
    }
}
