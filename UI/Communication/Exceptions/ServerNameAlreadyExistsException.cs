using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Exceptions
{
    /// <summary>
    /// Exception in case that a client try to create a server, which name is already taken
    /// </summary>
    class ServerNameAlreadyExistsException: Exception
    {
        public ServerNameAlreadyExistsException(string msg) : base(msg) { }
    }
}
