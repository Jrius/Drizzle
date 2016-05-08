/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.Vector;
import java.io.File;
import shared.*;

public class sdlfile
{
    byte[] data;
    //Vector<Statedesc> statedescs;
    parsers.sdlparser.Sdlfile sdl;

    public sdlfile(String filename)
    {
        this(new File(filename));
    }
    public sdlfile(File file)
    {
        m.msg(file.getName());
        data = FileUtils.ReadFile(file);
        //data = uru.UruCrypt.DecryptWhatdoyousee(data);
    }
    public void parse()
    {
        //char[] data2 = b.BytesToString(data).toCharArray();
        sdl = parsers.sdlparser.parse(data);
    }
    public static void test()
    {
        File sdldir = new File("H:/DontBackup/prps/mouldataserver/files/SDL");
        for(File child: sdldir.listFiles())
        {
            if(child.isFile())
            {
                sdlfile sdl = new sdlfile(child);
                sdl.parse();
                int dummy=0;
            }
        }
    }

    /*private static class SdlTokenizer
    {
        String data;
        int pos;
        int len;
        public SdlTokenizer(byte[] data2)
        {
            data = b.BytesToString(data2);
            pos = 0;
            len = data.length();
        }
        public SdlToken next()
        {
            StringBuilder s = new StringBuilder();
            boolean insidetoken = false;
            while(true)
            {
                if(pos==len) return SdlToken.eof();

                char ch = data.charAt(pos);
                if(ch==' ' && !insidetoken)
                {
                    //continue looking for token start.
                }
                else if(ch==' ' && insidetoken)
                {
                    return new SdlToken(s.toString());
                }
                else
                {
                    s.
                }
                pos++;
            }
        }
    }
    private static class SdlToken
    {
        String str;
        public SdlToken(){}
        public SdlToken(String s)
        {
            str = s;
        }
        static SdlToken eof()
        {
            return new SdlToken();
        }
    }

    public static class Statedesc
    {

    }*/
}
