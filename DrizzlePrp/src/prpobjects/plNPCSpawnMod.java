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
import shared.*;


public class plNPCSpawnMod extends uruobj
{
    plSingleModifier parent;
    Urustring s1;
    Urustring s2;
    byte b3;
    byte b4;
    PrpTaggedObject xobj5;
    
    public plNPCSpawnMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        s1 = new Urustring(c);
        s2 = new Urustring(c);
        b3 = c.readByte();
        b4 = c.readByte();
        if(b4!=0)
        {
            xobj5 = new PrpTaggedObject(c);
        }
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        s1.compile(c);
        s2.compile(c);
        c.writeByte(b3);
        c.writeByte(b4);
        if(b4!=0)
        {
            xobj5.compile(c);
        }
    }
    
}
