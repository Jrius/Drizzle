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

//I reverse-engineered this one. Works in pots's Eder Kemo, too.
public class x00FFStereizer extends uruobj
{
    //Objheader xheader;
    //int u1;
    //int u2;
    //int u3;
    plSingleModifier parent;
    Flt u4;
    Flt u5;
    Flt u6;
    Flt u7;
    Flt u8;
    Flt u9;
    Flt u10;
    Flt u11;
    
    public x00FFStereizer(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        //This should probably be a SynchedObject parent;
        //u1 = c.readInt(); e.ensure(u1==0||u1==4);
        //u2 = c.readInt(); e.ensure(u2==1);
        //u3 = c.readInt(); e.ensureflags(u3,0,1);
        parent = new plSingleModifier(c);
        u4 = new Flt(c);
        u5 = new Flt(c);
        u6 = new Flt(c);
        u7 = new Flt(c);
        u8 = new Flt(c);
        u9 = new Flt(c);
        u10 = new Flt(c);
        u11 = new Flt(c);
    }
    public void compile(Bytedeque c)
    {
        //c.writeInt(u1);
        //c.writeInt(u2);
        //c.writeInt(u3);
        parent.compile(c);
        u4.compile(c);
        u5.compile(c);
        u6.compile(c);
        u7.compile(c);
        u8.compile(c);
        u9.compile(c);
        u10.compile(c);
        u11.compile(c);
    }
}
