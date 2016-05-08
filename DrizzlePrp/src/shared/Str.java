/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.*;
import java.util.ArrayList;

//replacement for all other string garbage, hopefully!
public class Str
{
    Strtype type;
    String str;

    //for original reconstruction
    short[] utf16data;

    public static enum Strtype
    {
        JavaString,
        Utf16SizedAndNT,
        Utf16Sized16,
        Utf16FixedAndNT,
        Utf16NT,
    }

    public Str(String s)
    {
        type = Strtype.JavaString;
        str = s;
    }

    private Str(){}
    public static Str readAsUtf16Sized16(IBytestream c)
    {
        Str r = new Str();

        //read
        r.type = Strtype.Utf16Sized16;
        short size1 = c.readShort();
        int size = b.Int16ToInt32(size1);
        short[] utf16data = new short[size];
        for(int i=0;i<size;i++)
        {
            utf16data[i] = c.readShort();
        }
        r.utf16data = utf16data;

        //create string
        StringBuilder str = new StringBuilder();
        for(short sh: utf16data)
        {
            str.append((char)sh);
        }
        r.str = str.toString();

        return r;
    }
    public static Str readAsUtf16SizedAndNT(IBytestream c)
    {
        Str r = new Str();

        //read
        r.type = Strtype.Utf16SizedAndNT;
        int numbytes = c.readInt();
        int size = numbytes/2;
        short[] utf16data = new short[size];
        for(int i=0;i<size;i++)
        {
            utf16data[i] = c.readShort();
        }
        r.utf16data = utf16data;

        //create string
        StringBuilder str = new StringBuilder();
        for(short sh: utf16data)
        {
            if(sh==0) break;
            str.append((char)sh);
        }
        r.str = str.toString();

        return r;
    }
    public static Str readAsUtf16FixedAndNT(IBytestream c, int size)
    {
        Str r = new Str();
        
        //read
        r.type = Strtype.Utf16FixedAndNT;
        short[] utf16data = new short[size];
        for(int i=0;i<size;i++)
        {
            utf16data[i] = c.readShort();
        }
        r.utf16data = utf16data;
        
        //create string
        StringBuilder str = new StringBuilder();
        for(short sh: utf16data)
        {
            if(sh==0) break;
            str.append((char)sh);
        }
        r.str = str.toString();
        
        return r;
    }
    public static Str readAsUtf16NT(IBytestream c)
    {
        Str r = new Str();
        
        //read
        r.type = Strtype.Utf16NT;
        StringBuilder str = new StringBuilder();
        while(true)
        {
            short sh = c.readShort();
            if(sh==0) break;
            str.append((char)sh);
        }
        r.str = str.toString();
        
        return r;
    }


    public void writeAsUtf16SizedAndNT(IBytedeque c)
    {
        int numchars = str.length()+1;
        int numbytes = numchars*2;

        c.writeInt(numbytes);
        for(int i=0;i<str.length();i++)
        {
            char ch = str.charAt(i);
            c.writeShort((short)ch);
        }
        c.writeShort((short)0);
    }
    public void writeAsUtf16Sized16(IBytedeque c)
    {
        int numchars = str.length();
        short numchars2 = (short)numchars;

        c.writeShort(numchars2);
        for(int i=0;i<str.length();i++)
        {
            char ch = str.charAt(i);
            c.writeShort((short)ch);
        }
    }
    public void writeAsUtf16FixedAndNT(IBytedeque c, int size)
    {
        if(str.length()+1>size) m.throwUncaughtException("string is too big"); //perhaps we don't *have* to have the null at the end.

        for(int i=0;i<size;i++)
        {
            char ch = (i<str.length())?str.charAt(i):0;
            c.writeShort((short)ch);
        }
    }
    public void writeAsUtf16NT(IBytedeque c)
    {
        for(int i=0;i<str.length();i++)
        {
            char ch = str.charAt(i);
            c.writeShort((short)ch);
        }
        c.writeShort((short)0); //null terminator
    }

    public String toString()
    {
        return str;
    }
}
