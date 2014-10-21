using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace UI.Model
{
    /// <summary>
    /// Icons for all objects, settable by the user through the bottom left menu
    /// </summary>
    public class MenuIcon : ObservableModelBase
    {
        BitmapImage _bi;
        String tooltip;
        MenuCategories category;
        Object v;

        public BitmapImage BmImage
        {
            get
            {
                return _bi;
            }

            set
            {
                _bi = value;
                OnPropertyChanged("BmImage");
            }
        }

        public MenuCategories Category
        {
            get
            {
                return category;
            }
        }

        public Object V
        {
            get
            {
                return v;
            }
        }

        public MenuIcon(BitmapImage _bi, String tooltip, MenuCategories category, Object v)
        {
            this._bi = _bi;
            this.tooltip = tooltip;
            this.category = category;
            this.v = v;
        }
    }
}
