using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;
using UI.Builder.Interface;

namespace UI.Builder
{
    /// <summary>
    /// Builds players for soundfiles with absolute paths
    /// </summary>
    public class SoundBuilder: ISoundBuilder
    {
        public SoundBuilder()
        { }

        /// <summary>
        /// Builds Soundeffectplayer with an absolute path
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public SoundPlayer BuildEffectPlayer(string path)
        {
            return new SoundPlayer(path);
        }

        /// <summary>
        /// Builds Musicplayer with an absolute path
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public MediaPlayer BuildMusicPlayer(string path)
        {
            Uri uri = new Uri(path); //Mediaplayers need uri from absolute path
            MediaPlayer backgroundPlayer = new MediaPlayer();
            backgroundPlayer.Open(uri);
            return backgroundPlayer;
        }

    }
}
