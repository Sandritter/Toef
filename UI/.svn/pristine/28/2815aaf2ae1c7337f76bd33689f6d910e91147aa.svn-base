using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;
using UI.Controller.Interface;
using UI.Enums;
using UI.Manager;
using UI.Manager.Interface;

namespace UI.Controller
{
    /// <summary>
    /// Controlles and executes soundfiles
    /// </summary>
    public class SoundController: ISoundController
    {
        #region variables
        ISoundManager manager;
        MediaPlayer inGamePlayer; //Musicplayer for ingamemusic
        MediaPlayer menuPlayer; //Musicplayer for menumusic
        int actInGameSound; //Index of current ingamemusicfile 
        int actMenuSound; //Index of current menumusicfile 
        int amountInGameSounds;
        int amountMenuSounds;
        Random random;
        #endregion

        public SoundController()
        {
            manager = new SoundManager();
            random = new Random();
            amountMenuSounds = manager.CountMenuPlayers(); 
            amountInGameSounds = manager.CountInGamePlayers();
            actInGameSound = random.Next(0,amountInGameSounds); //Music should start at random index, depending on amount of sounds
            actMenuSound = random.Next(0,amountMenuSounds);

            inGamePlayer = manager.FetchInGamePlayer(actInGameSound);
            inGamePlayer.MediaEnded += InGamePlayerEnded; //If ingamesong is over, event is handled

            menuPlayer = manager.FetchMenuPlayer(actMenuSound);
            menuPlayer.MediaEnded += MenuPlayerEnded; //If menusong is over, event is handled
  
        }

        /// <summary>
        /// Scales values between 0 and 10 for playervolume
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        private float scaleForVolume(int value) //Value must be between 0 and 10
        {
            float f = (float) (value/100.0);
            if (value <= 100 && value >= 0)
            {
                return f;
            }
            else
            {
                throw new NotSupportedException();
            }

        }

        #region inGameSounds
        /// <summary>
        /// Starts soundreplay for the Game
        /// </summary>
        public void StartInGameSound()
        {
            StopMenuSound();
            inGamePlayer.Play();
        }

        /// <summary>
        /// Stops soundreplay for the game
        /// </summary>
        public void StopInGameSound()
        {
            inGamePlayer.Stop();
        }

        /// <summary>
        /// Skips current track
        /// </summary>
        public void SkipInGameSound()
        {
            StopInGameSound();
            actInGameSound = (actInGameSound + 1) % amountInGameSounds; //Next index is between 0 and the amount of music files
            inGamePlayer = manager.FetchInGamePlayer(actInGameSound); 
            StartInGameSound();
        }

        /// <summary>
        /// Adjusts the volume of the game music
        /// </summary>
        /// <param name="volume">Scaleparameter for the volume between 0.0f and 1.0f</param>
        public void AdjustInGameVolume(int volume) 
        {
            inGamePlayer.Volume = scaleForVolume(volume); //volume needs to be between 0.0f and 1.0f
        }

        /// <summary>
        /// If a track is over, skip to the next one
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void InGamePlayerEnded(object sender, EventArgs e)
        {
            SkipInGameSound();
        }
        #endregion

        #region menuSounds
        /// <summary>
        /// Starts music for the menu
        /// </summary>
        public void StartMenuSound()
        {
            StopInGameSound();
            menuPlayer.Play();
        }

        /// <summary>
        /// Stops the music for the menu
        /// </summary>
        public void StopMenuSound()
        {
            menuPlayer.Stop();
        }

        /// <summary>
        /// Skips music for menu
        /// </summary>
        public void SkipMenuSound()
        {
            StopMenuSound();
            actMenuSound = (actMenuSound + 1) % amountMenuSounds; //Next index is between 0 and the amount of music files
            menuPlayer = manager.FetchMenuPlayer(actMenuSound);
            StartMenuSound();
        }


        /// <summary>
        /// Adjusts the volume of the menu music
        /// </summary>
        /// <param name="volume">Scaleparameter for the volume between 0.0f and 1.0f</param>
        public void AdjustMenuVolume(int volume) //volume needs to be between 0.0f and 1.0f
        {
			menuPlayer.Volume = scaleForVolume(volume);
        }

        /// <summary>
        /// If a track is over, skip to the next one
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MenuPlayerEnded(object sender, EventArgs e)
        {
            SkipMenuSound();
        }
        #endregion

        #region effectSounds
        /// <summary>
        /// Plays an effect sound
        /// </summary>
        /// <param name="effect">Enumtype of the Soundeffect to be played</param>
        public void PlayEffectSound(SoundEffect effect)
        {
            SoundPlayer p = manager.FetchEffectPlayer(effect);
            p.Play();
        }
        #endregion
    }
}
