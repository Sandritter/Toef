using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;

namespace UI.Communication.Controller.Interfaces
{
    /// <summary>
    /// Interface: Generate and send messages on the server
    /// </summary>
    public interface IProducerController: IController
    {
        /// <summary>
        /// Return first successor of the chain of responsibility
        /// </summary>
        /// <returns></returns>
        IProducer FirstSuccessor();
    }
}
