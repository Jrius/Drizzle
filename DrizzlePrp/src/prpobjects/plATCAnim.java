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
public class plATCAnim extends uruobj
{
    //Objheader xheader;
    // (once again, thanks to H-uru HSPlasma for the variable names)
    public plAGAnim parent;
    public Flt initial; //float
    public byte autoStart; //bool
    public Flt loopStart; //float
    public Flt loopEnd; //float
    public byte loop; //bool
    public byte easeInType;
    public Flt easeInMin; //float
    public Flt easeInMax; //float
    public Flt easeInLength; //float
    public byte easeOutType;
    public Flt easeOutMin; //float
    public Flt easeOutMax; //float
    public Flt easeOutLength; //float
    public int count1;
    //int[] u14;
    public SubAtcanim2[] u14;
    public int count2;
    public SubAtcanim[] substuff;
    public int count3;
    //int[] u15;
    public Flt[] u15;
    
    public plATCAnim(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plAGAnim(c);//,false);
        initial = new Flt(c);
        autoStart = data.readByte();
        loopStart = new Flt(c);
        loopEnd = new Flt(c);
        loop = data.readByte();
        easeInType = data.readByte();
        easeInMin = new Flt(c);
        easeInMax = new Flt(c);
        easeInLength = new Flt(c);
        easeOutType = data.readByte();
        easeOutMin = new Flt(c);
        easeOutMax = new Flt(c);
        easeOutLength = new Flt(c);
        count1 = data.readInt();
        //u14 = data.readInts(count1);
        u14 = c.readArray(SubAtcanim2.class, count1);
        count2 = data.readInt();
        substuff = new SubAtcanim[count2];
        for(int i=0;i<count2;i++)
        {
            substuff[i] = new SubAtcanim(c);
        }
        count3 = data.readInt();
        //u15 = data.readInts(count3);
        u15 = c.readArray(Flt.class, count3);
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        initial.compile(c);
        c.writeByte(autoStart);
        loopStart.compile(c);
        loopEnd.compile(c);
        c.writeByte(loop);
        c.writeByte(easeInType);
        easeInMin.compile(c);
        easeInMax.compile(c);
        easeInLength.compile(c);
        c.writeByte(easeOutType);
        easeOutMin.compile(c);
        easeOutMax.compile(c);
        easeOutLength.compile(c);
        c.writeInt(count1);
        c.writeArray2(u14);
        c.writeInt(count2);
        for(int i=0;i<count2;i++)
        {
            substuff[i].compile(c);
        }
        c.writeInt(count3);
        c.writeArray(u15);
    }
    
    public static class SubAtcanim2 extends uruobj
    {
        Urustring channel;
        Flt u1; // might be enabled, but it's a bool, not float...
        
        public SubAtcanim2(context c)
        {
            channel = new Urustring(c);
            u1 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            channel.compile(c);
            u1.compile(c);
        }
    }
    public static class SubAtcanim extends uruobj
    {
        Urustring string;
        Flt u1; //float
        Flt u2; //float
        
        public SubAtcanim(context c)
        {
            string = new Urustring(c);
            u1 = new Flt(c);
            u2 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            string.compile(c);
            u1.compile(c);
            u2.compile(c);
        }
    }
}
