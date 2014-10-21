using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;
using System.Windows.Media;

namespace UI
{
    /// <summary>
    /// Converts the velocity of a participant to the appropriate color
    /// (green = (quite) full, yellow = rather empty, red = (almost) empty)
    /// Only works in one direction
    /// </summary>
	public class VelocityBarColorConverter : IValueConverter
	{
		public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
		{
			int velocityBarValue = (int)value;
            Brush foreground;

            // set appropriate color
			if (velocityBarValue >= 90)
			{
				foreground = Brushes.Red;
			}
			else if (velocityBarValue >= 70)
			{
				foreground = Brushes.Yellow;
			}
			else
			{
				foreground = Brushes.Green;
			}

			return foreground;
		}

		public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
		{
			throw new NotImplementedException();
		}
	}
}