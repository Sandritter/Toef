using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Models;
using UI.Communication.Models.Interfaces;

namespace UI.Communication.Commands
{   
    /// <summary>
    /// Concrete Command, that encapsulate a method call as the object containing all necessary information
    /// </summary>
    public class NetworkCommand: Command
    {
        public NetworkCommand(IReceiver re)
            : base(re)
        { }

        public override void Execute(EventArgs e)
        {
            receiver.Action(e);
        }
    }
}
