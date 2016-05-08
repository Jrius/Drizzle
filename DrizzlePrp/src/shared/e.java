/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package shared;

//import gui.Main;
import shared.m;

/**
 *
 * @author user
 */
public class e
{
    public static void force(boolean statement)
    {
        if(!statement)
        {
            m.throwUncaughtException("Condition not met.");
        }
    }
    public static void ensure(boolean statement)
    {
        if(!statement)
        {
            m.warn("ensure: condition not met.");
        }
    }
    public static void allowflags(int var, int... allowedflags)
    {
        int allflags = 0;
        for(int flag: allowedflags)
        {
            allflags |= flag;
        }
        
        int result = var & (~allflags);
        if(result!=0)
        {
            m.warn("Flag not in list of allowed flags. These are the disallowed flags: ",Integer.toBinaryString(result));
        }
    }
    /*public static void ensure(short a, int ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==(short)(options[i])) return;
        }
        Main.message("ensure: element is not in set.");
    }*/
    public static <T> void ensure(T a, T ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a.equals(options[i])) return;
        }
        m.warn("ensure: element is not in set.");
    }
    /*public static void ensure(uru.moulprp.Typeid a, uru.moulprp.Typeid ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==options[i]) return;
        }
        Main.message("ensure: typeid element is not in set.");
    }*/
    public static boolean isin(int a, int... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==options[i]) return true;
        }
        return false;
    }
    public static void ensureflags(int a, int ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==options[i]) return;
        }
         m.warn("ensure: element is not in set.");
    }
    public static void ensure(byte a, byte ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==options[i]) return;
        }
        m.warn("ensure: element is not in set.");
    }
    /*public static void ensure(byte a, int ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==(byte)(options[i])) return;
        }
        Main.message("ensure: element is not in set.");
    }*/
    public static void ensure(short a, short ... options)
    {
        for(int i=0; i<options.length; i++)
        {
            if(a==options[i]) return;
        }
        m.warn("ensure: element is not in set.");
    }
    
    public static void ensureString(byte[] string)
    {
        if(!isGoodString(string))
        {
            m.warn("ensureString: not a text string.");
        }
    }
    public static boolean isGoodString(byte[] string)
    {
        for(int i=0;i<string.length;i++)
        {
            if(string[i] < ' ' || string[i] > '~') //is it not a printable char?
            {
                if(string[i]!=0x09&&string[i]!=0x0d&&string[i]!=0x0a) //tab
                {
                    return false;
                }
            }
        }
        return true;
    }
}
