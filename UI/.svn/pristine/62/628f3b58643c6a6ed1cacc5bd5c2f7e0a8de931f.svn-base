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
    /// Calculates the position of the tile in the map
    /// has to be done, because (0/0) in the canvas is on the top left and the (0/0) of the map in the backend is on the bottom left
    /// </summary>
    class TilePosConverter : IValueConverter
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
           int size = Properties.Settings.Default.TileSize;
           int sizeY = Properties.Settings.Default.MapHeight;

           string axis = (string) parameter;

           if(axis == "x"){
               int x = (int) value;
               int posX = x * size;
               return posX;
           } else {
               int y = (int) value;
               int posY = ((sizeY-1) * size) - y * size;
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
            int size = Properties.Settings.Default.TileSize;
            int sizeY = Properties.Settings.Default.MapHeight;

            string axis = (string)parameter;

            if (axis == "x")
            {
                int x = (int)value;
                int posX = x * size;
                return posX;
            }
            else
            {
                int y = (int)value;
                int posY = (sizeY * size) - y * size;
                return posY;
            }
        }
    }
}