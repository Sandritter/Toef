using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;

namespace UI.Builder.Interface
{
    public interface ISoundBuilder
    {
        SoundPlayer BuildEffectPlayer(string path);
        MediaPlayer BuildMusicPlayer(string path);
    }
}
