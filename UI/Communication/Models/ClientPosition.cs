using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Communication.Interfaces;

namespace UI.Communication.Models
{
    /// <summary>
    /// Representation of the clients view
    /// </summary>
    public class ClientPosition : ISend
    {
        public int bottomLeftX; //x-Position of the lower lefthanded tile
        public int bottomLeftY; //y-Position of the lower lefthanded tile
        public int topRightX; //x-Position of the upper right handed tile
        public int topRightY; //y-Position of the upper right handed tile

        public ClientPosition(int x1, int y1, int x2, int y2)
        {
            this.bottomLeftX = x1;
            this.bottomLeftY = y1;
            this.topRightX = x2;
            this.topRightY = y2;
        }
    }
}
