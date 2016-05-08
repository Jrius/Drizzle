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


public class plArmatureLODMod extends uruobj
{
    plArmatureMod parent;
    int count;
    sub[] subs;
    
    public plArmatureLODMod(context c) throws readexception
    {
        parent = new plArmatureMod(c);
        count = c.readInt();
        subs = c.readArray(sub.class, count);
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(count);
        c.writeArray2(subs);
    }
    public static class sub extends uruobj
    {
        Uruobjectref ref1;
        int refcount;
        Uruobjectref[] refs;
        
        public sub(context c) throws readexception
        {
            ref1 = new Uruobjectref(c);
            refcount = c.readInt();
            refs = c.readArray(Uruobjectref.class, refcount);
        }
        
        public void compile(Bytedeque c)
        {
            ref1.compile(c);
            c.writeInt(refcount);
            c.writeArray2(refs);
        }
    }
}
