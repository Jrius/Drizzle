/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.util.Vector;
import java.util.HashMap;

public class common
{
    
    public static Vector<String> filterFilenamesByExtension(Vector<String> files, String extension)
    {
        Vector<String> result = new Vector<String>();
        for(String file: files) if(file.endsWith(extension)) result.add(file);
        return result;
    }
    
    public static String getAgenameFromFilename(String filename)
    {
        int pos = filename.lastIndexOf("/");
        if(pos != -1)
        {
            filename = filename.substring(pos+1);
        }
        
        pos = filename.lastIndexOf("\\");
        if(pos != -1)
        {
            filename = filename.substring(pos+1);
        }
        
        pos = filename.lastIndexOf(".");
        if(pos != -1)
        {
            filename = filename.substring(0, pos);
        }
        
        pos = filename.indexOf("_");
        if(pos != -1)
        {
            filename = filename.substring(0,pos);
        }
        
        return filename;
    }
    public static String replaceAgenameIfApplicable(String filename, HashMap<String, String>agenames)
    {
        String agename = common.getAgenameFromFilename(filename);
        String newagename = agenames.get(agename);
        if(newagename!=null)
        {
            return newagename+filename.substring(agename.length());
        }
        else
        {
            return filename;
        }
    }

}
