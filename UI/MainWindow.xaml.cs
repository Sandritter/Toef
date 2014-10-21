using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace UI
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            DispatcherTimer timer = new DispatcherTimer();
            timer.Tick += new EventHandler(update);
            timer.Interval = new TimeSpan(0, 0, 0, 0, 15);
            timer.Start();
           
        }

        private void update(object sender, EventArgs e)
        {
            var ScrollSpeed = 20;

            Point pos = Mouse.GetPosition(Application.Current.MainWindow);
            var pX = pos.X;
            var pY = pos.Y;
            if (pX < 5)
            {
                MapScrollViewer.ScrollToHorizontalOffset(MapScrollViewer.HorizontalOffset - ScrollSpeed);
            }
            if (pX > System.Windows.SystemParameters.PrimaryScreenWidth - 5)
            {
                MapScrollViewer.ScrollToHorizontalOffset(MapScrollViewer.HorizontalOffset + ScrollSpeed);
            }
            if (pY < 5)
            {
                MapScrollViewer.ScrollToVerticalOffset(MapScrollViewer.VerticalOffset - ScrollSpeed);
            }
            if (pY > System.Windows.SystemParameters.PrimaryScreenHeight - 5)
            {
                MapScrollViewer.ScrollToVerticalOffset(MapScrollViewer.VerticalOffset + ScrollSpeed);
            }
            
        }


        //Unterbindet das Scrollen in der Map mit dem Mausrad
        private void MapScrollViewer_PreviewMouseWheel(object sender, MouseWheelEventArgs e)
        {
            e.Handled = true;
        }

        private void Image_MouseEnter(object sender, MouseEventArgs e)
        {

        }
    }
}
