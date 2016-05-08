/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import shared.Bytes;
import java.util.StringTokenizer;
import uru.Bytestream;
import shared.m;
import java.util.Vector;
import uru.context;
import java.io.StreamTokenizer;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


//This file is essentially just playing with a few parsing schemes.
//Ultimately, however, the SDL files have to be converted from Myst5 manually, since it uses some functionality apparently not present in Pots.  I.e. the ability to have statedesc vars that are really just other statedescs.

public class sdlfile extends textfile
{
    /*public sdlfile(Bytes bs)
    {
        tryparsefile(bs.getByteArray(),0);
    }
    
    static class wholefile
    {
        public static wholefile tryparsefile(byte[] bs, int pos)
        {
            tryparsecomment()
        }
    }
    static class hashcomment
    {
        Vector<Byte> string = new Vector<Byte>();
        
        public static hashcomment tryhashcomment(byte[] bs, Integer pos)
        {
            byte hash = bs[pos]; pos++;
            if(hash!='#') return null;
            
            while(!c.eof())
            {
                byte curbyte = c.readByte();
                if(curbyte==0x0d||curbyte==0x0a) return;
                string.add(curbyte);
            }
        }
    }
    
    static class tryslashcomment
    {
        Vector<Byte> string = new Vector<Byte>();
        
        public tryslashcomment(Bytestream c)
        {
            byte slash = c.readByte();
            byte star = c.readByte();
            while(!c.eof())
            {
                byte curbyte = c.readByte();
                if(curbyte=='*')
                {
                    if(c.peekByte()=='/')
                    {
                        c.readByte();
                        return;
                    }
                }
                string.add(curbyte);
            }
        }
    }
    
    public sdlfile(Bytes bs, int dummy)
    {
        try
        {
        ByteArrayInputStream is = new ByteArrayInputStream(bs.getByteArray());
        StreamTokenizer t = new StreamTokenizer(new InputStreamReader(is));
        t.slashSlashComments(true);
        t.commentChar('#');
        
        int token;
        do
        {
            token = t.nextToken();
            switch(token)
            {
                case StreamTokenizer.TT_WORD:
                    break;
                case StreamTokenizer.TT_NUMBER:
                    break;
                case StreamTokenizer.TT_EOL:
                    break;
                case StreamTokenizer.TT_EOF:
                    break;
                default:
                    break;
            }
            
        }while(token != StreamTokenizer.TT_EOF);
        
        }catch(Exception e)
        {
            int dumm2y=0;
        }
    }
    
    
    Vector<statedesc> structs = new Vector<statedesc>();
    
    public String[] tokenizeline(Bytes bs)
    {
        StringTokenizer tokens = new StringTokenizer(bs.toString());
        int numTokens = tokens.countTokens();
        String[] result = new String[numTokens];
        for(int i=0;i<numTokens;i++)
        {
            result[i] = tokens.nextToken();
        }
        return result;
    }
    
    public void parse(Bytes data)
    {
        Bytestream c = Bytestream.createFromBytes(data);
        parse(c);
        //int numLines = lines.length;
        //for(int i=0;i<numLines;i++)
        //{
        //    Bytes bs = lines[i];
        //    //String s = bs.toString();
        //    //parseline(bs);
        //    String[] curline = tokenizeline(bs);
        //    i
        //}
    }
    static class token
    {
        
    }
    static class hashcomment
    {
        Vector<Byte> string = new Vector<Byte>();
        
        public hashcomment(Bytestream c)
        {
            byte hash = c.readByte();
            while(!c.eof())
            {
                byte curbyte = c.readByte();
                if(curbyte==0x0d||curbyte==0x0a) return;
                string.add(curbyte);
            }
        }
    }
    
    static class slashcomment
    {
        Vector<Byte> string = new Vector<Byte>();
        
        public slashcomment(Bytestream c)
        {
            byte slash = c.readByte();
            byte star = c.readByte();
            while(!c.eof())
            {
                byte curbyte = c.readByte();
                if(curbyte=='*')
                {
                    if(c.peekByte()=='/')
                    {
                        c.readByte();
                        return;
                    }
                }
                string.add(curbyte);
            }
        }
    }
    public static boolean isWhitespace(byte by)
    {
        return (by==0x20||by==0x9);
    }
    static class version
    {
        public version(Bytestream c)
        {
            byte[] versionstring = c.readBytes(7);
        }
    }
    public void parse(Bytestream c)
    {
        byte curbyte = c.readByte();
        switch(curbyte)
        {
            case '#':
                parseToEndline(c);
                break;
            default:
                m.warn("Unexpected value while parsing.");
                break;
        }
    }
    public void parseToEndline(Bytestream c)
    {
        while(c.peekByte()!='\n')
        {
            //c.
        }
    }
    
    static class statedesc
    {
        version ver;
        Vector<var> vars = new Vector<var>();
        
        statedesc(context c)
        {
            
        }
    }
    
    static class version2
    {
        version2(context c)
        {
            
        }
    }
    static class var
    {
        var(context c)
        {
            
        }
    }*/
}
