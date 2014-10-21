using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;
using UI.Builder;
using UI.Builder.Interface;
using UI.Enums;
using UI.Manager.Interface;
using UI.Repositories;

namespace UI.Manager
{
    /// <summary>
    /// Administrates different soundplayers
    /// </summary>
    public class SoundManager: ISoundManager
    {
        #region variables
        SoundRepository _rep;
        ISoundBuilder _builder;
        private Dictionary<SoundEffect, SoundPlayer> _effectPlayer;
        private Dictionary<int, MediaPlayer> _inGamePlayer;
        private Dictionary<int, MediaPlayer> _menuPlayer;
        #endregion

        public SoundManager()
        {
            _rep = new SoundRepository();
            _builder = new SoundBuilder();
            _effectPlayer = new Dictionary<SoundEffect, SoundPlayer>();
            InitEffectPlayers();
            _inGamePlayer = new Dictionary<int, MediaPlayer>();
            InitInGamePlayers();
            _menuPlayer = new Dictionary<int, MediaPlayer>();
            InitMenuPlayers();
        }



        #region public methods
        /// <summary>
        /// Fetches Soundeffectplayer for given enumtype 
        /// </summary>
        /// <param name="se"></param>
        /// <returns></returns>
        public SoundPlayer FetchEffectPlayer(SoundEffect se)
        {
            return _effectPlayer[se];
        }

        /// <summary>
        /// Fetches Menumusicplayer for given index
        /// </summary>
        /// <param name="pos"></param>
        /// <returns></returns>
        public MediaPlayer FetchMenuPlayer(int pos)
        {
            return _menuPlayer[pos];
        }

        /// <summary>
        /// Fetches Ingamemusicplayer for given index
        /// </summary>
        /// <param name="pos"></param>
        /// <returns></returns>
        public MediaPlayer FetchInGamePlayer(int pos)
        {
            return _inGamePlayer[pos];
        }

        /// <summary>
        /// Returns amount of ingamemusic-players
        /// </summary>
        /// <returns></returns>
        public int CountInGamePlayers()
        {
            return _inGamePlayer.Count;
        }

        /// <summary>
        /// Returns amount of menumusic-players
        /// </summary>
        /// <returns></returns>
        public int CountMenuPlayers()
        {
            return _menuPlayer.Count;
        }
        #endregion

        #region private methods
        /// <summary>
        /// Maps soundeffectplayers on thier enumtype
        /// </summary>
        private void InitEffectPlayers()
        {
            Dictionary<SoundEffect, string> _paths = _rep.FetchEffectPath();
            foreach (SoundEffect key in _paths.Keys)
            {
                _effectPlayer.Add(key, _builder.BuildEffectPlayer(_paths[key]));
            }
        }

        /// <summary>
        /// Maps menumusicplayers on thier index
        /// </summary>
        private void InitMenuPlayers()
        {
            Dictionary<int, string> _paths = _rep.FetchMenuPaths();
            foreach (int key in _paths.Keys)
            {
                _menuPlayer.Add(key, _builder.BuildMusicPlayer(_paths[key]));
            }
        }

        /// <summary>
        /// Maps ingamemusicplayers on thier index
        /// </summary>
        private void InitInGamePlayers()
        {
            Dictionary<int, string> _paths = _rep.FetchInGamePaths();
            foreach (int key in _paths.Keys)
            {
                _inGamePlayer.Add(key, _builder.BuildMusicPlayer(_paths[key]));
            }
        }
        #endregion
    }
}
