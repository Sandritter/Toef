using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Enums
{
    /// <summary>
    /// Actions that consumer, producer and requestor are responsible for
    /// </summary>
    public enum NetworkAction
    {
        placeTile,
        placeParticipant,
        updatePosition,
        deleteParticipant,
        logout,
        sendChatMessage,
        refuelParticipant,
        sendKeepAlive,
        getActiveClients,
        getActiveSimulations,
        createSimulation,
        deleteSimulation,
        getMap,
        login,
        incomingActiveClients,
        incomingChatMessage,
        incomingHostDown,
        incomingKeepAlive,
        incomingParticipants,
        incomingTile
    }

    /// <summary>
    /// Messagetopics for Broker
    /// </summary>
    public enum MessageTopics
    {
        _clientconnection_queue,
        _map_queue,
        _viewTile_queue,
        _viewTile_topic,
        _position_queue,
        _logout_queue,
        _hostDown_topic,
        _placeViewCar_queue,
        _viewCarUpdate_queue,
        _deleteParticipant_queue,
        _chatMessage_queue,
        _chatMessage_topic,
        _clientList_topic,
        _keepAlive_queue,
        _keepAlive_topic,
        _carFuelInfo_queue
    }

    /// <summary>
    /// Propertie names for messaging
    /// </summary>
    public enum MessageProperties
    {
        EDITMODE,
        SIMULATIONNAME,
        IPADDRESS,
        USERNAME,
        PARTICIPANTID,
        CLIENTLIST
    }

    /// <summary>
    /// Messageproperties for sending updates to server
    /// </summary>
    public enum EditMode
    {
        SET,
        DELETE
    }

    /// <summary>
    /// Messages from server
    /// </summary>
    public enum NetworkSignal
    {
        [DisplayText("000")]
        CLIENTCONNECT_SUCCSESSFULL,
        [DisplayText("101")]
        USERNAME_ALREADY_EXISTS,
        [DisplayText("102")]
        IPADDRESS_ALREADY_EXISTS,
        [DisplayText("103")]
        SERVERNAME_NOT_EXISTS,
        [DisplayText("104")]
        SERVERNAME_ALREADY_EXISTS,
        [DisplayText("105")]
        CLIENT_NOT_HOST,
        [DisplayText("200")]
        ACTION_SUCCESSFUL
    }

    /// <summary>
    /// Keynames for Mapmessage
    /// </summary>
    public enum ClientConnectionKeys
    {
        IP,
        USERNAME,
        SIMUALTIONNAME
    }

    /// <summary>
    /// Returns text information about a enum
    /// </summary>
    public class DisplayText : Attribute
    {
        public DisplayText(string text)
        {
            this.text = text;
        }

        private string text;
        public string Text
        {
            get {
                return text;
            }
            set {
                text = value;
            }
        }
    }

    /// <summary>
    /// Class containing utility methods concerning enums
    /// </summary>
    public static class EnumUtils
    {
        public static string ToDescription(this Enum en)
        {

            Type type = en.GetType();

            MemberInfo[] memInfo = type.GetMember(en.ToString());

            if (memInfo != null && memInfo.Length > 0)
            {

                object[] attrs = memInfo[0].GetCustomAttributes(
                                              typeof(DisplayText),

                                              false);

                if (attrs != null && attrs.Length > 0)

                    return ((DisplayText)attrs[0]).Text;

            }

            return en.ToString();

        }
    }
}
