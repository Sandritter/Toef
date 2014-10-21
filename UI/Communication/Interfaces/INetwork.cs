using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Model;

namespace UI.Communication.Interfaces
{
    public interface INetwork
    {
        void RefreshSimulationName(string name);
        void RefreshUsername(string name);
        void DeleteSimulation(string name);
        void CreateSimulation(string name);
        void ActiveSimulations();
        void JoinSimulation();
        void Logout();
        void RequestMap();
        void UpdateTile(Tile tile);
        void sendClientPosition(int x1, int y1, int x2, int y2);
        void PlaceParticipant(Participant participant); 
    }
}
