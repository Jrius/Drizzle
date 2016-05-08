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
//import java.util.Vector;

//not tested with pots, but should work fine according to disassembly.
public class x00AESittingModifier extends uruobj
{
    //Objheader xheader;
    
    plSingleModifier parent;
    byte u1;
    int count;
    Uruobjectref[] references;
    
    public x00AESittingModifier(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plSingleModifier(c);//,false);
        u1 = c.readByte();
        count = c.readInt();
        references = c.readArray(Uruobjectref.class, count);
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        data.writeByte(u1);
        data.writeInt(count);
        data.writeArray2(references);
    }
}
