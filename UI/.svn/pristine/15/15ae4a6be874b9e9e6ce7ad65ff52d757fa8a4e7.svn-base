using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UI.Builder
{
    public class InfoStringBuilder
    {
        public static string BuildOptionInfos(Dictionary<string, string> opt){
            StringBuilder builder = new StringBuilder();
            
            foreach(string key in opt.Keys){
                builder.Append(key).Append(" : ").Append(opt[key]).Append("\n");           
            }
            return builder.ToString();
        }
    }
}
