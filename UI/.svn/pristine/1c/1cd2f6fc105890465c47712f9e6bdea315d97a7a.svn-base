using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Models.Interfaces
{
    /// <summary>
    /// Contained the action which will be called if a message arrived from the server
    /// </summary>
    public interface IReceiver
    {
        event EventHandler<EventArgs> Handler;
        void Action(EventArgs e);
    }
}
