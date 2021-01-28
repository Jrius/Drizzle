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

package prpobjects;

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;

/**
 *
 * @author user
 */
public class plSynchedObject extends uruobj
{
    //Objheader xheader;
    //x0002Keyedobject parent;
    public int flags;
    short xstringcount;
    Wpstr[] sdllinks;
    
    short xstringcount2;
    Wpstr[] sdllinks2;
    
    /*public PlSynchedObject(context c) //handy
    {
        this(c);//,false);
    }*/
    private plSynchedObject(){}
    public static plSynchedObject createEmpty()
    {
        return new plSynchedObject();
    }
    public static plSynchedObject createDefault()
    {
        plSynchedObject result = plSynchedObject.createEmpty();
        result.flags = 0;
        return result;
    }
    public plSynchedObject(context c)//,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        //parent = new x0002Keyedobject(data);
        flags = data.readInt(); e.ensureflags(flags,0x00,0x04,0x0C,0x10,0x20,0x28,0x38,0x80,0x84,0x8C); //if fails, check cobbs
        if(c.readversion==3||c.readversion==6)
        {
            if ((flags & 0x10)!=0)
            {
                xstringcount = data.readShort();
                int count = b.Int16ToInt32(xstringcount);
                sdllinks = new Wpstr[count];
                for(int i=0;i<count;i++)
                {
                    sdllinks[i] = new Wpstr(c);
                }

            }
            if((flags & 0x40)!=0)
            {
                m.warn("plsynchedobject: haven't tested this yet.");
                xstringcount2 = data.readShort();
                int count2 = b.Int16ToInt32(xstringcount2);
                sdllinks2 = new Wpstr[count2];
                for(int i=0;i<count2;i++)
                {
                    sdllinks2[i] = new Wpstr(c);
                }
                //haven't implemented this yet.
                //m.err("plsynchedobject: haven't implemented this yet.");
            }
        }
        else if(c.readversion==4||c.readversion==7)
        {
            //if neither of bits 2 nor 3 are set...
            //I'm assuming that I should be assigning to the first set.  The 2nd set is hardly (possibly never) used.
            if ((flags & 0x6)==0)
            {
                //m.warn("plsynchedobject: untested case 2.");
                xstringcount = data.readShort();
                int count = b.Int16ToInt32(xstringcount);
                sdllinks = new Wpstr[count];
                for(int i=0;i<count;i++)
                {
                    sdllinks[i] = new Wpstr(c);
                }

            }
        }
    }
    public void compile(Bytedeque deque)
    {
        deque.writeInt(flags);
        if ((flags & 0x10)!=0)
        {
            deque.writeShort(xstringcount);
            int count = b.Int16ToInt32(xstringcount);
            for(int i=0;i<count;i++)
            {
                sdllinks[i].compile(deque);
            }
        }
    }
    public plSynchedObject deepClone()
    {
        plSynchedObject result = new plSynchedObject();
        result.flags = flags;
        if(sdllinks!=null)
        {
            result.sdllinks = new Wpstr[sdllinks.length];
            for(int i=0;i<sdllinks.length;i++)
            {
                result.sdllinks[i] = sdllinks[i].deepClone();
            }
        }
        if(sdllinks2!=null)
        {
            result.sdllinks2 = new Wpstr[sdllinks2.length];
            for(int i=0;i<sdllinks.length;i++)
            {
                result.sdllinks2[i] = sdllinks2[i].deepClone();
            }
        }
        result.xstringcount = xstringcount;
        result.xstringcount2 = xstringcount2;
        return result;
    }
}
