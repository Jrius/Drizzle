/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auto;

/**
 *
 * @author Dustin
 */

import shared.FileUtils;
import shared.b;
import java.util.Vector;

public class FindSdlInDump
{
    
    public static void FindSdlInDump(String dumpfile, String outfolder)
    {
        //read dump file
        byte[] dump = FileUtils.ReadFile(dumpfile); //could be quite large; hundreds of MB.

        //find where "STATEDESC" occurs.
        byte[] statedesc = new byte[]{'S','T','A','T','E','D','E','S','C'}; //it's always caps in mqo
        //FileUtils.ReadFileAsString(dumpfile)
        Vector<Integer> offsets =  b.findBytes_All(dump, statedesc);
        
        //do each STATEDESC.
        int curpos = 0;
        for(int offset: offsets)
        {
            if(offset>curpos) //don't include this statedesc if it was already handled.
            {
                //find previous non-text char and next non-text char.
                int start = findPrevNonText(dump, offset);
                int end = findNextNonText(dump, offset);
                curpos = end;
                byte[] curSdlText = b.substr(dump, start+1, end-start-1);
                String curSdlStr = b.BytesToString(curSdlText);
                
                //write to file.
                FileUtils.WriteFile(outfolder+"/"+Integer.toString(start) +".sdl.txt", curSdlText);
                int dummy=0;
            }
        }
        
        //find the sdl filenames
        byte[] sdlslash = new byte[]{'s','d','l','/'};
        byte[] dotsdl = new byte[]{'.','s','d','l'};
        Vector<Integer> offsets2 =  b.findBytes_All(dump, sdlslash);
        //String names = "";
        java.util.LinkedHashSet<String> names = new java.util.LinkedHashSet<String>();
        for(int offset: offsets2)
        {
            int end = b.findBytes_Once(dump, dotsdl, offset);
            byte[] name = b.substr(dump, offset+4, end-offset-4);
            String namestr = b.BytesToString(name);
            //names += namestr + "\n";
            names.add(namestr);
        }
        String namereport = "";
        for(String name: names)
        {
            namereport += name + "\n";
        }
        FileUtils.WriteFile(outfolder+"/names.txt", b.StringToBytes(namereport));
        
        
    }
    
    private static final int findPrevNonText(byte[] bs, int offset)
    {
        while(isCharText(bs[offset]))
        {
            offset--;
        }
        return offset;
    }
    
    private static final int findNextNonText(byte[] bs, int offset)
    {
        while(isCharText(bs[offset]))
        {
            offset++;
        }
        return offset;
    }
    
    private static final boolean isCharText(byte b)
    {
        //if(b==9 || b==10 || b==13) return true;
        //if(b>=32 && b<=126) return true;
        //return false;
        return  b==9 || b==10 || b==13 || ( b>=32 && b<=126 );
    }

}
