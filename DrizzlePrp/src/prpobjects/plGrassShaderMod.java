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


public class plGrassShaderMod extends uruobj
{
    plSynchedObject parent;
    Uruobjectref ref;
    Flt[] u1;
    Flt[] u2;
    Flt[] u3;
    Flt[] u4;
    
    public plGrassShaderMod(context c) throws readexception
    {
        e.ensure(c.readversion==6||c.readversion==4);
        
        parent = new plSynchedObject(c);
        ref = new Uruobjectref(c);
        u1 = c.readArray(Flt.class, 6);
        u2 = c.readArray(Flt.class, 6);
        u3 = c.readArray(Flt.class, 6);
        u4 = c.readArray(Flt.class, 6);

        throw new shared.readwarningexception("plGrassShaderMod: can read okay, but failing in order to ignore.");
    }
    
    public void compile(Bytedeque c)
    {
        m.warn("compile not implemented.",this.toString());
        m.warn("not tested with pots.",this.toString());
    }
    
}
