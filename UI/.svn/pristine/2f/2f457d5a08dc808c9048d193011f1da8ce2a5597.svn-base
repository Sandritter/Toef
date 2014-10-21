using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;
using UI.Enums;

namespace UI.Communication.Models
{
    /// <summary>
    /// Container contained all content that will be send to the server 
    /// </summary>
    public class UpdateParticipant: ISend
    {
        public ParticipantType type;
        public int id;
        public float relPosX;
        public float relPosY;
        public float rot;
        public Dictionary<string, string> optInfos;

        public UpdateParticipant(ParticipantType type, int id, float relPosX, float relPosY, float rot, Dictionary<string, string> optInfos)
        {
            this.type = type;
            this.id = id;
            this.relPosX = relPosX;
            this.relPosY = relPosY;
            this.rot = rot;
            this.optInfos = optInfos;
        }

    
    }
}
