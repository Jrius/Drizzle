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
public class plLineFollowMod extends uruobj
{
    plMultiModifier parent;
    PrpTaggedObject emb1;
    Uruobjectref ref1;
    Uruobjectref ref2;
    int count;
    Uruobjectref[] refs;
    int flags;
    Flt xu1;
    Flt xu2;
    Flt xu3;
    
    public plLineFollowMod(context c) throws readexception
    {
        parent = new plMultiModifier(c);
        emb1 = new PrpTaggedObject(c);
        ref1 = new Uruobjectref(c);
        ref2 = new Uruobjectref(c);
        count = c.readInt();
        refs = c.readArray(Uruobjectref.class, count);
        flags = c.readInt();
        if((flags&0x060000)!=0)
        {
            xu1 = new Flt(c);
        }
        if((flags&0x080000)!=0)
        {
            xu2 = new Flt(c);
        }
        if((flags&0x200000)!=0)
        {
            xu3 = new Flt(c);
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        emb1.compile(c);
        ref1.compile(c);
        ref2.compile(c);
        c.writeInt(count);
        c.writeArray2(refs);
        c.writeInt(flags);
        if((flags&0x060000)!=0)
        {
            xu1.compile(c);
        }
        if((flags&0x080000)!=0)
        {
            xu2.compile(c);
        }
        if((flags&0x200000)!=0)
        {
            xu3.compile(c);
        }
    }
    
}
