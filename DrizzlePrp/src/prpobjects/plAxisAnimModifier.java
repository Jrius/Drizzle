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


public class plAxisAnimModifier extends uruobj
{
    plSingleModifier parent;
    Uruobjectref ref1;
    Uruobjectref ref2;
    Uruobjectref ref3;
    byte b3;
    PrpTaggedObject emb1;
    Wpstr s1;
    
    public plAxisAnimModifier(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        if(c.readversion==4)
        {
            byte b1 = c.readByte();
            byte b2 = c.readByte();
        }
        ref1 = new Uruobjectref(c);
        ref2 = new Uruobjectref(c);
        ref3 = new Uruobjectref(c);
        b3 = c.readByte();
        emb1 = new PrpTaggedObject(c);
        s1 = new Wpstr(c);
        if(c.readversion==4)
        {
            int count1 = c.readInt();
            Flt[] flts1 = c.readArray(Flt.class, count1);
            int count2 = c.readInt();
            Flt[] flts2 = c.readArray(Flt.class, count2);
            byte b4 = c.readByte();
            Flt f1 = new Flt(c);
            Flt f2 = new Flt(c);
            Flt f3 = new Flt(c);
            
            Flt f4 = new Flt(c);
            Flt f5 = new Flt(c);
            Flt f6 = new Flt(c);
            Flt f7 = new Flt(c);
            Flt f8 = new Flt(c);
            byte b5 = c.readByte();
            byte b6 = c.readByte();
            byte b7 = c.readByte();
            byte b8 = c.readByte();
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        ref1.compile(c);
        ref2.compile(c);
        ref3.compile(c);
        c.writeByte(b3);
        emb1.compile(c);
        s1.compile(c);
        
    }
    
}
