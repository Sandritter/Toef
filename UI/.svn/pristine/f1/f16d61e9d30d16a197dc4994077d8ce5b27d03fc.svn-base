using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace UI
{

    /// <summary>
    /// Calculates the position of the participant in the canvas
    /// has to be done, because (0/0) in the canvas is on the top left and the (0/0) of the map in the backend is on the bottom left
    /// </summary>
    class ParticipantPosConverter : IValueConverter
    {

        /// <summary>
        /// converts the position to the other coordinate system
        /// </summary>
        /// <param name="value">value</param>
        /// <param name="targetType">targettype</param>
        /// <param name="parameter">parameter (x/y axis)</param>
        /// <param name="culture">cultureinfo</param>
        /// <returns>return the converted value</returns>
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {

            int tileSize = Properties.Settings.Default.TileSize;
            int mapWidth = Properties.Settings.Default.MapWidth;
            int mapHeight = Properties.Settings.Default.MapHeight;
            int participantSize = Properties.Settings.Default.ParticipantSize;

            string axis = (string)parameter;

            if (axis == "x")
            {
                float x = (float)value;
                return ((int)(x*mapWidth*tileSize)-participantSize/2);
            }
            else
            {
                int y = (int)(((float)value * mapHeight * tileSize) + participantSize / 2);
                int posY = mapHeight*tileSize - y ;
                return posY;
            }
        }

        /// <summary>
        /// converts back the position to the other coordinate system
        /// </summary>
        /// <param name="value">value</param>
        /// <param name="targetType">targettype</param>
        /// <param name="parameter">parameter (x/y axis)</param>
        /// <param name="culture">cultureinfo</param>
        /// <returns>return the converted value</returns>
        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            string axis = (string)parameter;

            if (axis == "x")
            {
                float x = (float)value;
                return (int)x * 3000;
            }
            else
            {
                float y = (float)value;
                return (int)y * 3000;
            }
        }
    }
}