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


public class plVolumeSensorConditionalObject extends uruobj
{
    plConditionalObject parent;
    int u3;
    int direction; //according to cobbs, 1=enter, 2=leave
    byte u4;
    
    public plVolumeSensorConditionalObject(context c) throws readexception
    {
        parent = new plConditionalObject(c);
        u3 = c.readInt();
        direction = c.readInt();
        u4 = c.readByte();
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(u3);
        c.writeInt(direction);
        c.writeByte(u4);
    }
    
    public static class PlVolumeSensorConditionalObjectNoArbitration extends uruobj
    {
        plVolumeSensorConditionalObject parent;
        
        public PlVolumeSensorConditionalObjectNoArbitration(context c) throws readexception
        {
            parent = new plVolumeSensorConditionalObject(c);
            throw new shared.readexception("Can read PlVolumeSensorConditionalObjectNoArbitration okay, but ignoring.");
        }
        public void compile(Bytedeque c)
        {
            //Perhaps we should convert these objects into PlVolumeSensorConditionalObject.
            //parent.compile(c);
            throw new shared.uncaughtexception("Shouldn't get here.");
            
        }
    }
}
