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
//import java.util.Vector;
import shared.*;


public class plBlower extends uruobj
{
    plSingleModifier parent;
    float masterPower;
    float directRate;
    float impulseRate;
    float springConstant;
    
    public plBlower(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        masterPower = c.readFloat();
        directRate = c.readFloat();
        impulseRate = c.readFloat();
        springConstant = c.readFloat();
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeFloat(masterPower);
        c.writeFloat(directRate);
        c.writeFloat(impulseRate);
        c.writeFloat(springConstant);
    }
    
}
