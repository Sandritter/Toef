using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UI.Enums;

namespace UI.Repositories
{
    /// <summary>
    /// Administrates the soundpaths for music and effects
    /// </summary>
    public class SoundRepository
    {
        #region variables
        private string absBackgroundPath; //Path to Background directory
        private string absIngamePath; //Path to Ingame directory
        private string absMenuPath; //Path to Menu directory
        private string absEffectsPath; //Path to Effects directory
        string preSoundDirPath = "../"; //Relative prefix from this file to sound directory
        Dictionary<SoundEffect, string> _effectsPath;
        Dictionary<int, string> _menuPath;
        Dictionary<int, string> _inGamePath;
        #endregion

        public SoundRepository()
        {
            //Initialise absolute paths to soundfiles - All the directory names come from properties file
            absBackgroundPath = GenerateDirPath(Properties.Settings.Default.BackgroundSoundPath);
            absEffectsPath = GenerateDirPath(Properties.Settings.Default.EffectsSoundPath);
            absIngamePath = absBackgroundPath + Properties.Settings.Default.InGameSoundPath;
            absMenuPath = absBackgroundPath + Properties.Settings.Default.MenuSoundPath;

            //absBackgroundPath = "pack://application:,,,/"+ (Properties.Settings.Default.BackgroundSoundPath);
            //absEffectsPath = "pack://application:,,,/" + (Properties.Settings.Default.EffectsSoundPath);
            //absIngamePath = absBackgroundPath + Properties.Settings.Default.InGameSoundPath;
            //absMenuPath = absBackgroundPath + Properties.Settings.Default.MenuSoundPath;

            //Initialise different dictionaries with Soundpaths
            _effectsPath = new Dictionary<SoundEffect, string>();
            InitEffectPaths();
            _inGamePath = new Dictionary<int, string>();
            InitInGamePaths();
            _menuPath = new Dictionary<int, string>();
            InitMenuPaths();
          

        }

        #region public methods
        /// <summary>
        /// Fetches absolute path to given enumtype for effectsounds
        /// </summary>
        /// <param name="se"></param>
        /// <returns></returns>
        public Dictionary<SoundEffect, string> FetchEffectPath()
        {
            return _effectsPath;
        }

        /// <summary>
        /// Fetches absolute path with given number to ingamemusic file
        /// </summary>
        /// <param name="i"></param>
        /// <returns></returns>
        public Dictionary<int, string> FetchInGamePaths()
        {
            return _inGamePath;
        }

        /// <summary>
        /// Fetches absolute path with given number to menumusic file
        /// </summary>
        /// <param name="i"></param>
        /// <returns></returns>
        public Dictionary<int, string> FetchMenuPaths()
        {
            return _menuPath;
        }
        #endregion

        #region private methods
        /// <summary>
        /// Initialises dictionary for effectsounds with enumtypes mapped on thier absolute paths
        /// </summary>
        private void InitEffectPaths()
        {
            //For every enumtype in the soundeffect enum generate an absolute path
            foreach (SoundEffect se in Enum.GetValues(typeof(SoundEffect)))
            {
                _effectsPath.Add(se, GenerateSoundPath(absEffectsPath, se.ToString("g"), Properties.Settings.Default.EffectsSoundType));
            }
        }

        /// <summary>
        /// Initialises dictionary for ingamesounds with index mapped on thier absolute paths
        /// </summary>
        private void InitInGamePaths()
        {
            int i = 0;
            DirectoryInfo parentDir = new DirectoryInfo(absIngamePath);

            //For every file in the ingame directory generate an absolute path
            foreach (FileInfo f in parentDir.GetFiles())
            {
                _inGamePath.Add(i, GenerateSoundPath(absIngamePath, f.Name, ""));
                i += 1;
            }
        }

        /// <summary>
        /// Initialises dictionary for menusounds with index mapped on thier absolute paths
        /// </summary>
        private void InitMenuPaths()
        {
            int i = 0;
            DirectoryInfo parentDir = new DirectoryInfo(absMenuPath);

            //For every file in the menu directory generate an absolute path
            foreach (FileInfo f in parentDir.GetFiles())
            {
                _menuPath.Add(i, GenerateSoundPath(absMenuPath, f.Name, ""));
                i += 1;
            }
        }

        /// <summary>
        /// Generates absolute path to given soundfile
        /// </summary>
        /// <param name="soundfile"></param>
        /// <returns></returns>
        private string GenerateSoundPath(string path, string soundfile, string type)
        {
            return path + soundfile + type;
        }

        /// <summary>
        /// Generates path to given directory below  soundfile
        /// </summary>
        /// <param name="directory"></param>
        /// <returns></returns>
        private string GenerateDirPath(string directory)
        {
            string relPath = preSoundDirPath + Properties.Settings.Default.BasicSoundPath + directory;
            string path = Path.GetFullPath(relPath);
            return path.Replace("\\bin\\", "\\"); //Must be removed otherwise paths are unusable
        }
        #endregion

    }
}
