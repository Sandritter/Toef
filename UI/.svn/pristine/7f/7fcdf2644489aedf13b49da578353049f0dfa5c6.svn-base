using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Helper;
using UI.Communication.Interfaces;
using UI.Communication.Models;

namespace UI.Communication
{
    /// <summary>
    /// Bundle of necessary information of client and connection
    /// </summary>
    public class ConnectionData: IClientInfo
    {
        string _brokerurl; //Adress for the messagebroker
        string _soapurl;
        string _ip; // IP-Adress of this Client
        string _simulationName; // Name of the Simulation that is selected for joining
        string _username; // Given Username for this Client
        string _serverAddress;

        public ConnectionData() {
            Ip = FetchIP();
        }

        public ConnectionData(string serverAddress)
        {
            Ip = FetchIP();
            ServerAddress = serverAddress;
            Brokerurl = "tcp://" + serverAddress + ":" + Properties.Settings.Default.JMSPort;
            SOAPurl = "http://" + serverAddress + ":" + Properties.Settings.Default.SOAPPort + Properties.Settings.Default.SOAPrestURL;
        }

        public string ServerAddress
        {
            get
            {
                return _serverAddress;
            }
            set
            {
                _serverAddress = value;
                Brokerurl = "tcp://" + _serverAddress + ":" + Properties.Settings.Default.JMSPort;
                SOAPurl = "http://" + _serverAddress + ":" + Properties.Settings.Default.SOAPPort + Properties.Settings.Default.SOAPrestURL;
            }
        }

        public string SOAPurl
        {
            get
            {
                return _soapurl;
            }
            set
            {
                _soapurl = value;
            }
        }

        public string Brokerurl
        {
            get 
            {
                return _brokerurl;
            }
            set
            {
                this._brokerurl = value;
            }
        }

        public string Ip
        {
            get 
            {
                return _ip;
            }
            set
            {
                this._ip = value;
            }
        }

        public string SimulationName
        {
            get
            {
                return _simulationName;
            }
            set
            {
                this._simulationName = value;
            }
        }

        public string Username
        {
            get
            {
                return _username;
            }
            set
            {
                this._username = value;
            }
        }

        private string FetchIP()
        {
            IPHostEntry Host = Dns.GetHostEntry(Dns.GetHostName());
            return Host.AddressList[0].ToString();
        }
    }
}
