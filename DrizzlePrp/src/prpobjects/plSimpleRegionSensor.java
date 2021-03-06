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


public class plSimpleRegionSensor extends uruobj
{
    plSingleModifier parent;
    byte hasObj1;
    PrpTaggedObject obj1;
    byte hasObj2;
    PrpTaggedObject obj2;
    
    public plSimpleRegionSensor(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        hasObj1 = c.readByte();
        if(hasObj1!=0)
        {
            obj1 = new PrpTaggedObject(c);
        }
        hasObj2 = c.readByte();
        if(hasObj2!=0)
        {
            obj2 = new PrpTaggedObject(c);
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeByte(hasObj1);
        if(hasObj1!=0)
        {
            obj1.compile(c);
        }
        c.writeByte(hasObj2);
        if(hasObj2!=0)
        {
            obj2.compile(c);
        }
    }
    
}
