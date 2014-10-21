using Apache.NMS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Enums;
using UI.Communication.Events;
using UI.Communication.Interfaces;
using UI.Communication.Models;
using UI.Model;

namespace UI.Communication.NMS.Consumer
{
    public class ViewTileConsumer: BaseConsumer, IBuffer
    {
        Queue<ViewTile> updateBuffer;
        Boolean mapFlag;

        public ViewTileConsumer(IClientInfo connectionData, IConverter converter, NetworkAction action)
            : base(connectionData, converter, action)
        {
            this.mapFlag = false;
            this.updateBuffer = new Queue<ViewTile>();
            this.queueTopic = connectionData.SimulationName + MessageTopics._viewTile_topic.ToString("g");
            Init();
        }


        /*
        * Callbackmethod for incoming message
        */
        protected override void OnMessage(IMessage mes)
        {
            ITextMessage tmsg = mes as ITextMessage;
            string text = tmsg.Text;
            ViewTile vt = converter.StringToViewTile(text);

            if (mapFlag) //If the map is there
            {
                command.Execute(new TileEventArgs(converter.Convert(vt)));
            }
            else
            {
                updateBuffer.Enqueue(vt); //Enqueue incoming updates in buffer
            }
        }

        /*
         * Callbackmethod for after map is recieved
         * Throws Event for every tileupdate in the curren buffer
         */
        public void ProcessBuffer(object sender, EventArgs args)
        {
            while(updateBuffer.Count != 0)
            {
                System.Diagnostics.Debug.WriteLine("Ich entleere den Buffer");
                ViewTile vt = (ViewTile)updateBuffer.Dequeue();
                Tile t = converter.Convert(vt);
                command.Execute(new TileEventArgs(t));
            }
            mapFlag = true; //set flag
        }
    }
}
