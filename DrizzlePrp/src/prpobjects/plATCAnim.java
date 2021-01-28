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
    public plAGAnim parent;
    Flt u1; //float
    byte u2; //bool
    Flt u3; //float
    Flt u4; //float
    byte u5; //bool
    byte u6;
    Flt u7; //float
    Flt u8; //float
    Flt u9; //float
    byte u10;
    Flt u11; //float
    Flt u12; //float
    Flt u13; //float
    int count1;
    //int[] u14;
    SubAtcanim2[] u14;
    int count2;
    SubAtcanim[] substuff;
    int count3;
    //int[] u15;
    Flt[] u15;
    
    public plATCAnim(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plAGAnim(c);//,false);
        u1 = new Flt(c);
        u2 = data.readByte();
        u3 = new Flt(c);
        u4 = new Flt(c);
        u5 = data.readByte();
        u6 = data.readByte();
        u7 = new Flt(c);
        u8 = new Flt(c);
        u9 = new Flt(c);
        u10 = data.readByte();
        u11 = new Flt(c);
        u12 = new Flt(c);
        u13 = new Flt(c);
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
        u1.compile(c);
        c.writeByte(u2);
        u3.compile(c);
        u4.compile(c);
        c.writeByte(u5);
        c.writeByte(u6);
        u7.compile(c);
        u8.compile(c);
        u9.compile(c);
        c.writeByte(u10);
        u11.compile(c);
        u12.compile(c);
        u13.compile(c);
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
        Urustring string;
        Flt u1;
        
        public SubAtcanim2(context c)
        {
            string = new Urustring(c);
            u1 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            string.compile(c);
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
