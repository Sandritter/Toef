using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;
using UI.Communication.Models;
using UI.Enums;
using UI.Model;

namespace UI.Communication.Converter
{
    /// <summary>
    /// Responsible for converting messages to different data structures
    /// </summary>
    public class NetworkConverter : IConverter
    {
    
        public NetworkConverter()
        {   }

        public string ClientPositionToString(ClientPosition cp)
        {
            return JsonConvert.SerializeObject(cp);
        }

        public Tile Convert(ViewTile t)
        {
            return new Tile(t.posX, t.posY, t.rot, t.type);
        }

        public UpdateParticipant[] StringToUpdateParticipantArray(string vc)
        {
            return JsonConvert.DeserializeObject<UpdateParticipant[]>(vc);
            
        }

        public ViewTile StringToViewTile(string vt)
        {
            return JsonConvert.DeserializeObject<ViewTile>(vt);
        }

        public ViewTile TileToViewTile(Tile t)
        {
            return new ViewTile((TileType)t.State, (int)t.X, (int)t.Y, t.Rotation);
        }

        public string ViewTileToString(ViewTile vt)
        {
            return JsonConvert.SerializeObject(vt);
        }
       
        public ViewTile[][] StringToViewTileArray(string s)
        {
            return JsonConvert.DeserializeObject<ViewTile[][]>(s);
        }

        public Tile[,] ViewTileArrayToTileArray(ViewTile[][] vt)
        {
            Tile[,] copy = new Tile[vt.Length,vt[0].Length];

            for(int i=0; i<vt[0].Length; i++)
            {
                for (int j = 0; j < vt.Length; j++ )
                {
                    copy[i, j] = new Tile(vt[i][j].posX, vt[i][j].posY, vt[i][j].rot, vt[i][j].type);
                }
            }

            return copy;

        }

        
        public Dictionary<int, Participant> DictToParticipantArray(UpdateParticipant[] vc)
        {
            Dictionary<int, Participant> copy = new Dictionary<int, Participant>();

            for (int i = 0; i < vc.Length; i++)
            {
               copy[vc[i].id] = new Participant(vc[i].id, vc[i].type, vc[i].relPosX, vc[i].relPosY, vc[i].rot, vc[i].optInfos);
                
            }

            return copy;
        }

        public string NewParticipantToString(NewParticipant np)
        {
            return JsonConvert.SerializeObject(np);
        }

        public UpdateParticipant StringToUpdateParticipant(string s)
        {
            return JsonConvert.DeserializeObject<UpdateParticipant>(s);
        }
    }
}
