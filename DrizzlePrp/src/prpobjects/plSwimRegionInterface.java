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


public class plSwimRegionInterface extends uruobj
{
    plObjInterface parent;
    Flt f1;
    Flt f2;
    Flt f3;
    
    public plSwimRegionInterface(context c) throws readexception
    {
        parent = new plObjInterface(c);
        f1 = new Flt(c);
        f2 = new Flt(c);
        f3 = new Flt(c);
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        f1.compile(c);
        f2.compile(c);
        f3.compile(c);
    }
    
    public static class PlSwimCircularCurrentRegion extends uruobj
    {
        plSwimRegionInterface parent;
        Flt f1;
        Flt f2;
        Flt f3;
        Flt f4;
        Flt f5;
        Uruobjectref ref6;

        public PlSwimCircularCurrentRegion(context c) throws readexception
        {
            parent = new plSwimRegionInterface(c);
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
            f4 = new Flt(c);
            f5 = new Flt(c);
            ref6 = new Uruobjectref(c);

        }

        public void compile(Bytedeque c)
        {
            parent.compile(c);
            f1.compile(c);
            f2.compile(c);
            f3.compile(c);
            f4.compile(c);
            f5.compile(c);
            ref6.compile(c);
        }

    }
    
    public static class PlSwimStraightCurrentRegion extends uruobj
    {
        plSwimRegionInterface parent;
        Flt f1;
        Flt f2;
        Flt f3;
        Flt f4;
        Uruobjectref ref5;

        public PlSwimStraightCurrentRegion(context c) throws readexception
        {
            parent = new plSwimRegionInterface(c);
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
            f4 = new Flt(c);
            ref5 = new Uruobjectref(c);

        }

        public void compile(Bytedeque c)
        {
            parent.compile(c);
            f1.compile(c);
            f2.compile(c);
            f3.compile(c);
            f4.compile(c);
            ref5.compile(c);
        }

    }
}
