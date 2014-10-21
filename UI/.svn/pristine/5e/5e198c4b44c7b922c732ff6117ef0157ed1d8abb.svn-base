using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Commands.Interfaces;
using UI.Communication.Controller.Interfaces;
using UI.Communication.Enums;
using UI.Communication.Exceptions;
using UI.Communication.Interfaces;
using UI.Communication.NMS;
using UI.Communication.Requestor;
using UI.Communication.SOAP;
using UI.LobbyFunctions;

namespace UI.Communication.Controller
{
    /// <summary>
    /// Generate and controll all requestors
    /// </summary>
    public class RequestorController: IRequestorController
    {
        SOAPFunctionsClient service;
        IClientInfo info;
        IConverter converter;
        List<IRequestor> requestor;

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="info">contained all relevant information for the connection</param>
        /// <param name="converter">converts for and from communication</param>
        /// <param name="service">soap service</param>
        public RequestorController(IClientInfo info, IConverter converter, SOAPFunctionsClient service)
        {
            this.info = info;
            this.converter = converter;
            this.service = service;
            requestor = new List<IRequestor>();
            Init();
        }

        /// <summary>
        /// Instantiate all requestors
        /// </summary>
        public void Init()
        {
            requestor.Add(new ActiveClientService(info, service, NetworkAction.getActiveClients));
            requestor.Add(new ActiveSimulationService(info, service, NetworkAction.getActiveSimulations));
            requestor.Add(new CreateSimulationService(info, service, NetworkAction.createSimulation));
            requestor.Add(new DeleteSimulationService(info, service, NetworkAction.deleteSimulation));
            requestor.Add(new RegisterClientService(info, service, NetworkAction.login));
            requestor.Add(new MapRequestHandler(info, converter, NetworkAction.getMap));

            // Set up Chain of Responsibility
            int lastIndex = requestor.Count - 1;
            for (int i = 0; i <= lastIndex; i += 1)
            {
                if (i != lastIndex)
                {
                    requestor[i].SetSuccessor(requestor[i + 1]);
                }

            }
        }

        /// <summary>
        /// Set command to a specific requestor
        /// </summary>
        /// <param name="command"></param>
        /// <param name="action">action that the requestor is responsible for</param>
        public void SetCommand(ICommand command, NetworkAction action)
        {
            foreach (IRequestor r in requestor)
            {
                if (action == ((BaseRequestor)r).Action)
                {
                    r.SetCommand(command);
                }
            }
        }

        /// <summary>
        /// Return the first successor of the chain of responsibility
        /// </summary>
        /// <returns></returns>
        public IRequestor FirstSuccessor()
        {
            if (requestor.Count > 0)
            {
                return requestor[0];
            }
            throw new NoRequestorExists("Es wurde kein Requestor instanziiert.");

        }

        /// <summary>
        /// Close connections of each requestor
        /// </summary>
        public void Close()
        {
            foreach (IRequestor r in requestor)
            {
                r.Close();
            }
        }
    }
}
