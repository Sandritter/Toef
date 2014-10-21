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
    /// Converts the fuel amount of a participant to the appropriate color
    /// (green = normal speed, yellow = fast, red = very fast)
    /// Only works in one direction
    /// </summary>
	public class FuelBarColorConverter : IValueConverter
	{
		public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
		{
			int fuelBarValue = (int)value;
			Brush foreground;

            // set appropriate color
			if (fuelBarValue <= 10)
			{
				foreground = Brushes.Red;
			}
			else if (fuelBarValue <= 30)
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
