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
    /// Repository to manage the cursorpaths
    /// </summary>
    public class CursorRepository
    {
        private string preImagesDirPath = "pack://application:,,,/";
        private Dictionary<CursorEnum, string> _cursorPaths;
        private string absCursorDir;

        public CursorRepository() {
            _cursorPaths = new Dictionary<CursorEnum, string>();
            absCursorDir = GenerateDirPath(Properties.Settings.Default.CursorImagePath);

            InitCursorPaths();
        }


        /// <summary>
        /// returns the cursorpath of a specific cursorenum
        /// </summary>
        /// <param name="ce">cursorsenum</param>
        /// <returns>cursorpath</returns>
        public string GetCursorPath(CursorEnum ce){
            return _cursorPaths[ce];
        }

        /// <summary>
        /// initializes the cursorpaths
        /// </summary>
        private void InitCursorPaths(){
            foreach(CursorEnum ce in Enum.GetValues(typeof(CursorEnum))){
                _cursorPaths.Add(ce, GenerateCursorPath(absCursorDir, ce.ToString("g"), Properties.Settings.Default.CursorImageType));
            }
        }

        /// <summary>
        /// generate a path to a directory
        /// </summary>
        /// <param name="directory">directory</param>
        /// <returns>directorypath</returns>
        private string GenerateDirPath(string directory)
        {
            return preImagesDirPath + Properties.Settings.Default.BasicImagePath + directory;
            
        }

        /// <summary>
        /// generate the curcorspath
        /// </summary>
        /// <param name="path">path</param>
        /// <param name="cursorFile">cursorfile</param>
        /// <param name="type">type</param>
        /// <returns>cursorpath</returns>
        private string GenerateCursorPath(string path, string cursorFile, string type)
        {
            return path + cursorFile + type;
        }
    }
}
