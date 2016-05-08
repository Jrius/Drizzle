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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class pfGUIProgressCtrl extends uruobj
{
    pfGUIButtonMod.PfGUIValueCtrl parent;
    Refvector animationKeys;
    Urustring animname;
    
    public pfGUIProgressCtrl(context c) throws readexception
    {
        parent = new pfGUIButtonMod.PfGUIValueCtrl(c);
        animationKeys = new Refvector(c);
        animname = new Urustring(c);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        animationKeys.compile(c);
        animname.compile(c);
    }
}
