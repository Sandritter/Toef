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
    public class NewParticipant: ISend
    {
        public ParticipantType type;
        public int tilePosX;
        public int tilePosY;
        public float rot;

        public NewParticipant(ParticipantType type, int tilePosX, int tilePosY, float rot)
        {
            this.type = type;
            this.tilePosX = tilePosX;
            this.tilePosY = tilePosY;
            this.rot = rot;
        }
    }
}
