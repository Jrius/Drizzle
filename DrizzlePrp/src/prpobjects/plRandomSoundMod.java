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

import shared.Flt;
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
public class plRandomSoundMod extends uruobj
{
    //Objheader xheader;
    
    /*int u1;
    int u2;
    short u3;
    Flt u4;
    Flt u5;
    short u6;*/
    public PlRandomCommandMod parent;
    short count;
    subRandomSoundMod[] subs;
    
    public plRandomSoundMod(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        /*u1 = c.readInt(); e.ensure(u1==0);
        u2 = c.readInt(); e.ensure(u2==0);
        u3 = c.readShort();
        u4 = new Flt(c); //always approx. round numbers.
        u5 = new Flt(c); //always approx. round numbers.
        u6 = c.readShort();*/
        parent = new PlRandomCommandMod(c);
        count = c.readShort();
        subs = new subRandomSoundMod[count];
        for(int i=0;i<count;i++)
        {
            subs[i] = new subRandomSoundMod(c);
        }
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeShort(count);
        for(int i=0;i<count;i++)
        {
            subs[i].compile(c);
        }
    }
    /*public void compile(Bytedeque c)
    {
        c.writeInt(u1);
        c.writeInt(u2);
        c.writeShort(u3);
        u4.compile(c);
        u5.compile(c);
        c.writeShort(u6);
    }*/
    public static class subRandomSoundMod extends uruobj
    {
        short count;
        short u2;
        short[] u3;
        public subRandomSoundMod(context c)
        {
            count = c.readShort();
            u2 = c.readShort();
            u3 = c.readShorts(count);
                    
        }
        
        public void compile(Bytedeque c)
        {
            c.writeShort(count);
            c.writeShort(u2);
            c.writeShorts(u3);
        }
    }
    public static class PlRandomCommandMod extends uruobj
    {
        plSingleModifier parent;
        public byte mode;
        public byte state;
        public Flt minDelay;
        public Flt maxDelay;
        
        public PlRandomCommandMod(context c)
        {
            parent = new plSingleModifier(c);
            mode = c.readByte();
            state = c.readByte();
            minDelay = new Flt(c);
            maxDelay = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeByte(mode);
            c.writeByte(state);
            minDelay.compile(c);
            maxDelay.compile(c);
        }
    }
}
