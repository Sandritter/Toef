using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Exceptions;
using UI.Communication.Helper;
using UI.Communication.Interfaces;
using UI.Communication.Requestor;
using UI.LobbyFunctions;

namespace UI.Communication.SOAP
{
    /// <summary>
    /// Send a request to register a client
    /// </summary>
    public class RegisterClientService: BaseRequestor
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="service">soap service</param>
        public RegisterClientService(IClientInfo info, SOAPFunctionsClient service, NetworkAction action)
            :base(info, service, action)
        {   }

        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                string state = null;
                try
                {
                    state = service.registerClient(info.SimulationName, info.Ip, info.Username);
                }
                catch (Exception e)
                {
                    Console.WriteLine("SOAP EndpointNotFoundException: " + e.Message);
                    throw new EndpointNotFoundException("Lobby Server '" + info.ServerAddress + "' nicht erreichbar.");
                }

                if (EnumUtils.ToDescription(NetworkSignal.ACTION_SUCCESSFUL).CompareTo(state) != 0)
                {
                    // If Username already exists in Simulation throw Exception

                    if (EnumUtils.ToDescription(NetworkSignal.USERNAME_ALREADY_EXISTS).CompareTo(state) == 0)
                    {
                        throw new UsernameAlreadyExistsException("Username bereits vergeben");
                    }
                    // If Servername not exists throw Exception
                    else if (EnumUtils.ToDescription(NetworkSignal.SERVERNAME_NOT_EXISTS).CompareTo(state) == 0)
                    {
                        throw new ServerNameNotExistsException("Ausgewählter Server existiert nicht mehr");
                    }
                }
            }
            else if (successor != null)
            {
                successor.Send(action, content);
            }
            
        }

        public override void Close() {  }
    }
}
