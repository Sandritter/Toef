using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Models.Interfaces;

namespace UI.Communication.Models
{
    /// <summary>
    /// Contained the event handler which is called by incomming messages
    /// </summary>
    public class Receiver: IReceiver
    {
        public event EventHandler<EventArgs> Handler;

        public void Action(EventArgs e)
        {
            if (Handler != null)
            {
                Handler(this, e);
            }
            
        }
    }
}
