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


public class plClothingBase extends uruobj
{
    Urustring str1;
    byte b2;
    Uruobjectref xref3;
    Urustring str4;
    
    public plClothingBase(context c) throws readexception
    {
        str1 = new Urustring(c);
        b2 = c.readByte();
        if(b2!=0)
        {
            xref3 = new Uruobjectref(c);
        }
        str4 = new Urustring(c);
    }
    
    public void compile(Bytedeque c)
    {
        str1.compile(c);
        c.writeByte(b2);
        if(b2!=0)
        {
            xref3.compile(c);
        }
        str4.compile(c);
    }
    
}
