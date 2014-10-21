using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Helper
{
    /*
     * Utility class for Helpermethods
     */
    public static class Utils
    {
       /// <summary>
        /// Creates random identification string for callback message
       /// </summary>
       /// <returns></returns>
       public static string CreateRandomString()
        {
            Random random = new Random();
            long tmp = random.Next(DateTime.Now.Millisecond);
            return tmp.ToString("X4");
        }
    }
}
