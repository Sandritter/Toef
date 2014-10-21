using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Events
{
    /// <summary>
    /// EventArgs for a incomming message
    /// </summary>
    public class ChatEventArgs: System.EventArgs
    {
        public readonly string username;
        public readonly string message;

        public ChatEventArgs(string username, string message)
        {
            this.username = username;
            this.message = message;
        }
    }
}
