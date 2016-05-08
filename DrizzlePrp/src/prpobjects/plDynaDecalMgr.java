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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plDynaDecalMgr extends uruobj
{
    plSynchedObject parent;
    Uruobjectref ref1;
    Uruobjectref ref2;
    int count1;
    Uruobjectref[] refs1;
    int count2;
    Uruobjectref[] refs2;
    int u1;
    int u2;
    int u3;
    Flt[] flts;
    Vertex u4;
    Flt u5;
    int count3;
    Uruobjectref[] refs3;
    
    public plDynaDecalMgr(context c) throws readexception
    {
        parent = new plSynchedObject(c);
        ref1 = new Uruobjectref(c);
        ref2 = new Uruobjectref(c);
        count1 = c.readInt();
        refs1 = c.readArray(Uruobjectref.class, count1);
        count2 = c.readInt();
        refs2 = c.readArray(Uruobjectref.class, count2);
        u1 = c.readInt();
        u2 = c.readInt();
        u3 = c.readInt();
        flts = c.readArray(Flt.class, 7);
        u4 = new Vertex(c);
        u5 = new Flt(c);
        count3 = c.readInt();
        refs3 = c.readArray(Uruobjectref.class, count3);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        ref1.compile(c);
        ref2.compile(c);
        c.writeInt(count1);
        c.writeArray2(refs1);
        c.writeInt(count2);
        c.writeArray2(refs2);
        c.writeInt(u1);
        c.writeInt(u2);
        c.writeInt(u3);
        c.writeArray(flts);
        u4.compile(c);
        u5.compile(c);
        c.writeInt(count3);
        c.writeArray2(refs3);
    }
    
}
