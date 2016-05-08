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
import shared.*;
//import java.util.Vector;


public class plDistOpacityMod extends uruobj
{
    plSingleModifier parent;
    Flt f1;
    Flt f2;
    Flt f3;
    Flt f4;
    
    public plDistOpacityMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        //these 4 floats are actually read in a for(int i=0;i<4;i++) loop.
        f1 = new Flt(c);
        f2 = new Flt(c);
        f3 = new Flt(c);
        f4 = new Flt(c);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        f1.compile(c);
        f2.compile(c);
        f3.compile(c);
        f4.compile(c);
    }
    
}
