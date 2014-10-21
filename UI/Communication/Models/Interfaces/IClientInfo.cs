using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Interfaces
{
    /// <summary>
    /// Bundle of necessary information of client and connection
    /// </summary>
    public interface IClientInfo
    {
        // Property declaration
        string ServerAddress
        {
            get;
            set;
        }

        string SOAPurl
        {
            get;
            set;
        }

        string Brokerurl
        {
            get;
            set;
        }

        string Ip
        {
            get;
            set;
        }

        string SimulationName
        {
            get;
            set;
        }

        string Username
        {
            get;
            set;
        }
    }
}
