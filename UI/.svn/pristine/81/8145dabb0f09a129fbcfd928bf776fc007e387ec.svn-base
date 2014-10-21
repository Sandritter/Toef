using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Threading;
using UI.Communication;
using UI.Communication.Events;
using UI.Enums;
using UI.Helper;

namespace UI.Model
{

    /// <summary>
    /// this class manages the movement and placement of the participants
    /// </summary>
    class ParticipantManager
    {
        DispatcherTimer timer;
        private static int FPS = Properties.Settings.Default.FPS;
        private int updateRate = 1000 / FPS;
        private DateTime newTime;
        private DateTime oldTime;
        private int diffTime = 0;
        private bool hasStarted = false;
        private Network network;
        private Dictionary<int, Participant> participants;
        private Dictionary<int, Participant> newParticipantDic;

        public event EventHandler<ParticipantArgs> participantAddedHandler;
        public event EventHandler<ParticipantArgs> participantDeletedHandler;

        private static readonly object lockObject = new object();

        private Map map;

        public ParticipantManager(Network network, Map map)
        {

            timer = new DispatcherTimer();
            timer.Tick += new EventHandler(update);
            timer.Interval = new TimeSpan(0, 0, 0, 0, updateRate);
            participants = new Dictionary<int, Participant>();
            this.network = network;
            this.network.UpdateParticipantReceiver.Handler += OnParticipantListChanged;
            oldTime = DateTime.Now;

            this.map = map;
        }

        /// <summary>
        /// calculates the new position of every participant with linear interpolation
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void update(object sender, EventArgs e)
        {
            lock (lockObject) {
                //f = interval between 0 and 1 
                float f = 1 / ((float)diffTime / updateRate);

                List<int> deleteList = new List<int>();
                //marks all participant that have to be deleted
                foreach (int key in participants.Keys)
                {
                    if (!newParticipantDic.ContainsKey(key))
                    {
                        deleteList.Add(key);
                    }
                }

                //deletes the marked participants
                foreach (int key in deleteList)
                {
                    ParticipantArgs a = new ParticipantArgs();
                    a.participant = participants[key];
                    participants.Remove(key);
                    participantDeletedHandler(this, a);
                }

                //updates the participants
                foreach (int key in newParticipantDic.Keys)
                {

                    //if an element is in the new list but not in den actual list
                    if (!participants.ContainsKey(key))
                    {
                        Participant newParticipant = newParticipantDic[key];
                        participants.Add(key, newParticipant);

                        // notify view
                        ParticipantArgs a = new ParticipantArgs();
                        a.participant = newParticipant;
                        participantAddedHandler(this, a);
                    }

                    participants[key].OptInfos = newParticipantDic[key].OptInfos;

                    # region interpolation

                    //x und y interpolation
                    participants[key].PosX = Interpolate(participants[key].PosX, newParticipantDic[key].PosX, f);
                    participants[key].PosY = Interpolate(participants[key].PosY, newParticipantDic[key].PosY, f);

                    //rotation interpolation
                    //a bit more complicated then the interpolation of the position, because the particicpant will spin if the value goes from 350 to 0
                    float rot1 = Math.Abs(participants[key].Rot - newParticipantDic[key].Rot);
                    float rot2 = 0;

                    if (participants[key].Rot >= newParticipantDic[key].Rot)
                    {
                        rot2 = Math.Abs(participants[key].Rot - (newParticipantDic[key].Rot + 360));

                        if (rot1 <= rot2)
                        {
                            participants[key].Rot = Interpolate(participants[key].Rot, newParticipantDic[key].Rot, f);
                        }
                        else
                        {
                            participants[key].Rot = Interpolate(participants[key].Rot, newParticipantDic[key].Rot + 360, f) % 360;
                        }
                    }
                    else
                    {
                        rot2 = Math.Abs((participants[key].Rot + 360) - newParticipantDic[key].Rot);

                        if (rot1 <= rot2)
                        {
                            participants[key].Rot = Interpolate(participants[key].Rot, newParticipantDic[key].Rot, f);
                        }
                        else
                        {
                            participants[key].Rot = Interpolate(participants[key].Rot + 360, newParticipantDic[key].Rot, f) % 360;
                        }
                    }

                    # endregion

                    #region ship hiding under a bridge

                    // display or hide ships if they drive over a StreetBridge-Tile
                    if (ParticipantEnumHelper.GetUpperParticipantType(participants[key].Type) == UpperParticipantType.Ship)
                    {
                        Participant actParticipant = participants[key];
                        float rot = actParticipant.Rot;
                        float rotDivergence = 2.0f; // variance of rotation
                        float posDivergence = Properties.Settings.Default.ParticipantSize / 2; // half of ship image height
                        int frontTileX = 0;
                        int frontTileY = 0;
                        if (actParticipant.Visible) // ship is not under a bridge
                        {
                            if ((90 - rotDivergence) <= rot && rot <= (90 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, posDivergence);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);

                            }
                            else if ((270 - rotDivergence) <= rot && rot <= (270 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, -posDivergence);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);
                            }
                            else if ((180 - rotDivergence) <= rot && rot <= (180 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, -posDivergence);
                            }
                            else if ((0 <= rot && rot <= (0 + rotDivergence)) ||
                                ((360 - rotDivergence) <= rot && rot <= 360))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, posDivergence);
                            }

                            Tile frontTile = map.GetTile(frontTileX, frontTileY);
                            if (frontTile != null)
                            {
                                if (frontTile.State == TileType.StreetBridge)
                                {
                                    actParticipant.Visible = false;
                                }
                            }
                            
                        }
                        else // ship is under a bridge
                        {
                            int endTileX = 0;
                            int endTileY = 0;
                            if ((90 - rotDivergence) <= rot && rot <= (90 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, posDivergence);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);
                                // calculate tile position on the end of the ship
                                endTileX = PositionConverter.TilePosX(actParticipant.PosX, -posDivergence);
                                endTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);
                            }
                            else if ((270 - rotDivergence) <= rot && rot <= (270 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, -posDivergence);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);
                                // calculate tile position on the end of the ship
                                endTileX = PositionConverter.TilePosX(actParticipant.PosX, posDivergence);
                                endTileY = PositionConverter.TilePosY(actParticipant.PosY, 0);
                            }
                            else if ((180 - rotDivergence) <= rot && rot <= (180 + rotDivergence))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, -posDivergence);
                                // calculate tile position on the end of the ship
                                endTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                endTileY = PositionConverter.TilePosY(actParticipant.PosY, posDivergence);
                            }
                            else if ((0 <= rot && rot <= (0 + rotDivergence)) ||
                                ((360 - rotDivergence) <= rot && rot <= 360))
                            {
                                // calculate tile position on the top of the ship
                                frontTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                frontTileY = PositionConverter.TilePosY(actParticipant.PosY, posDivergence);
                                // calculate tile position on the end of the ship
                                endTileX = PositionConverter.TilePosX(actParticipant.PosX, 0);
                                endTileY = PositionConverter.TilePosY(actParticipant.PosY, -posDivergence);
                            }
                            Tile frontTile = map.GetTile(frontTileX, frontTileY);
                            Tile endTile = map.GetTile(endTileX, endTileY);
                            if (frontTile != null && endTile != null)
                            {
                                if (frontTile.State != TileType.StreetBridge && endTile.State != TileType.StreetBridge)
                                {
                                    actParticipant.Visible = true;
                                }
                            }
                        }
                    }

                    #endregion
                }
            }
        }

        //Lineare Interpolation
        private float Interpolate(float a, float b, float f)
        {
            return a + f * (b - a);
        }

        /// <summary>
        /// places a new participant
        /// </summary>
        /// <param name="type">type of the participant</param>
        /// <param name="x">x position</param>
        /// <param name="y">y position</param>
        /// <param name="rotation">rotation</param>
        public void placeParticipant(ParticipantType type, float x, float y, float rotation)
        {
            Participant p = new Participant(1, type, x, y, rotation, null);
            network.PlaceParticipant(p);
        }


        /// <summary>
        /// callback if the participant list has changed
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void OnParticipantListChanged(object sender, EventArgs args)
        {
            newTime = DateTime.Now;
            TimeSpan span = newTime - oldTime;
            diffTime = (int)span.TotalMilliseconds;
            oldTime = DateTime.Now;
            lock (lockObject) {
                newParticipantDic = ((ParticipantEventArgs)args).participantArray;
            }

            // proofs if participant updates already has been received. Then the interpolation can start
            if (!hasStarted)
            {
                timer.Start();
                diffTime = 100;
                hasStarted = true;
            }
        }
    }

    public class ParticipantArgs : EventArgs
    {
        public Participant participant { get; set; }
    }
}
