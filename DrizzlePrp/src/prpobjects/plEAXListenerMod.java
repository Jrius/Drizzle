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
import shared.readexception;
//import java.util.Vector;

//I reverse-engineered this myself, via decompilation.
public class plEAXListenerMod extends uruobj
{
    plSingleModifier parent;
    Uruobjectref ref;
    int u1;
    Flt u2;
    Flt u3;
    int u4;
    int u5;
    int u6;
    Flt u7;
    Flt u8;
    Flt u9;
    int u10;
    Flt u11;
    int u12;
    Flt[] u13; //9 of them
    int u14;
    
    public plEAXListenerMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        ref = new Uruobjectref(c);
        u1 = c.readInt();
        u2 = new Flt(c);
        u3 = new Flt(c);
        u4 = c.readInt();
        u5 = c.readInt();
        u6 = c.readInt();
        u7 = new Flt(c);
        u8 = new Flt(c);
        u9 = new Flt(c);
        u10 = c.readInt();
        u11 = new Flt(c);
        u12 = c.readInt();
        u13 = c.readArray(Flt.class, 9);
        u14 = c.readInt();
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        ref.compile(c);
        c.writeInt(u1);
        u2.compile(c);
        u3.compile(c);
        c.writeInt(u4);
        c.writeInt(u5);
        c.writeInt(u6);
        u7.compile(c);
        u8.compile(c);
        u9.compile(c);
        c.writeInt(u10);
        u11.compile(c);
        c.writeInt(u12);
        c.writeArray(u13);
        c.writeInt(u14);
    }
    
}
