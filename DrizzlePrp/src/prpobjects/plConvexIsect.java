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


public class plConvexIsect extends uruobj
{
    plVolumeIsect parent;
    short count;
    public PrpVolumeIsect.PlConvexPlane[] planes;

    public plConvexIsect(context c) throws readexception
    {
        parent = new plVolumeIsect(c);
        count = c.readShort();
        int count2 = b.Int16ToInt32(count);
        planes = c.readArray(PrpVolumeIsect.PlConvexPlane.class, count2);
    }

    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeShort(count);
        c.writeArray2(planes);
    }
}

