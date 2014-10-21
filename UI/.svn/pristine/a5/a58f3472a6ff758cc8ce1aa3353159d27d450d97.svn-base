using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Enums
{

    /// <summary>
    /// uppertypes for the participants
    /// </summary>
    public enum UpperParticipantType
    {
        None,
        Car,
        Ship
    }

    /// <summary>
    /// types of the participants
    /// </summary>
    public enum ParticipantType
    {
        None,
        //Cars
        CabrioPink,
        CabrioBlue,
        CabrioGreen,
        CabrioSilver,
        CabrioYellow,
        LimoBlue,
        //Ships
        Boat,
        Kanu

    }

    /// <summary>
    /// list of info keys of the participant
    /// </summary>
    public enum OptInfosKey
    {
        Damaged,
        Fuel,
        DriverType,
        Velocity,
		MaxVelocity,
		MaxFuel
    }


    /// <summary>
    /// helperclass to get the uppertype from a participanttype
    /// </summary>
    public class ParticipantEnumHelper
    {
        /// <summary>
        /// returns the uppertype of a participanttype
        /// </summary>
        /// <param name="type">participanttype</param>
        /// <returns>uppertype</returns>
        public static UpperParticipantType GetUpperParticipantType(ParticipantType type)
        {
            if (type == ParticipantType.None)
            {
                return UpperParticipantType.None;
            }
            else if (type == ParticipantType.Boat || type == ParticipantType.Kanu)
            {
                return UpperParticipantType.Ship;
            }
            else
            {
                return UpperParticipantType.Car;
            }
        }
    }
}
