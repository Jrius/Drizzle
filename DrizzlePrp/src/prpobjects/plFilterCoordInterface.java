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


public class plFilterCoordInterface extends uruobj
{
    public static final int kNoRotation = 0x1;
    public static final int kNoTransX   = 0x2;
    public static final int kNoTransY   = 0x4;
    public static final int kNoTransZ   = 0x8;

    plCoordinateInterface parent;
    int filterMask; //filterMask was u1
    Transmatrix refParentLocalToWorld; //refParentLocalToWorld was matrix
    
    public plFilterCoordInterface(context c) throws readexception
    {
        parent = new plCoordinateInterface(c);
        filterMask = c.readInt();
        refParentLocalToWorld = new Transmatrix(c);
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(filterMask);
        refParentLocalToWorld.compile(c);
    }
    
}
