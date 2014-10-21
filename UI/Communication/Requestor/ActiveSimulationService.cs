using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Events;
using UI.Communication.Exceptions;
using UI.Communication.Helper;
using UI.Communication.Interfaces;
using UI.Communication.Requestor;
using UI.LobbyFunctions;

namespace UI.Communication.SOAP
{
    /// <summary>
    /// Send a request to server to become all active server
    /// </summary>
    public class ActiveSimulationService: BaseRequestor
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="service">soap service</param>
        public ActiveSimulationService(IClientInfo info, SOAPFunctionsClient service, NetworkAction action)
            :base(info, service, action)
        {   }

        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                try
                {
                    string[] list = service.getServerList();
                    command.Execute(new ServerEventArgs(list));
                }
                catch (Exception e)
                {
                    Console.WriteLine("SOAP EndpointNotFoundException: " + e.Message);
                    throw new EndpointNotFoundException("Lobby Server '" + info.ServerAddress + "' nicht erreichbar.");
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
