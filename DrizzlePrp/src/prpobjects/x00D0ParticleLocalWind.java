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

//I reverse-engineered this myself.
public class x00D0ParticleLocalWind extends uruobj
{
    //Objheader xheader;
    
    Flt u1;
    Flt u2;
    Flt u3;
    byte u4; //1?
    Flt u5;
    Flt u6;
    Flt u7;
    Flt u8;
    Flt u9;
    Flt u10;
    Flt u11;
    Flt u12;
    int u13; //0?
    Flt u14;
    
    public x00D0ParticleLocalWind(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        u1 = new Flt(c);
        u2 = new Flt(c);
        u3 = new Flt(c);
        u4 = c.readByte(); e.ensureflags(u4,1,0);
        u5 = new Flt(c);
        u6 = new Flt(c);
        u7 = new Flt(c);
        u8 = new Flt(c);
        u9 = new Flt(c);
        u10 = new Flt(c);
        u11 = new Flt(c); //20.0?
        u12 = new Flt(c); //20.0?
        u13 = c.readInt(); e.ensure(u13==0||u13==0x40a00000); //the 0x40a00000 is a hack.  I can't remember why it should be zero, or if that is just an observation.
        u14 = new Flt(c); //15.0?
    }
    public void compile(Bytedeque data)
    {
        u1.compile(data);
        u2.compile(data);
        u3.compile(data);
        data.writeByte(u4);
        u5.compile(data);
        u6.compile(data);
        u7.compile(data);
        u8.compile(data);
        u9.compile(data);
        u10.compile(data);
        u11.compile(data);
        u12.compile(data);
        data.writeInt(u13);
        u14.compile(data);
    }
}
