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


public class plDynamicCamMap extends uruobj
{
    plDynamicEnvMap.plRenderTarget target;
    Flt[] u1;
    byte u2;
    Uruobjectref u3;
    Uruobjectref u4;
    byte u5;
    Uruobjectref[] u6;
    int u7;
    Uruobjectref[] u8;
    int u9;
    Urustring[] u10;
    Uruobjectref u11;
    byte u12;
    Uruobjectref[] u13;
    
    public plDynamicCamMap(context c) throws readexception
    {
        e.ensure(c.readversion==6||c.readversion==4);
        
        target = new plDynamicEnvMap.plRenderTarget(c);
        u1 = c.readArray(Flt.class, 8);
        u2 = c.readByte();
        u3 = new Uruobjectref(c);
        u4 = new Uruobjectref(c);
        u5 = c.readByte();
        int count = b.ByteToInt32(u5); //should this be signed instead?
        u6 = c.readArray(Uruobjectref.class, count);
        u7 = c.readInt();
        u8 = c.readArray(Uruobjectref.class, u7);
        u9 = c.readInt();
        u10 = c.readArray(Urustring.class, u9);
        u11 = new Uruobjectref(c);
        u12 = c.readByte();
        int count2 = b.ByteToInt32(u12);
        u13 = c.readArray(Uruobjectref.class, count2);
        
        throw new shared.readwarningexception("plDynamicCamMap: can read okay, but failing in order to ignore.");
    }
    
    public void compile(Bytedeque c)
    {
        m.warn("compile not implemented.",this.toString());
        m.warn("not tested with pots.",this.toString());
    }
    
}
