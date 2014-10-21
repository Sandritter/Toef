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
    /// Send a request to delete a server
    /// </summary>
    public class DeleteSimulationService: BaseRequestor
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="service">soap service</param>
        public DeleteSimulationService(IClientInfo info, SOAPFunctionsClient service, NetworkAction action)
            :base(info, service, action)
        {   }

        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                string state = null;

                try
                {
                    state = service.deleteServer(info.SimulationName, info.Ip);
                }
                catch (Exception e)
                {
                    Console.WriteLine("SOAP EndpointNotFoundException: " + e.Message);
                    throw new EndpointNotFoundException("Lobby Server '" + info.ServerAddress + "' nicht erreichbar.");
                }

                if (EnumUtils.ToDescription(NetworkSignal.ACTION_SUCCESSFUL).CompareTo(state) != 0)
                {
                    if (EnumUtils.ToDescription(NetworkSignal.CLIENT_NOT_HOST).CompareTo(state) == 0)
                    {
                        throw new ClientNotHostException("Nur selbsterstellte Server können gelöscht werden.");
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
