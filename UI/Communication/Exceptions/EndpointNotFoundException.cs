using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Exceptions
{
    /// <summary>
    /// Exception in case that the endpoint not found. 
    /// </summary>
    public class EndpointNotFoundException: Exception
    {
        public EndpointNotFoundException(string msg) : base(msg) { }
    }
}
