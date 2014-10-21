using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Communication.Controller.Interfaces
{
    /// <summary>
    /// Interface: For all controller
    /// </summary>
    public interface IController
    {
        /// <summary>
        /// Init all necessary object for the connection
        /// </summary>
        void Init();

        /// <summary>
        /// Close all connections to the server
        /// </summary>
        void Close();
    }
}
