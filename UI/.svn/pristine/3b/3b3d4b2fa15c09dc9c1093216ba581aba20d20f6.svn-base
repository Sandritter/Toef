using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;

namespace UI.Communication.Models
{
    /// <summary>
    /// Container contained all content that will be send to the server 
    /// </summary>
    public class ChatMessage: ISend
    {
        string _message;
        public ChatMessage(string msg)
        {
            Message = msg;
        }

        public string Message
        {
            get
            { return _message; }
            set { _message = value; }
        }
    }
}
