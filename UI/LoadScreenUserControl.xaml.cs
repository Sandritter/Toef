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
    /// Interaktionslogik für LoadScreenUserControl.xaml
    /// </summary>
    public partial class LoadScreenUserControl : UserControl
    {
        //private int dotCount = 3;

        public LoadScreenUserControl()
        {
            InitializeComponent();
            //DispatcherTimer timer = new DispatcherTimer(DispatcherPriority.Send);
            //timer.Tick += new EventHandler(update);
            //timer.Interval = new TimeSpan(0, 0, 0, 0, 50);
            //timer.Start();
        }

        //private void update(object sender, EventArgs e)
        //{
        //    var text = "Map wird geladen, bitte warten ";

        //    for(var i = 0; i < dotCount; i ++){
        //        text += ".";
        //    }
        //    Textfield.Text = text;

        //    dotCount++;
        //    dotCount %= 4;
        //}
    }
}
