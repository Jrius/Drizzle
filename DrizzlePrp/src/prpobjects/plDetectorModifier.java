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
import shared.readexception;
//import java.util.Vector;


public class plDetectorModifier extends uruobj
{
    public plSingleModifier parent;
    public int count;
    public Uruobjectref[] refs;
    public Uruobjectref ref2;
    public Uruobjectref ref3;
    
    public plDetectorModifier(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        count = c.readInt();
        refs = c.readArray(Uruobjectref.class, count);
        ref2 = new Uruobjectref(c);
        ref3 = new Uruobjectref(c);
        
    }
    
    public static plDetectorModifier createDefault()
    {
        plDetectorModifier e = new plDetectorModifier();
        e.parent = plSingleModifier.createDefault();
        e.ref2 = Uruobjectref.none();
        e.ref3 = Uruobjectref.none();
        return e;
    }

    private plDetectorModifier() {}
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(count);
        c.writeArray2(refs);
        ref2.compile(c);
        ref3.compile(c);
    }
    
}
