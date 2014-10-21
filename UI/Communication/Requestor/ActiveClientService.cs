using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Events;
using UI.Communication.Exceptions;
using UI.Communication.Interfaces;
using UI.Communication.Requestor;
using UI.LobbyFunctions;

namespace UI.Communication.SOAP
{
    /// <summary>
    /// Send a request to server to become all active clients on  the map
    /// </summary>
    public class ActiveClientService: BaseRequestor
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="action">action that the consumer is responsible for</param>
        /// <param name="service">soap service</param>
        public ActiveClientService(IClientInfo info, SOAPFunctionsClient service, NetworkAction action)
            : base(info, service, action)
        {   }

        public override void Send(NetworkAction action, ISend content)
        {
            if (action == this.action)
            {
                try
                {
                    string list = service.getClientList(info.SimulationName);
                    command.Execute(new ClientEventArgs(list.Split(',')));
                }
                catch (Exception e)
                {
                    Console.WriteLine("Hier SOAP EndpointNotFoundException: " + e.Message);
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
