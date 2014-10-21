using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Exceptions
{
    /// <summary>
    /// Exception in case that no requestor instantiate in the network 
    /// </summary>
    public class NoRequestorExists: Exception
    {
        public NoRequestorExists(string msg) : base(msg) { }
    }
}
