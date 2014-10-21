using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;

namespace UI.Communication.Interfaces
{
    /// <summary>
    /// Operate as a consumer and producer
    /// </summary>
    public interface IRequestor: IProducer, IConsumer
    {
        /// <summary>
        /// Close the network connections
        /// </summary>
        new void Close();
    }
}
